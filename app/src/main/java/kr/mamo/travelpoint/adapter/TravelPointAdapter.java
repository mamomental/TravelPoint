package kr.mamo.travelpoint.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.db.domain.TravelPoint;

/**
 * Created by alucard on 2015-07-17.
 */
public class TravelPointAdapter extends BaseAdapter {
    ArrayList<TravelPoint> list;

    public TravelPointAdapter() {
        list = new ArrayList<TravelPoint>();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        TextView name = null;
        TravelPointHolder holder = null;

        if (null == convertView) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.travel_point_item, parent, false);

            name    = (TextView) convertView.findViewById(R.id.travel_point_item_name);

            holder = new TravelPointHolder();
            holder.name = name;
            convertView.setTag(holder);
        }
        else {
            holder  = (TravelPointHolder) convertView.getTag();
            name = holder.name;
        }
        name.setText(list.get(position).getName());

        return convertView;
    }

    public void addTravelPoint(ArrayList<TravelPoint> list) {
        this.list = list;
        notifyDataSetInvalidated();
    }

    private class TravelPointHolder {
        TextView name;
    }
}
