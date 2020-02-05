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

import nl.sunflare.sunflare_sensorviewer.R;
import nl.sunflare.sunflare_sensorviewer.ViewTypes.PowerGridCell;

public class PowergridBottomViewAdapter extends BaseAdapter {

    private Activity context;
    private LayoutInflater mInflater;
    private TableLayout.LayoutParams params;
    private List<PowerGridCell> cellList;

    public PowergridBottomViewAdapter(Activity context, List<PowerGridCell> cellList) {
        this.context = context;
        this.cellList = cellList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PowerGridCellViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.powercell, null);
            holder = new PowerGridCellViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (PowerGridCellViewHolder) convertView.getTag();
        }
        PowerGridCell cell = cellList.get(position);
        holder.title.setText(cell.getTitle());
        holder.cell_in_1.setText(cell.getPowergrid_in_value_1());
        holder.cell_in_2.setText(cell.getPowergrid_in_value_2());
        return convertView;
    }

    @Override
    public int getCount() {
        return 12;
    }

    @Override
    public Object getItem(int position) {
        return cellList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class PowerGridCellViewHolder {
        TextView title;
        TextView cell_in_1;
        TextView cell_in_2;

        PowerGridCellViewHolder(View v) {
            title = (TextView) v.findViewById(R.id.powercell_title);
            cell_in_1 = (TextView) v.findViewById(R.id.powercell_in_value1);
            cell_in_2 = (TextView) v.findViewById(R.id.powercell_in_value2);
        }
    }


    private static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.y;
    }

}