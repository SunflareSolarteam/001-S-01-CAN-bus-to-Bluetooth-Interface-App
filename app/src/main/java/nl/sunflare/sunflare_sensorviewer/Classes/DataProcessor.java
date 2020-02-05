package nl.sunflare.sunflare_sensorviewer.Classes;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import nl.sunflare.sunflare_sensorviewer.Fragments.BigdataFragment;
import nl.sunflare.sunflare_sensorviewer.Fragments.EngineInformationFragment;
import nl.sunflare.sunflare_sensorviewer.Fragments.MPPTGridFragment;
import nl.sunflare.sunflare_sensorviewer.Fragments.MainscreenFragment;
import nl.sunflare.sunflare_sensorviewer.Fragments.PowerscreenCellsFragment;
import nl.sunflare.sunflare_sensorviewer.Interfaces.IBaseGpsListener;

import static android.os.Looper.getMainLooper;

public class DataProcessor implements IBaseGpsListener {

    private String TAG = "DataProcessor";

    private MainscreenFragment mainscreenGrid;
    private BigdataFragment bigdataGrid;
    private MPPTGridFragment mpptGrid;
    private PowerscreenCellsFragment powerscreenPowercells;
    private EngineInformationFragment engineInformationGrid;

    public static Session currentSession = new Session();
    private Databaselogger dbLogger = new Databaselogger();

    private DecimalFormat noDecimal = new DecimalFormat("0");
    private DecimalFormat oneDecimal = new DecimalFormat("0.0");
    private DecimalFormat twoDecimal = new DecimalFormat("0.00");

    private Float[] cellVoltages = new Float[12];

    public DataProcessor(MainscreenFragment mainscreenGrid, BigdataFragment bigdataGrid, MPPTGridFragment mpptGrid, PowerscreenCellsFragment powerscreenPowercells, EngineInformationFragment engineInformationGrid) {
        this.mainscreenGrid = mainscreenGrid;
        this.bigdataGrid = bigdataGrid;
        this.mpptGrid = mpptGrid;
        this.powerscreenPowercells = powerscreenPowercells;
        this.engineInformationGrid = engineInformationGrid;
    }

    public void processMessage(String in) {
        boolean sorted = false;
        String[] mpptOneIds = {"284", "285", "286", "287", "288", "289", "28A", "28B", "28C", "28D"};
        String[] mpptTwoIds = {"184", "185", "186", "187", "188", "189", "18A", "18B", "18C", "18D"};
        String[] mpptTemps = {"484", "485", "486", "487", "488", "489", "48A", "48B", "48C", "48D"};
        try {
            String[] msg = DataProcessorUtils.splitMessageData(in);
            if (msg != null) {
                String inID = msg[0];
                String inData = msg[1];
                if (Arrays.asList(mpptOneIds).contains(msg[0]) || Arrays.asList(mpptTwoIds).contains(inID) || Arrays.asList(mpptTemps).contains(msg[0])) {
                    sorted = true;
                    handleMppt(inID, inData);
                }

                if (inID.equals("302")) {
                    sorted = true;
                    handle302(inData);
                }

                if (inID.equals("402")) {
                    sorted = true;
                    handle402(inData);
                }

                if (inID.equals("482")) {
                    sorted = true;
                    handle482(inData);
                }

                if (inID.equals("202")) {
                    sorted = true;
                    handle202(inData);
                }

                if (inID.equals("190")) {
                    sorted = true;
                    handle190(inData);
                }

                if (inID.equals("290")) {
                    sorted = true;
                    handle290(inData);
                }

                if (inID.equals("390")) {
                    sorted = true;
                    handle390(inData);
                }

                if (inID.equals("490")) {
                    sorted = true;
                    handle490(inData);
                }

                if (!sorted) {
                    Log.d(TAG, "Could not sort message: " + Arrays.toString(msg));
                }
            }
        } catch (Exception e) {
            System.out.println("Error " + e);
        }
    }

    private void handleMppt(String id, String hex) throws IndexOutOfBoundsException {
        String dataOne = hex.substring(0, 8);
        String dataTwo = hex.substring(8, 16);
        String index = id.substring(0, 2);
        if (index.equals("28")) {
            dataOne = oneDecimal.format(DataProcessorUtils.convertMpptHexToValue(dataOne, false)) + " V";
            dataTwo = oneDecimal.format(DataProcessorUtils.convertMpptHexToValue(dataTwo, true)) + " W";
        } else if (index.equals("18")) {
            dataOne = oneDecimal.format(DataProcessorUtils.convertMpptHexToValue(dataOne, true)) + " A";
            dataTwo = oneDecimal.format(DataProcessorUtils.convertMpptHexToValue(dataTwo, false)) + " V";
        } else if (index.equals("48")) {
            dataOne = oneDecimal.format(DataProcessorUtils.convertMpptHexToValue(dataOne, true)) + " °C";
            dataTwo = oneDecimal.format(DataProcessorUtils.convertMpptHexToValue(dataTwo, false)) + " °C";
        }
        mpptGrid.recieveNewValue(id, dataOne, dataTwo);
    }

