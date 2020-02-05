package nl.sunflare.sunflare_sensorviewer.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Session implements Serializable {

    // Variables need to be public for firebase.

    public String currentTime = "";
    public String currentSpeed = "";
    public float voltageHigh = 0;
    public float tempLow = 0;
    public float voltageLow = 0;
    public float tempHigh = 0;
    public float voltage = 0;
    public float current = 0;
    public float currentDischarge = 0;
    public float currentCharge = 0;
    public float state_of_charge = 0;
    public float state = 0;
    public float powerLevel = 0;
    public List<Float> cellVoltages = new ArrayList<>();
    public double latitude = 0;
    public double longitude = 0;
    public String time_to_go = "";
    public ArrayList<Float> cellTemps = new ArrayList<>();
    public float controllerOneTemp = 0;
    public float controllerTwoTemp = 0;
    public float motorOneTemp = 0;
    public float motorTwoTemp = 0;
    public float motorCurrent = 0;
    public float inputCurrent = 0;
    public float rpm = 0;

    public Session() {
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(String currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public float getVoltageHigh() {
        return voltageHigh;
    }

    public void setVoltageHigh(float voltageHigh) {
        this.voltageHigh = voltageHigh;
    }

    public float getTempLow() {
        return tempLow;
    }

    public void setTempLow(float tempLow) {
        this.tempLow = tempLow;
    }

    public float getVoltageLow() {
        return voltageLow;
    }

    public void setVoltageLow(float voltageLow) {
        this.voltageLow = voltageLow;
    }

    public float getTempHigh() {
        return tempHigh;
    }

    public void setTempHigh(float tempHigh) {
        this.tempHigh = tempHigh;
    }

    public float getVoltage() {
        return voltage;
    }

    public void setVoltage(float voltage) {
        this.voltage = voltage;
    }

    public float getCurrent() {
        return current;
    }

    public void setCurrent(float current) {
        this.current = current;
    }

    public float getCurrentDischarge() {
        return currentDischarge;
    }

    public void setCurrentDischarge(float currentDischarge) {
        this.currentDischarge = currentDischarge;
    }

    public float getCurrentCharge() {
        return currentCharge;
    }

    public void setCurrentCharge(float currentCharge) {
        this.currentCharge = currentCharge;
    }

    public float getState_of_charge() {
        return state_of_charge;
    }

    public void setState_of_charge(float state_of_charge) {
        this.state_of_charge = state_of_charge;
    }

    public float getState() {
        return state;
    }

    public void setState(float state) {
        this.state = state;
    }

    public float getPowerLevel() {
        return powerLevel;
    }

    public void setPowerLevel(float powerLevel) {
        this.powerLevel = powerLevel;
    }

    public List<Float> getCellVoltages() {
        return cellVoltages;
    }

    public void setCellVoltages(List<Float> cellVoltages) {
        this.cellVoltages = cellVoltages;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTime_to_go() {
        return time_to_go;
    }

    public void setTime_to_go(String time_to_go) {
        this.time_to_go = time_to_go;
    }

    public ArrayList<Float> getCellTemps() {
        return cellTemps;
    }

    public void setCellTemps(ArrayList<Float> cellTemps) {
        this.cellTemps = cellTemps;
    }

    public float getControllerOneTemp() {
        return controllerOneTemp;
    }

    public void setControllerOneTemp(float controllerOneTemp) {
        this.controllerOneTemp = controllerOneTemp;
    }

    public float getControllerTwoTemp() {
        return controllerTwoTemp;
    }

    public void setControllerTwoTemp(float controllerTwoTemp) {
        this.controllerTwoTemp = controllerTwoTemp;
    }

    public float getMotorOneTemp() {
        return motorOneTemp;
    }

    public void setMotorOneTemp(float motorOneTemp) {
        this.motorOneTemp = motorOneTemp;
    }

    public float getMotorTwoTemp() {
        return motorTwoTemp;
    }

    public void setMotorTwoTemp(float motorTwoTemp) {
        this.motorTwoTemp = motorTwoTemp;
    }

    public float getMotorCurrent() {
        return motorCurrent;
    }

    public void setMotorCurrent(float motorCurrent) {
        this.motorCurrent = motorCurrent;
    }

    public float getInputCurrent() {
        return inputCurrent;
    }

    public void setInputCurrent(float inputCurrent) {
        this.inputCurrent = inputCurrent;
    }

    public float getRpm() {
        return rpm;
    }

    public void setRpm(float rpm) {
        this.rpm = rpm;
    }
}
