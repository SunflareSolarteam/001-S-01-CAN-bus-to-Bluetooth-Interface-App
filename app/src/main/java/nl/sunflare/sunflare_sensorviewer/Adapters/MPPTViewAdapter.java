package nl.sunflare.sunflare_sensorviewer.Adapters;

import android.annotation.SuppressLint;
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

import nl.sunflare.sunflare_sensorviewer.R;
import nl.sunflare.sunflare_sensorviewer.ViewTypes.MPPT;

public class MPPTViewAdapter extends BaseAdapter {

    private Activity context;
    private LayoutInflater mInflater;
    private TableLayout.LayoutParams params;
    private List<MPPT> mpptList;

    public MPPTViewAdapter(Activity context, List<MPPT> mpptList) {
        this.context = context;
        this.mpptList = mpptList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        params = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                (int) (getDeviceHeight(context) - getStatusBarHeight()) / 2-3);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.mppt, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MPPT mppt = mpptList.get(position);
        holder.title.setText(mppt.getTitle());
        holder.mppt_in_1.setText(mppt.getMppt_in_value_1());
        holder.mppt_in_2.setText(mppt.getMppt_in_value_2());
        holder.mppt_in_3.setText(mppt.getMppt_in_value_3());
        holder.mppt_out.setText(mppt.getMppt_out_value());
        holder.mppt_temp.setText(mppt.getMppt_temp_value());
        convertView.setLayoutParams(params);
        return convertView;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return mpptList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView title;
        TextView mppt_in_1;
        TextView mppt_in_2;
        TextView mppt_in_3;
        TextView mppt_out;
        TextView mppt_temp;

        ViewHolder(View v) {
            title = (TextView) v.findViewById(R.id.mppt_title);
            mppt_in_1 = (TextView) v.findViewById(R.id.mppt_in_value1);
            mppt_in_2 = (TextView) v.findViewById(R.id.mppt_in_value2);
            mppt_in_3 = (TextView) v.findViewById(R.id.mppt_in_value3);
            mppt_out = (TextView) v.findViewById(R.id.mppt_out_value);
            mppt_temp = (TextView) v.findViewById(R.id.mppt_temp_value);
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