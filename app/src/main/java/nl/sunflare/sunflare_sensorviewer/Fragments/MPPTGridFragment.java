package nl.sunflare.sunflare_sensorviewer.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import java.util.ArrayList;
import nl.sunflare.sunflare_sensorviewer.Adapters.MPPTViewAdapter;
import nl.sunflare.sunflare_sensorviewer.ViewTypes.MPPT;
import nl.sunflare.sunflare_sensorviewer.R;

public class MPPTGridFragment extends Fragment {

    String TAG = "MPPTGrid";
    ArrayList<MPPT> mpptList = new ArrayList<>();
    MPPTViewAdapter itemsAdapter;

    public MPPTGridFragment() {
        // Required empty public constructor
    }

    //TODO handle incoming message.
    public void recieveNewValue(String id, String dataOne, String dataTwo) {
        String index = id.substring(0, 2);
        int mpptIndex = Integer.parseInt(id.substring(2, 3), 16) - 4;
        MPPT m = (MPPT) itemsAdapter.getItem(mpptIndex);
        switch (index) {
            case "28":
                m.setMppt_in_value_3(dataTwo);
                m.setMppt_out_value(dataOne);
                break;
            case "18":
                m.setMppt_in_value_1(dataTwo);
                m.setMppt_in_value_2(dataOne);
                break;
            case "48":
                m.setMppt_temp_value(dataTwo);
                break;
        }
        itemsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 1; i < 11; i++) {
            MPPT mppt = new MPPT("MPPT " + i);
            mpptList.add(mppt);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        itemsAdapter = new MPPTViewAdapter(getActivity(), mpptList);
        GridView listView = (GridView) getActivity().findViewById(R.id.mppt_gridview);
        listView.setAdapter(itemsAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mppt_grid, container, false);
    }
}
