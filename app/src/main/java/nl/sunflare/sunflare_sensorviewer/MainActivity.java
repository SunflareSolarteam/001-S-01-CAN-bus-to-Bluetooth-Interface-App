package nl.sunflare.sunflare_sensorviewer;


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
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import nl.sunflare.sunflare_sensorviewer.Adapters.SectionsPagerAdapter;
import nl.sunflare.sunflare_sensorviewer.Classes.BTConnectionService;
import nl.sunflare.sunflare_sensorviewer.Classes.DataProcessor;
import nl.sunflare.sunflare_sensorviewer.Classes.DeviceListActivity;
import nl.sunflare.sunflare_sensorviewer.Fragments.EngineInformationFragment;
import nl.sunflare.sunflare_sensorviewer.Fragments.MPPTGridFragment;
import nl.sunflare.sunflare_sensorviewer.Fragments.BigdataFragment;
import nl.sunflare.sunflare_sensorviewer.Fragments.MainscreenFragment;
import nl.sunflare.sunflare_sensorviewer.Fragments.PowerscreenCellsFragment;
import nl.sunflare.sunflare_sensorviewer.Fragments.ConnectionSettingsFragment;
import nl.sunflare.sunflare_sensorviewer.Interfaces.Constants;

import static nl.sunflare.sunflare_sensorviewer.Interfaces.Constants.LOCATION_PERMISSION_REQUEST;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    MainscreenFragment mainscreenGrid;
    BigdataFragment bigdataGrid;
    MPPTGridFragment mpptGrid;
    PowerscreenCellsFragment powerscreenPowercells;
    EngineInformationFragment engineInformationFragment;
    ConnectionSettingsFragment connectionSettingsFragment;

    public static DataProcessor dataProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(6);

        mViewPager.setCurrentItem(1);

        // Keep screen on.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        connectionSettingsFragment = (ConnectionSettingsFragment) mSectionsPagerAdapter.getItem(0);
        mainscreenGrid = (MainscreenFragment) mSectionsPagerAdapter.getItem(1);
        bigdataGrid = (BigdataFragment) mSectionsPagerAdapter.getItem(2);
        mpptGrid = (MPPTGridFragment) mSectionsPagerAdapter.getItem(3);
        powerscreenPowercells = (PowerscreenCellsFragment) mSectionsPagerAdapter.getItem(4);
        engineInformationFragment = (EngineInformationFragment) mSectionsPagerAdapter.getItem(5);

        dataProcessor = new DataProcessor(mainscreenGrid, bigdataGrid, mpptGrid, powerscreenPowercells, engineInformationFragment);
    }
}
