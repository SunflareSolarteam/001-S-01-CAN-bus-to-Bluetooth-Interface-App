package nl.sunflare.sunflare_sensorviewer.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import nl.sunflare.sunflare_sensorviewer.Fragments.EngineInformationFragment;
import nl.sunflare.sunflare_sensorviewer.Fragments.MPPTGridFragment;
import nl.sunflare.sunflare_sensorviewer.Fragments.MainscreenFragment;
import nl.sunflare.sunflare_sensorviewer.Fragments.BigdataFragment;
import nl.sunflare.sunflare_sensorviewer.Fragments.PowerscreenCellsFragment;
import nl.sunflare.sunflare_sensorviewer.Fragments.ConnectionSettingsFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private ConnectionSettingsFragment settings = new ConnectionSettingsFragment();
    private MainscreenFragment grid1 = new MainscreenFragment();
    private BigdataFragment grid2 = new BigdataFragment();
    private MPPTGridFragment grid3 = new MPPTGridFragment();
    private PowerscreenCellsFragment grid4 = new PowerscreenCellsFragment();
    private EngineInformationFragment grid5 = new EngineInformationFragment();

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return settings;
            case 1:
                return grid1;
            case 2:
                return grid2;
            case 3:
                return grid3;
            case 4:
                return grid4;
            case 5:
                return grid5;
            default:
                return grid1;
        }
    }

    @Override
    public int getCount() {
        // Amount of pages.
        return 6;
    }
}
