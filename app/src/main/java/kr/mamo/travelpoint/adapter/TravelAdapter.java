package kr.mamo.travelpoint.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.db.domain.Travel;

/**
 * Created by alucard on 2015-07-17.
 */
public class TravelAdapter extends BaseAdapter {
    ArrayList<Travel> list;

    public TravelAdapter() {
        list = new ArrayList<Travel>();
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
        TextView description = null;
        TravelHolder holder = null;

        if (null == convertView) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.travel_item, parent, false);

            name    = (TextView) convertView.findViewById(R.id.travel_item_name);
            description    = (TextView) convertView.findViewById(R.id.travel_item_description);

            holder = new TravelHolder();
            holder.name = name;
            holder.description = description;
            convertView.setTag(holder);
        }
        else {
            holder  = (TravelHolder) convertView.getTag();
            name = holder.name;
            description = holder.description;
        }

        name.setText(list.get(position).getName());
        description.setText(list.get(position).getDescription());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 터치 시 해당 아이템 이름 출력
                Toast.makeText(context, "리스트 클릭 : "+list.get(pos).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        // 리스트 아이템을 길게 터치 했을 떄 이벤트 발생
        convertView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // 터치 시 해당 아이템 이름 출력
                Toast.makeText(context, "리스트 롱 클릭 : "+list.get(pos).getName(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return convertView;
    }

    public void addTravel(Travel travel) {
        list.add(travel);
    }

    private class TravelHolder {
        TextView name;
        TextView description;
    }
}
