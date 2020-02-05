package nl.sunflare.sunflare_sensorviewer.Classes;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import nl.sunflare.sunflare_sensorviewer.Interfaces.Constants;

public class BTConnectionService implements Constants {

    private static final String TAG = "BTConnectionService";

    private final BluetoothAdapter mAdapter;

    private BluetoothDevice mmDevice;
    private static ConnectThread mConnectThread;
    private static ConnectedThread mConnectedThread;

    private boolean isAllowedToAutoReconnect;
    private boolean connectionAborted;
    private boolean reconnectAborted;

    // Unique UUID for this application
    private static final UUID MY_UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    // Member fields
    private final Handler mHandler;
    private int mState = STATE_NOT_CONNECTED;

    Executor executor = Executors.newSingleThreadExecutor();

    public BTConnectionService(Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mHandler = handler;
    }

    public void setAllowedToAutoReconnect(boolean allowedToAutoReconnect) {
        isAllowedToAutoReconnect = allowedToAutoReconnect;
    }

    public void setReconnectAborted(boolean reconnectAborted) {
        this.reconnectAborted = reconnectAborted;
    }

    public void setConnectionAborted(boolean connectionAborted) {
        this.connectionAborted = connectionAborted;
    }

    public synchronized int getState() {
        return mState;
    }

    private synchronized void updateState(int state) {
        mState = state;
        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage(MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    private synchronized void stopCurrentConnection() {
        Log.d(TAG, "Killing current connection.");
        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
    }

    private void sendToast(String message) {
        Message msg = mHandler.obtainMessage(MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(TOAST, message);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    public void connectToDevice(Intent data) {
        updateState(STATE_CONNECTING);
        stopCurrentConnection();
        // Get the device MAC address
        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        mConnectThread = new ConnectThread(mAdapter.getRemoteDevice(address));
        executor.execute(mConnectThread);
    }

    public void disconnectFromDevice() {
        if (getState() == STATE_CONNECTED) {
            stopCurrentConnection();
            handleDisconnect();
        }
    }

    private void reconnectToDevice() {
        updateState(STATE_RECONNECTING);
        stopCurrentConnection();
        mConnectThread = new ConnectThread(mmDevice);
        executor.execute(mConnectThread);
    }

    private  void handleDisconnect() {
        sendToast("Disconnected from device!");
        // check if we need to reconnect.
        if (isAllowedToAutoReconnect && !connectionAborted) {
            reconnectToDevice();
        } else {
            updateState(STATE_NOT_CONNECTED);
        }
    }

    private void handleCouldNotConnect() {
        if (isAllowedToAutoReconnect && !reconnectAborted) {
            updateState(STATE_RECONNECTING);
            reconnectToDevice();
        } else {
            updateState(STATE_NOT_CONNECTED);
            sendToast("Unable to connect to device");
        }
    }

    private class ConnectThread extends Thread {

        private BluetoothSocket mmSocket = null;

        ConnectThread(BluetoothDevice device) {
            Log.d(TAG, "ConnectThread started.");
            mmDevice = device;
        }

        public void run() {
            Log.d(TAG, "BEGIN mConnectThread");
            connectionAborted = false;
            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();

            try {
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
                try {
                    Log.d(TAG, "Succesfully created commsocket.");
                    mmSocket.connect();
                    if (mmSocket.isConnected()) {
                        Log.d(TAG, "Connected: Starting.");
                        mConnectedThread = new ConnectedThread(mmSocket);
                        mConnectedThread.start();
                    }
                } catch (Exception e) {
                    try {
                        cancel();
                        Log.d(TAG, "Could not connect because : " + e);
                    } catch (Exception e1) {
                        Log.d(TAG, "Unabled to close connection in socket.");
                    }
                    Log.d(TAG, "Could not connect to uuid: " + MY_UUID_SECURE);
                    handleCouldNotConnect();
                }
            } catch (Exception e) {
                Log.d(TAG, "Failed to create commsocket" + e.getMessage());
                handleCouldNotConnect();
            }
        }

        void cancel() {
            try {
                mmSocket.close();
            } catch (Exception e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    private class  ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;

        ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "create ConnectedThread: ");
            mmSocket = socket;
            InputStream tmpIn = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();

            } catch (Exception e) {
                Log.e(TAG, "temp sockets not created", e);
            }
            mmInStream = tmpIn;
            // Update UI title
            updateState(STATE_CONNECTED);
        }

        public void run() {
            Log.d(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[256];
            // byte 10 = ASCII "\n"
            final byte delimiter = 10;
            int readBufferPosition = 0;
            while (mState == STATE_CONNECTED) {
                try {
                    int bytesAvailable = mmInStream.available();
                    if (bytesAvailable > 0) {
                        byte[] packetBytes = new byte[bytesAvailable];
                        mmInStream.read(packetBytes);
                        for (int i = 0; i < bytesAvailable; i++) {
                            byte b = packetBytes[i];
                            if (b == delimiter) {
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(buffer, 0, encodedBytes, 0, encodedBytes.length);
                                final String data = new String(encodedBytes, StandardCharsets.US_ASCII);
                                readBufferPosition = 0;
                                mHandler.obtainMessage(MESSAGE_PROCCESING_COMPLETED, data).sendToTarget();
                            } else if (readBufferPosition < buffer.length) {
                                buffer[readBufferPosition++] = b;
                            } else {
                                System.out.println("BUFFER OVERFLOW!!");
                                readBufferPosition = 0;
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.d(TAG, "disconnected " + e);
                    handleDisconnect();
                    break;
                }
            }
        }

        void cancel() {
            try {
                mmSocket.close();
                Log.d(TAG, "cancel: 3");
            } catch (Exception e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
}
