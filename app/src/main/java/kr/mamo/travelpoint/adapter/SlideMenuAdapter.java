package kr.mamo.travelpoint.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.model.SlideMenuItem;

/**
 * Created by alucard on 2015-07-21.
 */
public class SlideMenuAdapter extends BaseAdapter {
    ArrayList<SlideMenuItem> list;

    public SlideMenuAdapter() {
        list = new ArrayList<SlideMenuItem>();
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
        SlideMenuHolder holder = null;

        if (null == convertView) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.slide_menu_item, parent, false);

            name    = (TextView) convertView.findViewById(R.id.slide_menu_item_name);

            holder = new SlideMenuHolder();
            holder.name = name;
            convertView.setTag(holder);
        }
        else {
            holder  = (SlideMenuHolder) convertView.getTag();
            name = holder.name;
        }
        name.setText(list.get(position).getTitle());

        return convertView;
    }

    public void addMenu(SlideMenuItem item) {
        list.add(item);
    }

    private class SlideMenuHolder {
        TextView name;
    }
}