    private void handle190(String hex) throws IndexOutOfBoundsException {
        String subIndex = hex.substring(6, 8);
        String bit = hex.substring(8, 12);
        switch (subIndex) {
            case "00":
                engineInformationGrid.recieveNewValue("SLS_Status", DataProcessorUtils.getSlsStatusMessage(Integer.parseInt(bit)));
                break;
            case "01":
                engineInformationGrid.recieveNewValue("SLS_output_limiting", DataProcessorUtils.getSlsStatusMessage(Integer.parseInt(bit)));
                break;
        }
    }

    private void handle202(String hex) throws IndexOutOfBoundsException {
        currentSession.setPowerLevel(DataProcessorUtils.convertHexToValue(hex.substring(0, 2), 1, true, false));
        powerscreenPowercells.recieveNewValue("Power_Level", noDecimal.format(currentSession.getPowerLevel()));
    }

    private void handle290(String hex) throws IndexOutOfBoundsException {
        String index = hex.substring(2, 4);
        String subIndex = hex.substring(6, 8);
        switch (index) {
            case "00":
                switch (subIndex) {
                    case "01":
                        currentSession.setControllerOneTemp(DataProcessorUtils.convertHexToValue(hex.substring(8, 16), 10, true, true));
                        engineInformationGrid.recieveNewValue("Temp_Controller_One", String.format("%s °C", noDecimal.format(currentSession.getControllerOneTemp())));
                        break;
                    case "02":
                        currentSession.setControllerTwoTemp(DataProcessorUtils.convertHexToValue(hex.substring(8, 16), 10, true, true));
                        engineInformationGrid.recieveNewValue("Temp_Controller_Two", String.format("%s °C", noDecimal.format(currentSession.getControllerTwoTemp())));
                        break;
                }
                break;
            case "01":
                switch (subIndex) {
                    case "01":
                        currentSession.setMotorOneTemp(DataProcessorUtils.convertHexToValue(hex.substring(8, 16), 10, true, true));
                        engineInformationGrid.recieveNewValue("Temp_Motor_One", String.format("%s °C", noDecimal.format(currentSession.getMotorOneTemp())));
                        mainscreenGrid.recieveNewValue("Temp_Motor_One", String.format("%s °C", noDecimal.format(currentSession.getMotorOneTemp())));
                        break;
                    case "02":
                        currentSession.setMotorTwoTemp(DataProcessorUtils.convertHexToValue(hex.substring(8, 16), 10, true, true));
                        engineInformationGrid.recieveNewValue("Temp_Motor_Two", String.format("%s °C", noDecimal.format(currentSession.getMotorTwoTemp())));
                        break;
                }
                break;
        }
    }

