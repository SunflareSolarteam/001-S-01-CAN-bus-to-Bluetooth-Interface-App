package nl.sunflare.sunflare_sensorviewer.Fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import nl.sunflare.sunflare_sensorviewer.Adapters.BigDataViewAdapter;
import nl.sunflare.sunflare_sensorviewer.ViewTypes.BigData;
import nl.sunflare.sunflare_sensorviewer.R;

public class BigdataFragment extends Fragment {

    String TAG = "BigdataFragment";
    ArrayList<BigData> bigDataList = new ArrayList<>();
    BigDataViewAdapter itemsAdapter;

    BigData viewOne;
    BigData viewTwo;
    BigData viewThree;
    BigData viewFour;

    public BigdataFragment() {
        // Required empty public constructor
    }

    public void recieveNewValue(String id, String data) {
        switch (id) {
            case "Current_Speed":
                viewOne.setValue(data);
                viewOne.setType(" KM/H");
                break;
            case "Battery_Wattage":
                viewTwo.setValue(data);
                viewTwo.setType(" W");
                break;
            case "State_Of_Charge":
                viewThree.setValue(data);
                viewThree.setType(" %");
                break;
            case "cm":
                viewThree.setValue("?");
                viewThree.setType(" cm");
                break;
        }
        itemsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        itemsAdapter = new BigDataViewAdapter(getActivity(), bigDataList);
        GridView listView = (GridView) getActivity().findViewById(R.id.bigdata_gridview);
        listView.setAdapter(itemsAdapter);
        viewOne = (BigData) itemsAdapter.getItem(0);
        viewTwo = (BigData) itemsAdapter.getItem(1);
        viewThree = (BigData) itemsAdapter.getItem(2);
        viewFour = (BigData) itemsAdapter.getItem(3);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < 4; i++) {
            BigData bigData = new BigData();
            bigDataList.add(bigData);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bigdata_grid, container, false);
    }
}
