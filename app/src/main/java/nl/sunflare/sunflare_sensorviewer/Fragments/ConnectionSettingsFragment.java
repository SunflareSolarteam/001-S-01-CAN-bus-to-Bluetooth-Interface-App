package nl.sunflare.sunflare_sensorviewer.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import nl.sunflare.sunflare_sensorviewer.BuildConfig;
import nl.sunflare.sunflare_sensorviewer.Classes.BTConnectionService;
import nl.sunflare.sunflare_sensorviewer.Classes.DeviceListActivity;
import nl.sunflare.sunflare_sensorviewer.Interfaces.Constants;
import nl.sunflare.sunflare_sensorviewer.R;

import static nl.sunflare.sunflare_sensorviewer.MainActivity.dataProcessor;

public class ConnectionSettingsFragment extends Fragment implements Constants {

    private TextView statusLabel;
    private Button connectionBtn;
    private Switch logDatabaseSwitch;
    private Switch autoReconnectSwitch;
    private BTConnectionService btConnection;

    private LocationManager locationManager;

    private boolean locationPermissionGranted = false;
    private boolean askedForGPS = false;
    private boolean isReconnecting = false;

    private BluetoothAdapter mBluetoothAdapter;

    public static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_ENABLE_LOCATION = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    private boolean btEnabled = false;

    private void enableUi(boolean enableSettings, boolean enableConnectionButton) {
        logDatabaseSwitch.setEnabled(enableSettings);
        autoReconnectSwitch.setEnabled(enableSettings);
        connectionBtn.setEnabled(enableConnectionButton);
    }

    private void setConnectionState(int status) {
        switch (status) {
            case STATE_CONNECTED:
                connectionBtn.setText(getString(R.string.disconnect));
                connectionBtn.setOnClickListener(disconnectOnClickListener);
                statusLabel.setText(getString(R.string.connected));
                statusLabel.setTextColor(Color.GREEN);
                enableUi(false, true);
                break;
            case STATE_CONNECTING:
                statusLabel.setText("Connecting...");
                statusLabel.setTextColor(Color.BLUE);
                enableUi(false, false);
                break;
            case STATE_NOT_CONNECTED:
                connectionBtn.setText(getString(R.string.connect));
                connectionBtn.setOnClickListener(connectOnClickListener);
                statusLabel.setText(getString(R.string.not_connected));
                statusLabel.setTextColor(Color.RED);
                enableUi(true, true);
                break;
            case STATE_RECONNECTING:
                connectionBtn.setText("Abort");
                connectionBtn.setOnClickListener(abortReconnectOnClickListener);
                statusLabel.setText("Attempting to reconnect...");
                statusLabel.setTextColor(Color.BLUE);
                enableUi(false, true);
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btConnection = new BTConnectionService(mHandler);

        askForLocationPermission();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Register for broadcasts on BluetoothAdapter state change
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        getActivity().registerReceiver(mBTAdapterReceiver, filter);
        getActivity().registerReceiver(mReceiver, filter1);
        getActivity().registerReceiver(mReceiver, filter2);
        getActivity().registerReceiver(mReceiver, filter3);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    private boolean requirementsStatisfied() {
        if (!locationPermissionGranted) {
            askForLocationPermission();
            return false;
        }

        checkBtStatus();
        return btEnabled;
    }

    View.OnClickListener connectOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (requirementsStatisfied() && !DeviceListActivity.active) {
                if (askedForOptionals()) {
                    startDeviceListActivity();
                }
            }
        }
    };

