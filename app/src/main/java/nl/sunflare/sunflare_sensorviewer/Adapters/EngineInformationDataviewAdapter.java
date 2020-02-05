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
import nl.sunflare.sunflare_sensorviewer.ViewTypes.MainscreenTitleTwoValues;

public class EngineInformationDataviewAdapter extends BaseAdapter {
    private Activity context;
    private LayoutInflater mInflater;
    private TableLayout.LayoutParams params;
    private List<MainscreenTitleTwoValues> mainscreenTitleTwoValuesList;

    public EngineInformationDataviewAdapter(Activity context, List<MainscreenTitleTwoValues> mainscreenTitleTwoValuesList) {
        this.context = context;
        this.mainscreenTitleTwoValuesList = mainscreenTitleTwoValuesList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        params = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                (int) (getDeviceHeight(context) - getStatusBarHeight()) / 2 - 2);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EngineInformationDataviewAdapter.EngineInformationTitleFourValuesViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.title_two_values, null);
            holder = new EngineInformationDataviewAdapter.EngineInformationTitleFourValuesViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (EngineInformationDataviewAdapter.EngineInformationTitleFourValuesViewHolder) convertView.getTag();
        }
        MainscreenTitleTwoValues engineView = mainscreenTitleTwoValuesList.get(position);
        holder.title.setText(engineView.getTitle());
        holder.valueOne.setText(engineView.getValueOne());
        holder.valueTwo.setText(engineView.getValueTwo());
        convertView.setLayoutParams(params);
        return convertView;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return mainscreenTitleTwoValuesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class EngineInformationTitleFourValuesViewHolder {
        TextView title;
        TextView valueOne;
        TextView valueTwo;


        EngineInformationTitleFourValuesViewHolder(View v) {
            title = (TextView) v.findViewById(R.id.engine_title_two_values_title);
            valueOne = (TextView) v.findViewById(R.id.engine_title_two_values_value_one);
            valueTwo = (TextView) v.findViewById(R.id.engine_title_two_values_value_two);
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
