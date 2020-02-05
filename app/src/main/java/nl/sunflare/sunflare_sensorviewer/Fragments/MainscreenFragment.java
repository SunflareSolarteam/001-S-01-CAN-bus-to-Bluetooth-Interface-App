package nl.sunflare.sunflare_sensorviewer.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import nl.sunflare.sunflare_sensorviewer.Adapters.MainscreenDataAdapter;
import nl.sunflare.sunflare_sensorviewer.R;
import nl.sunflare.sunflare_sensorviewer.ViewTypes.MainscreenTitleFourValues;
import nl.sunflare.sunflare_sensorviewer.ViewTypes.MainscreenTwoValues;

import static android.os.Looper.getMainLooper;
import static java.lang.Math.abs;


public class MainscreenFragment extends Fragment {

    String TAG = "MainscreenFragment";

    private ArrayList<MainscreenTwoValues> mainscreenRowOneDataList = new ArrayList<>();
    private ArrayList<MainscreenTitleFourValues> mainscreenRowTwoDataList = new ArrayList<>();
    private MainscreenDataAdapter itemsAdapter;

    private MainscreenTwoValues speedView;
    private MainscreenTwoValues stateOfChargeView;
    private MainscreenTwoValues batteryStateView;

    private MainscreenTitleFourValues chargeView;
    private MainscreenTitleFourValues batteryView;
    private MainscreenTitleFourValues dischargeView;

    public MainscreenFragment() {
        // Required empty public constructor
    }

    public void recieveNewValue(String id, String data) {
        switch (id) {
            case "Current_Speed":
                speedView.setValue(data);
                speedView.setType("KM/H");
                break;
            case "Voltage":
                batteryStateView.setSubValue(data);
                break;
            case "Battery_Wattage":
                // Current
                batteryView.setBigValue(String.format("%s W", data));
                batteryStateView.setValue(data);
                batteryStateView.setType("W");
                break;
            case "Current_Discharge":
                batteryView.setSmallValueOne(data);
                break;
            case "Current_Charge":
                chargeView.setSmallValueOne(data);
                break;
            case "Current_Charge_Wattage":
                chargeView.setBigValue(data);
                break;
            case "Current_Discharge_Wattage":
                // Current discharge
                dischargeView.setBigValue(data);
                break;
            case "State_Of_Charge":
                stateOfChargeView.setValue(data);
                stateOfChargeView.setType("%");
                break;
            case "Time_To_Go":
                stateOfChargeView.setSubValue(data);
                break;
            case "Cell_Temperature_High":
                batteryView.setSmallValueThree(data);
                break;
            case "Cell_Temperature_Low":
                batteryView.setSmallValueThree(data);
                break;
            case "Cell_Voltage_High":
                batteryView.setSmallValueTwo(data);
                break;
            case "Cell_Voltage_Low":
                batteryView.setSmallValueTwo(data);
                break;
            case "BMS_State":
                break;
            case "Temperature_Collection":
                break;
            case "Temp_Motor_One":
                dischargeView.setSmallValueOne(data);
                break;
            case "RPM":
                dischargeView.setSmallValueTwo(data);
                break;
            case "Motor_Current":
                dischargeView.setSmallValueThree(data);
                break;
        }
        itemsAdapter.notifyDataSetChanged();
    }

    private void startCurrentTimeView() {
        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                speedView.setSubValue(new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(new Date()));
                someHandler.postDelayed(this, 1000);
                itemsAdapter.notifyDataSetChanged();
            }
        }, 1000);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        itemsAdapter = new MainscreenDataAdapter(getActivity(), mainscreenRowOneDataList, mainscreenRowTwoDataList);
        GridView listView = (GridView) getActivity().findViewById(R.id.mainscreen_gridview);
        listView.setAdapter(itemsAdapter);

        speedView = (MainscreenTwoValues) itemsAdapter.getItem(0);
        stateOfChargeView = (MainscreenTwoValues) itemsAdapter.getItem(1);
        batteryStateView = (MainscreenTwoValues) itemsAdapter.getItem(2);
        chargeView = (MainscreenTitleFourValues) itemsAdapter.getItem(3);
        batteryView = (MainscreenTitleFourValues) itemsAdapter.getItem(4);
        dischargeView = (MainscreenTitleFourValues) itemsAdapter.getItem(5);

        startCurrentTimeView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < 3; i++) {
            MainscreenTwoValues twoValuesView = new MainscreenTwoValues();
            mainscreenRowOneDataList.add(twoValuesView);
        }

        for (int i = 0; i < 3; i++) {
            MainscreenTitleFourValues titleFourValuesView;
            if (i == 0) {
                titleFourValuesView = new MainscreenTitleFourValues("CHARGE");
            } else if (i == 1) {
                titleFourValuesView = new MainscreenTitleFourValues("BATTERY");
            } else {
                titleFourValuesView = new MainscreenTitleFourValues("DISCHARGE");
            }
            mainscreenRowTwoDataList.add(titleFourValuesView);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mainscreen_grid, container, false);
    }
}