    private void handle302(String hex) throws IndexOutOfBoundsException {
        String subIndex = hex.substring(6, 8);
        switch (subIndex) {
            case "01":
                // Voltage
                currentSession.setVoltage(DataProcessorUtils.convertHexToValue(hex.substring(8, 12), 1000, true, false));
                mainscreenGrid.recieveNewValue("Voltage", String.format("%s V", oneDecimal.format(currentSession.getVoltage())));
                powerscreenPowercells.recieveNewValue("Voltage", oneDecimal.format(currentSession.getVoltage()));
                break;
            case "02":
                // Current
                currentSession.setCurrent(DataProcessorUtils.convertHexToValue(hex.substring(8, 12), 100, true, true));
                mainscreenGrid.recieveNewValue("Battery_Wattage", noDecimal.format(currentSession.getCurrent() * currentSession.getVoltage()));
                bigdataGrid.recieveNewValue("Battery_Wattage", noDecimal.format(currentSession.getCurrent() * currentSession.getVoltage()));
                powerscreenPowercells.recieveNewValue("Battery_Wattage", noDecimal.format(currentSession.getCurrent() * currentSession.getVoltage()));
                break;
            case "03":
                // Current discharge
                currentSession.setCurrentCharge(DataProcessorUtils.convertHexToValue(hex.substring(8, 12), 100, true, true));
                mainscreenGrid.recieveNewValue("Current_Charge", String.format("%s A", oneDecimal.format(currentSession.getCurrentCharge())));
                mainscreenGrid.recieveNewValue("Current_Charge_Wattage", String.format("%s W", noDecimal.format(currentSession.getCurrentCharge() * currentSession.getVoltage())));
                powerscreenPowercells.recieveNewValue("Current_Charge", oneDecimal.format(currentSession.getCurrentCharge()));
                powerscreenPowercells.recieveNewValue("Current_Charge_Wattage", noDecimal.format(currentSession.getCurrentCharge() * currentSession.getVoltage()));
                break;
            case "04":
                // Current charge
                currentSession.setCurrentDischarge(DataProcessorUtils.convertHexToValue(hex.substring(8, 12), 100, true, true));
                mainscreenGrid.recieveNewValue("Current_Discharge", String.format("%s A", oneDecimal.format(currentSession.getCurrentDischarge())));
                mainscreenGrid.recieveNewValue("Current_Discharge_Wattage", String.format("%s W", noDecimal.format(currentSession.getCurrentDischarge() * currentSession.getVoltage())));
                powerscreenPowercells.recieveNewValue("Current_Discharge", oneDecimal.format(currentSession.getCurrentDischarge()));
                powerscreenPowercells.recieveNewValue("Current_Discharge_Wattage", noDecimal.format(currentSession.getCurrentDischarge() * currentSession.getVoltage()));
                break;
            case "05":
                // State of charge
                currentSession.setState_of_charge(DataProcessorUtils.convertHexToValue(hex.substring(8, 10), 1, false, false));
                mainscreenGrid.recieveNewValue("State_Of_Charge", String.format("%s", noDecimal.format(currentSession.getState_of_charge())));
                bigdataGrid.recieveNewValue("State_Of_Charge", String.format("%s", noDecimal.format(currentSession.getState_of_charge())));
                powerscreenPowercells.recieveNewValue("State_Of_Charge", String.format("%s", noDecimal.format(currentSession.getState_of_charge())));
                break;
            case "07":
                // Time to go
                int time_to_go_minutes = (int) DataProcessorUtils.convertHexToValue(hex.substring(8, 10), 1, false, false);
                int hours = time_to_go_minutes / 60;
                int minutes = time_to_go_minutes % 60;
                currentSession.setTime_to_go(String.format("%d:%02d:00", hours, minutes));
                mainscreenGrid.recieveNewValue("Time_To_Go", currentSession.getTime_to_go());
                break;
        }
    }

    private void handle390(String hex) throws IndexOutOfBoundsException {
        String index = hex.substring(2, 4);
        String subIndex = hex.substring(6, 8);
        switch (index) {
            case "01":
                switch (subIndex) {
                    case "01":
                        currentSession.setMotorCurrent(DataProcessorUtils.convertHexToValue(hex.substring(8, 12), 10, true, true));
                        engineInformationGrid.recieveNewValue("Motor_Current", String.format("%s A", noDecimal.format(currentSession.getMotorCurrent())));
                        mainscreenGrid.recieveNewValue("Motor_Current", String.format("%s A", noDecimal.format(currentSession.getMotorCurrent())));
                        break;
                    case "02":
                        break;
                    case "03":
                        break;
                }
                break;
            case "02":
                switch (subIndex) {
                    case "01":
                        currentSession.setInputCurrent(DataProcessorUtils.convertHexToValue(hex.substring(8, 12), 10, true, true));
                        engineInformationGrid.recieveNewValue("Input_Current", String.format("%s A", noDecimal.format(currentSession.getInputCurrent())));
                        break;
                    case "02":
                        break;
                    case "03":
                        break;
                }
                break;
            case "03":
                switch (subIndex) {
                    case "01":
                        currentSession.setRpm(DataProcessorUtils.convertHexToValue(hex.substring(8, 12), 1, true, true));
                        engineInformationGrid.recieveNewValue("RPM", noDecimal.format(currentSession.getRpm()));
                        mainscreenGrid.recieveNewValue("RPM", noDecimal.format(currentSession.getRpm()));
                        break;
                    case "02":
                        break;
                    case "03":
                        break;
                }
                break;
        }
    }

