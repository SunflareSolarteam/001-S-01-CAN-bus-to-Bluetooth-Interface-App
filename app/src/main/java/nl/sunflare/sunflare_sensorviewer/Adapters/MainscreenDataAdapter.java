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
import nl.sunflare.sunflare_sensorviewer.ViewTypes.MainscreenTitleFourValues;
import nl.sunflare.sunflare_sensorviewer.ViewTypes.MainscreenTwoValues;

public class MainscreenDataAdapter extends BaseAdapter {

    private Activity context;
    private LayoutInflater mInflater;
    private TableLayout.LayoutParams params;
    private List<MainscreenTwoValues> mainscreenTwoValuesList;
    private List<MainscreenTitleFourValues> mainscreenTitleFourValuesList;

    public MainscreenDataAdapter(Activity context, List<MainscreenTwoValues> mainscreenTwoValuesList, List<MainscreenTitleFourValues> mainscreenTitleFourValuesList) {
        this.context = context;
        this.mainscreenTwoValuesList = mainscreenTwoValuesList;
        this.mainscreenTitleFourValuesList = mainscreenTitleFourValuesList;

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        params = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                (int) (getDeviceHeight(context) - getStatusBarHeight()) / 2-3);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position <= 2) {
            MainscreenTwoValuesViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.two_values, null);
                holder = new MainscreenTwoValuesViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (MainscreenTwoValuesViewHolder) convertView.getTag();
            }
            MainscreenTwoValues mainscreenTwoValuesView = mainscreenTwoValuesList.get(position);
            holder.value.setText(mainscreenTwoValuesView.getValue());
            holder.type.setText(mainscreenTwoValuesView.getType());
            holder.subValue.setText(mainscreenTwoValuesView.getSubValue());
            convertView.setLayoutParams(params);
            return convertView;
        } else {
            MainscreenTitleFourValuesViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.title_four_values, null);
                holder = new MainscreenTitleFourValuesViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (MainscreenTitleFourValuesViewHolder) convertView.getTag();
            }
            MainscreenTitleFourValues mainscreenTitleFourValuesView = mainscreenTitleFourValuesList.get(position - 3);
            holder.title.setText(mainscreenTitleFourValuesView.getTitle());
            holder.bigValue.setText(mainscreenTitleFourValuesView.getBigValue());
            holder.smallValueOne.setText(mainscreenTitleFourValuesView.getSmallValueOne());
            holder.smallValueTwo.setText(mainscreenTitleFourValuesView.getSmallValueTwo());
            holder.smallValueThree.setText(mainscreenTitleFourValuesView.getSmallValueThree());
            convertView.setLayoutParams(params);
            return convertView;
        }
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int position) {
        if (position <= 2) {
            return mainscreenTwoValuesList.get(position);
        } else {
            return mainscreenTitleFourValuesList.get(position - 3);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class MainscreenTwoValuesViewHolder {
        TextView value;
        TextView type;
        TextView subValue;

        MainscreenTwoValuesViewHolder(View v) {
            value = (TextView) v.findViewById(R.id.mainscreen_two_values_value);
            type = (TextView) v.findViewById(R.id.mainscreen_two_values_type);
            subValue = (TextView) v.findViewById(R.id.mainscreen_two_values_subValue);
        }
    }

    private class MainscreenTitleFourValuesViewHolder {
        TextView title;
        TextView bigValue;
        TextView smallValueOne;
        TextView smallValueTwo;
        TextView smallValueThree;

        MainscreenTitleFourValuesViewHolder(View v) {
            title = (TextView) v.findViewById(R.id.mainscreen_title_four_values_title);
            bigValue = (TextView) v.findViewById(R.id.mainscreen_title_four_values_bigvalue);
            smallValueOne = (TextView) v.findViewById(R.id.mainscreen_title_four_values_smallValueOne);
            smallValueTwo = (TextView) v.findViewById(R.id.mainscreen_title_four_values_smallValueTwo);
            smallValueThree = (TextView) v.findViewById(R.id.mainscreen_title_four_values_smallValueThree);
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