    private View.OnClickListener disconnectOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            disconnectFromDevice();
        }
    };

    private View.OnClickListener abortReconnectOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setConnectionState(STATE_NOT_CONNECTED);
            disconnectFromDevice();
            btConnection.setReconnectAborted(true);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection_settings, container, false);

        TextView versionName = (TextView) view.findViewById(R.id.version_value_lbl);
        versionName.setText(String.format("%s%s", getString(R.string.version_lbl), BuildConfig.VERSION_NAME));

        statusLabel = (TextView) view.findViewById(R.id.connection_status_value);
        connectionBtn = (Button) view.findViewById(R.id.bluetooth_connection_btn);
        logDatabaseSwitch = (Switch) view.findViewById(R.id.logToDatabaseSwitch);
        autoReconnectSwitch = (Switch) view.findViewById(R.id.autoReconnectSwitch);

        autoReconnectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btConnection.setAllowedToAutoReconnect(isChecked);
            }
        });

        setConnectionState(STATE_NOT_CONNECTED);
        // Inflate the layout for this fragment
        return view;
    }

    private void disconnectFromDevice() {
        btConnection.disconnectFromDevice();
    }

    private boolean askedForOptionals() {
        if (!askedForGPS && !isGPSEnabled()) {
            buildAlertMessageNoGps();
            askedForGPS = true;
            return false;
        }
        return true;
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case STATE_NOT_CONNECTED:
                            setConnectionState(STATE_NOT_CONNECTED);
                            dataProcessor.switchDatabaseLogger(false);
                            askedForGPS = false;
                            break;
                        case STATE_CONNECTING:
                            showToast("Trying to connect...");
                            setConnectionState(STATE_CONNECTING);
                            break;
                        case STATE_CONNECTED:
                            showToast("Successfully connected!");
                            setConnectionState(STATE_CONNECTED);
                            dataProcessor.switchDatabaseLogger(logDatabaseSwitch.isChecked());
                            startLocationUpdates();
                            break;
                        case STATE_RECONNECTING:
                            setConnectionState(STATE_RECONNECTING);
                            dataProcessor.switchDatabaseLogger(false);
                            break;
                    }
                    break;
                case Constants.MESSAGE_PROCCESING_COMPLETED:
                    String message = (String) msg.obj;
                    dataProcessor.processMessage(message);
                    break;
                case Constants.MESSAGE_TOAST:
                    showToast(msg.getData().getString(Constants.TOAST));
                    break;
            }
        }
    };

    private void askForLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        } else {
            locationPermissionGranted = true;
        }
    }


    // Callback for BT
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CONNECT_DEVICE_SECURE) {// When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                btConnection.connectToDevice(data);
            }
        }
    }

    private void startDeviceListActivity() {
        Intent serverIntent = new Intent(getContext(), DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
    }

    //The BroadcastReceiver that listens for bluetooth broadcasts
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case BluetoothDevice.ACTION_FOUND:
                        //Device found
                        break;
                    case BluetoothDevice.ACTION_ACL_CONNECTED:
                        //   snackbar.dismiss();
                        break;
                    case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                        //Done searching
                        break;
                    case BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED:
                        //Device is about to disconnect
                        break;
                    case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                        dataProcessor.switchDatabaseLogger(false);
                        disconnectFromDevice();
                        if (autoReconnectSwitch.isChecked()) {
                            attemptReconnect();
                        } else {
                            setConnectionState(STATE_NOT_CONNECTED);
                        }
                        break;
                }
            }
        }
    };

    private void attemptReconnect() {
        isReconnecting = true;
    }

    private void checkBtStatus() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            btEnabled = true;
        }
    }

    private final BroadcastReceiver mBTAdapterReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        btEnabled = false;
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        btEnabled = true;
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                }
            }
        }
    };

    private boolean isGPSEnabled() {
        int locationMode = 0;

        try {
            locationMode = Settings.Secure.getInt(getContext().getContentResolver(), Settings.Secure.LOCATION_MODE);

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF;
    }

    private void startLocationUpdates() {
        if (isGPSEnabled()) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, dataProcessor);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            locationPermissionGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        disconnectFromDevice();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
        getActivity().unregisterReceiver(mBTAdapterReceiver);
        getActivity().unregisterReceiver(mReceiver);
        disconnectFromDevice();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("GPS is required to calculate current speed, enable GPS ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        // dialog.cancel();
                        askedForGPS = true;
                        startDeviceListActivity();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private int getConnectionState() {
        return btConnection.getState();
    }
}