    private void handle402(String hex) throws IndexOutOfBoundsException {
        String subIndex = hex.substring(6, 8);
        switch (subIndex) {
            case "09":
                currentSession.setTempHigh(DataProcessorUtils.convertHexToValue(hex.substring(8, 12), 1, true, false));
                mainscreenGrid.recieveNewValue("Cell_Temperature_High",
                        String.format("%s °C - %s °C", oneDecimal.format(currentSession.getTempHigh()), oneDecimal.format(currentSession.getTempLow())));
                break;
            case "0A":
                // Empty ?
                break;
            case "0B":
                currentSession.setTempLow(DataProcessorUtils.convertHexToValue(hex.substring(8, 12), 1, true, false));
                mainscreenGrid.recieveNewValue("Cell_Temperature_Low",
                        String.format("%s °C - %s °C", oneDecimal.format(currentSession.getTempHigh()), oneDecimal.format(currentSession.getTempLow())));
                break;
            case "0C":
                currentSession.setVoltageHigh(DataProcessorUtils.convertHexToValue(hex.substring(8, 12), 1000, true, false));
                mainscreenGrid.recieveNewValue("Cell_Voltage_High",
                        String.format("%s V - %s V", oneDecimal.format(currentSession.getVoltageHigh()), oneDecimal.format(currentSession.getVoltageLow())));
                break;
            case "0D":
                currentSession.setVoltageLow(DataProcessorUtils.convertHexToValue(hex.substring(8, 12), 1000, true, false));
                mainscreenGrid.recieveNewValue("Cell_Voltage_Low",
                        String.format("%s V - %s V", oneDecimal.format(currentSession.getVoltageHigh()), oneDecimal.format(currentSession.getVoltageLow())));
                break;
            case "0E":
                currentSession.setState(DataProcessorUtils.convertHexToValue(hex.substring(8, 12), 1, false, false));
                powerscreenPowercells.recieveNewValue("BMS_State", noDecimal.format(currentSession.getState()));
                break;
            case "0F":
                ArrayList<Float> cellTemps = new ArrayList<>();
                float celltemp1 = DataProcessorUtils.convertHexToValue(hex.substring(8, 10), 1, true, false);
                float celltemp2 = DataProcessorUtils.convertHexToValue(hex.substring(10, 12), 1, true, false);
                float celltemp3 = DataProcessorUtils.convertHexToValue(hex.substring(12, 14), 1, true, false);
                float celltemp4 = DataProcessorUtils.convertHexToValue(hex.substring(14, 16), 1, true, false);
                cellTemps.add(celltemp1);
                cellTemps.add(celltemp2);
                cellTemps.add(celltemp3);
                cellTemps.add(celltemp4);
                currentSession.setCellTemps(cellTemps);
                for (int i = 0; i < cellTemps.size(); i++) {
                    powerscreenPowercells.recieveNewValue(("Cell_Temp_" + (i + 1)), String.format("%s °C", noDecimal.format(cellTemps.get(i))));
                }
                handleTempCollection();
                break;
        }
    }

    private void handle482(String hex) throws IndexOutOfBoundsException {
        int cellIndex = Integer.parseInt(hex.substring(7, 8), 16) - 1;
        float cellVoltage = DataProcessorUtils.convertHexToValue(hex.substring(8, 16), 1000, true, false);
        cellVoltages[cellIndex] = cellVoltage;
        currentSession.setCellVoltages(Arrays.asList(cellVoltages));
        if (cellIndex > 12) {
            System.out.println("Tried to fill an invalid cell index!");
        } else {
            powerscreenPowercells.recieveNewPowercell(cellIndex, String.format("%s V", twoDecimal.format(cellVoltage)));
        }
    }

    private void handle490(String hex) throws IndexOutOfBoundsException {
        String index = hex.substring(2, 4);
        String subIndex = hex.substring(6, 8);
        switch (index) {
            case "01":
                break;
            case "02":
                break;
            case "03":
                switch (subIndex) {
                    case "01":
                        break;
                    case "02":
                        engineInformationGrid.recieveNewValue("placeholder", null);
                        break;
                    case "03":
                        break;
                }
                break;
        }
    }

    private void handleTempCollection() {
        float temp = 0;
        for (Float f : currentSession.getCellTemps()) {
            temp += f;
        }
        powerscreenPowercells.recieveNewValue(("Cell_Temp_AVG"), noDecimal.format(temp / currentSession.getCellTemps().size()));
        powerscreenPowercells.recieveNewValue(("Cell_Temp_MIN"), noDecimal.format(Collections.min(currentSession.getCellTemps())));
        powerscreenPowercells.recieveNewValue(("Cell_Temp_MAX"), noDecimal.format(Collections.max(currentSession.getCellTemps())));
    }

    private void updateSpeed(CLocation location) {
        float nCurrentSpeed = 0;

        if (location != null) {
            nCurrentSpeed = location.getSpeed();
        }

        Formatter fmt = new Formatter(new StringBuilder());
        fmt.format("%.1f", nCurrentSpeed);
        String strCurrentSpeed = fmt.toString();
        strCurrentSpeed = strCurrentSpeed.replace(' ', '0');
        String[] data = new String[]{"Current_Speed", strCurrentSpeed};
        currentSession.setCurrentSpeed(strCurrentSpeed);
        if (mainscreenGrid != null) {
            mainscreenGrid.recieveNewValue(data[0], currentSession.getCurrentSpeed());
            bigdataGrid.recieveNewValue(data[0], currentSession.getCurrentSpeed());
        }
    }

    public void switchDatabaseLogger(boolean enabled) {
        if (enabled) {
            dbLogger.startUpdates();
        } else {
            dbLogger.stopUpdates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            CLocation myLocation = new CLocation(location);
            currentSession.setLatitude(myLocation.getLatitude());
            currentSession.setLongitude(myLocation.getLongitude());
            updateSpeed(myLocation);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onGpsStatusChanged(int event) {

    }
}