package kr.mamo.travelpoint.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.db.domain.TravelHistory;

/**
 * Created by alucard on 2015-07-17.
 */
public class TravelHistoryAdapter extends BaseAdapter {
    ArrayList<TravelHistory> list;

    public TravelHistoryAdapter() {
        list = new ArrayList<TravelHistory>();
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
        TravelHistoryHolder holder = null;

        if (null == convertView) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.travel_history_item, parent, false);

            name    = (TextView) convertView.findViewById(R.id.travel_history_item_name);

            holder = new TravelHistoryHolder();
            holder.name = name;
            convertView.setTag(holder);
        }
        else {
            holder  = (TravelHistoryHolder) convertView.getTag();
            name = holder.name;
        }
        name.setText(list.get(position).getDiary());

        return convertView;
    }

    public void addItem(TravelHistory travelPoint) {
        list.add(travelPoint);
    }

    public void addItem(ArrayList<TravelHistory> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private class TravelHistoryHolder {
        TextView name;
    }
}
