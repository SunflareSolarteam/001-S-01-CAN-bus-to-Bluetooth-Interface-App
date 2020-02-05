package nl.sunflare.sunflare_sensorviewer.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.sunfusheng.marqueeview.MarqueeView;

import java.util.ArrayList;
import java.util.List;

import nl.sunflare.sunflare_sensorviewer.Adapters.EngineInformationDataviewAdapter;
import nl.sunflare.sunflare_sensorviewer.R;
import nl.sunflare.sunflare_sensorviewer.ViewTypes.MainscreenTitleTwoValues;


public class EngineInformationFragment extends Fragment {

    String TAG = "Engine Info Fragment";
    private ArrayList<MainscreenTitleTwoValues> mainscreenRowTwoDataList = new ArrayList<>();
    private EngineInformationDataviewAdapter itemsAdapter;

    private List<String> slsMessages = new ArrayList<>();
    private MarqueeView statusView;
    private MainscreenTitleTwoValues controller;
    private MainscreenTitleTwoValues motor;
    private MainscreenTitleTwoValues rpm;
    private MainscreenTitleTwoValues current;

    public EngineInformationFragment() {
        // Required empty public constructor
    }

    public void recieveNewValue(String id, String data) {
        switch (id) {
            case "Motor_Current":
                current.setValueOne(data);
                break;
            case "Input_Current":
                current.setValueTwo(data);
                break;
            case "Temp_Motor_One":
                motor.setValueOne(data);
                break;
            case "Temp_Motor_Two":
                motor.setValueTwo(data);
                break;
            case "Temp_Controller_One":
                controller.setValueOne(data);
                break;
            case "Temp_Controller_Two":
                controller.setValueTwo(data);
                break;
            case "RPM":
                rpm.setValueOne(data);
                break;
            case "SLS_Status":
                addStatusMessageToMarquee(data);
                break;
            case "SLS_output_limiting":
                addStatusMessageToMarquee(data);
                break;
        }
        itemsAdapter.notifyDataSetChanged();
    }

    private void addStatusMessageToMarquee(String message) {
        if (!slsMessages.contains(message)) {
            slsMessages.add(message);
            statusView.startWithList(slsMessages);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        itemsAdapter = new EngineInformationDataviewAdapter(getActivity(), mainscreenRowTwoDataList);
        GridView listView = (GridView) getActivity().findViewById(R.id.engine_information_gridview);
        listView.setAdapter(itemsAdapter);

        statusView = (MarqueeView) getActivity().findViewById(R.id.engine_status);
        controller = (MainscreenTitleTwoValues) itemsAdapter.getItem(0);
        motor = (MainscreenTitleTwoValues) itemsAdapter.getItem(1);
        rpm = (MainscreenTitleTwoValues) itemsAdapter.getItem(2);
        current = (MainscreenTitleTwoValues) itemsAdapter.getItem(3);

//        slsMessages.add("Fail safe stop");
//        slsMessages.add("2 phase PWM");
//        slsMessages.add("Loadless fault");
//        slsMessages.add("Over speed fault");
//        slsMessages.add("Offset fault");
//        slsMessages.add("Zero speed fault");
//        slsMessages.add("Phase loss fault");
//        slsMessages.add("Output limited due to minimal voltage");
//        slsMessages.add("Output cut off due to minimal voltage");
//        slsMessages.add("Switched off due to under voltage");
//        slsMessages.add("Output limited due to maximal voltage");
//        slsMessages.add("Output cut off due to maximal voltage");
//        slsMessages.add("Switched off due to over voltage");
//        slsMessages.add("Output limited due to maximal temperature");
//        slsMessages.add("Output cut off due to maximal temperature");
//        slsMessages.add("Switched off due to over temperature")

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < 4; i++) {
            MainscreenTitleTwoValues titleFourValuesView;
            if (i == 0) {
                titleFourValuesView = new MainscreenTitleTwoValues("CONTROLLER");
            } else if (i == 1) {
                titleFourValuesView = new MainscreenTitleTwoValues("MOTOR");
            } else if (i == 2) {
                titleFourValuesView = new MainscreenTitleTwoValues("RPM");
            } else {
                titleFourValuesView = new MainscreenTitleTwoValues("CURRENT");
            }
            mainscreenRowTwoDataList.add(titleFourValuesView);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_engine_information_grid, container, false);
    }
}
