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
import nl.sunflare.sunflare_sensorviewer.ViewTypes.BigData;

public class BigDataViewAdapter extends BaseAdapter {
    private Activity context;
    private LayoutInflater mInflater;
    private TableLayout.LayoutParams params;
    private List<BigData> bigDataList;

    public BigDataViewAdapter(Activity context, List<BigData> bigDataList) {
        this.context = context;
        this.bigDataList = bigDataList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        params = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                (int) (getDeviceHeight(context) - getStatusBarHeight()) / 2 -3);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.big_data_view, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BigData bigView = bigDataList.get(position);
        holder.value.setText(bigView.getValue());
        holder.type.setText(bigView.getType());
        convertView.setLayoutParams(params);
        return convertView;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return bigDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView value;
        TextView type;

        ViewHolder(View v) {
            value = (TextView) v.findViewById(R.id.big_data_value);
            type = (TextView) v.findViewById(R.id.big_data_type);
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