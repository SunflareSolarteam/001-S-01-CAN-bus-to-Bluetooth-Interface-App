package nl.sunflare.sunflare_sensorviewer.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.List;

import nl.sunflare.sunflare_sensorviewer.Fragments.BigdataFragment;
import nl.sunflare.sunflare_sensorviewer.R;
import nl.sunflare.sunflare_sensorviewer.ViewTypes.BigData;
import nl.sunflare.sunflare_sensorviewer.ViewTypes.PowerGridInformation;

public class PowergridTopPowerInformationViewAdapter extends BaseAdapter {

    private Activity context;
    private LayoutInflater mInflater;
    private TableLayout.LayoutParams params;
    private List<PowerGridInformation> informationViewList;

    public PowergridTopPowerInformationViewAdapter(Activity context, List<PowerGridInformation> informationViewList) {
        this.context = context;
        this.informationViewList = informationViewList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        params = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                (int) (ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PowerGridInformationViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.powergrid_information_big, null);
            holder = new PowerGridInformationViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (PowerGridInformationViewHolder) convertView.getTag();
        }
        PowerGridInformation view = informationViewList.get(position);
        holder.value.setText(view.getValue());
        holder.type.setText(view.getType());
        holder.label.setText(view.getLabel());
        convertView.setLayoutParams(params);
        return convertView;

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return informationViewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class PowerGridInformationViewHolder {
        TextView label;
        TextView value;
        TextView type;

        PowerGridInformationViewHolder(View v) {
            value = (TextView) v.findViewById(R.id.powergrid_information_value);
            type = (TextView) v.findViewById(R.id.powergrid_information_type);
            label = (TextView) v.findViewById(R.id.powergrid_information_label);
        }
    }

    private static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.y;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}