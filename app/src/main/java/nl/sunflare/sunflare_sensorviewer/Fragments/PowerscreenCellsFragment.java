package nl.sunflare.sunflare_sensorviewer.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import nl.sunflare.sunflare_sensorviewer.Adapters.PowergridMidPowerInformationViewAdapter;
import nl.sunflare.sunflare_sensorviewer.Adapters.PowergridBottomViewAdapter;
import nl.sunflare.sunflare_sensorviewer.Adapters.PowergridTopPowerInformationViewAdapter;
import nl.sunflare.sunflare_sensorviewer.R;
import nl.sunflare.sunflare_sensorviewer.ViewTypes.PowerGridCell;
import nl.sunflare.sunflare_sensorviewer.ViewTypes.PowerGridInformation;


public class PowerscreenCellsFragment extends Fragment {

    String TAG = "PowerGrid";
    ArrayList<PowerGridCell> cellList = new ArrayList<>();
    ArrayList<PowerGridInformation> powerGridTopInformationList = new ArrayList<>();
    ArrayList<PowerGridInformation> powerGridMidInformationList = new ArrayList<>();

    PowergridTopPowerInformationViewAdapter itemsAdapter1;
    PowergridMidPowerInformationViewAdapter itemsAdapter2;
    PowergridBottomViewAdapter itemsAdapter3;

    PowerGridInformation SOC;
    PowerGridInformation PWR;
    PowerGridInformation STATE;

    PowerGridInformation AVG;
    PowerGridInformation MIN;
    PowerGridInformation MAX;

    PowerGridInformation bat_in_amp;
    PowerGridInformation bat_in_wat;
    PowerGridInformation bat_out_amp;
    PowerGridInformation bat_out_wat;

    TextView bigVoltageValue;
    TextView bigVoltageType;

    public PowerscreenCellsFragment() {
        // Required empty public constructor
    }

    //TODO handle incoming message.
    public void recieveNewValue(String id, String data) {
        switch (id) {
            case "Voltage":
                bigVoltageValue.setText(data);
                bigVoltageType.setText(" V");
                break;
            case "State_Of_Charge":
                SOC.setValue(data);
                SOC.setType("%");
                break;
            case "Power_Level":
                PWR.setValue(data);
                PWR.setType("%");
                break;
            case "BMS_State":
                STATE.setValue(data);
                STATE.setType("");
                break;
            case "Cell_Temp_1":
                cellList.get(1).setPowergrid_in_value_2(data);
                break;
            case "Cell_Temp_2":
                cellList.get(2).setPowergrid_in_value_2(data);
                break;
            case "Cell_Temp_3":
                cellList.get(3).setPowergrid_in_value_2(data);
                break;
            case "Cell_Temp_4":
                cellList.get(4).setPowergrid_in_value_2(data);
                break;
            case "Cell_Temp_AVG":
                AVG.setValue(data);
                AVG.setType("°C");
                break;
            case "Cell_Temp_MAX":
                MAX.setValue(data);
                MAX.setType("°C");
                break;
            case "Cell_Temp_MIN":
                MIN.setValue(data);
                MIN.setType("°C");
                break;
            case "Current_Charge":
                bat_in_amp.setValue(data);
                bat_in_amp.setType("A");
                break;
            case "Current_Charge_Wattage":
                bat_in_wat.setValue(data);
                bat_in_wat.setType("W");
                break;
            case "Current_Discharge":
                bat_out_amp.setValue(data);
                bat_out_amp.setType("A");
                break;
            case "Current_Discharge_Wattage":
                bat_out_wat.setValue(data);
                bat_out_wat.setType("W");
                break;
        }
        itemsAdapter1.notifyDataSetChanged();
        itemsAdapter2.notifyDataSetChanged();
    }

    public void recieveNewPowercell(int cellIndex, String voltage) {
        PowerGridCell m = (PowerGridCell) itemsAdapter3.getItem(cellIndex);
        m.setPowergrid_in_value_1(voltage);
        itemsAdapter3.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        itemsAdapter1 = new PowergridTopPowerInformationViewAdapter(getActivity(), powerGridTopInformationList);
        itemsAdapter2 = new PowergridMidPowerInformationViewAdapter(getActivity(), powerGridMidInformationList);
        itemsAdapter3 = new PowergridBottomViewAdapter(getActivity(), cellList);

        GridView listView1 = (GridView) getActivity().findViewById(R.id.powerscreen_top_information_gridview);
        GridView listView2 = (GridView) getActivity().findViewById(R.id.powerscreen_mid_information_gridview);
        GridView listView3 = (GridView) getActivity().findViewById(R.id.powercell_gridview);

        listView1.setAdapter(itemsAdapter1);
        listView2.setAdapter(itemsAdapter2);
        listView3.setAdapter(itemsAdapter3);

        SOC = (PowerGridInformation) itemsAdapter2.getItem(0);
        PWR = (PowerGridInformation) itemsAdapter2.getItem(1);
        STATE = (PowerGridInformation) itemsAdapter2.getItem(2);

        AVG = (PowerGridInformation) itemsAdapter2.getItem(3);
        MIN = (PowerGridInformation) itemsAdapter2.getItem(4);
        MAX = (PowerGridInformation) itemsAdapter2.getItem(5);

        bat_in_amp = (PowerGridInformation) itemsAdapter1.getItem(0);
        bat_in_wat = (PowerGridInformation) itemsAdapter1.getItem(1);
        bat_out_amp = (PowerGridInformation) itemsAdapter1.getItem(2);
        bat_out_wat = (PowerGridInformation) itemsAdapter1.getItem(3);

        MAX = (PowerGridInformation) itemsAdapter2.getItem(5);
        MAX = (PowerGridInformation) itemsAdapter2.getItem(5);
        MAX = (PowerGridInformation) itemsAdapter2.getItem(5);

        bigVoltageValue = (TextView) getView().findViewById(R.id.big_data_value);
        bigVoltageType = (TextView) getView().findViewById(R.id.big_data_type);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    powerGridTopInformationList.add(new PowerGridInformation("IN"));
                    break;
                case 1:
                    powerGridTopInformationList.add(new PowerGridInformation("IN"));
                    break;
                case 2:
                    powerGridTopInformationList.add(new PowerGridInformation("OUT"));
                    break;
                case 3:
                    powerGridTopInformationList.add(new PowerGridInformation("OUT"));
                    break;
            }
        }

        for (int i = 0; i < 6; i++) {
            switch (i) {
                case 0:
                    powerGridMidInformationList.add(new PowerGridInformation("SOC"));
                    break;
                case 1:
                    powerGridMidInformationList.add(new PowerGridInformation("PWR"));
                    break;
                case 2:
                    powerGridMidInformationList.add(new PowerGridInformation("STATE"));
                    break;
                case 3:
                    powerGridMidInformationList.add(new PowerGridInformation("AVG"));
                    break;
                case 4:
                    powerGridMidInformationList.add(new PowerGridInformation("MIN"));
                    break;
                case 5:
                    powerGridMidInformationList.add(new PowerGridInformation("MAX"));
                    break;
            }
        }

        for (int i = 1; i < 13; i++) {
            PowerGridCell cell = new PowerGridCell("CELL" + i);
            cellList.add(cell);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_powerscreen_grid, container, false);
    }
}
