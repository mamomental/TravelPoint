package kr.mamo.travelpoint.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.constant.Constants;
import kr.mamo.travelpoint.db.TP;
import kr.mamo.travelpoint.db.domain.TravelHistory;
import kr.mamo.travelpoint.db.domain.TravelPoint;
import kr.mamo.travelpoint.util.GPSTracker;

/**
 * Created by alucard on 2015-07-17.
 */
public class TravelHistoryAdapter extends BaseAdapter {
    ArrayList<TravelHistory> list;
    TravelPoint travelPoint;

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
        final TravelHistory data = list.get(pos);
        final Context context = parent.getContext();

        SimpleDraweeView image = null;
        TextView name = null;
        TravelHistoryHolder holder = null;

        if (null == convertView) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.travel_history_item, parent, false);

            image = (SimpleDraweeView) convertView.findViewById(R.id.travel_history_item_image);
            name    = (TextView) convertView.findViewById(R.id.travel_history_item_name);

            holder = new TravelHistoryHolder();
            holder.image = image;
            holder.name = name;
            convertView.setTag(holder);
        }
        else {
            holder  = (TravelHistoryHolder) convertView.getTag();
            image = holder.image;
            name = holder.name;
        }
        if (null != data) {
            String value = data.getDiary() + " " + data.getTravelPointNo() + " " + data.getLatitude() + " " + data.getLongitude();
            if (null != travelPoint) {
                value += " : " + GPSTracker.calcDistance(travelPoint.getLatitude(), travelPoint.getLongitude(), data.getLatitude(), data.getLongitude());
            }

            Uri uri = Uri.parse("android.resource://"+context.getPackageName()+"/drawable/penguin");
            if (null != data.getImagePath()) {
                File imageFile = new File(data.getImagePath());
                if (null != imageFile && imageFile.exists() && imageFile.isFile()) {
                    uri = Uri.fromFile(imageFile);
                }
            }
            image.setImageURI(uri);
            name.setText(value);
        }

        return convertView;
    }

    public void setTravelPoint(TravelPoint travelPoint) {
        this.travelPoint = travelPoint;
    }

    public void addItem(TravelHistory travelPoint) {
        list.add(travelPoint);
    }

    public void addItem(ArrayList<TravelHistory> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void delItem(Context context, int position) {
        TravelHistory travelHistory = list.get(position);

        String path = travelHistory.getImagePath();
        Log.i(Constants.LOGCAT_TAGNAME, "path ; " + path);
        if (null != path) {
            File file = new File (path);
            Log.i(Constants.LOGCAT_TAGNAME, "file.delete() ; " + file.delete());
        }

        TP.deleteTravelHistory(context, travelHistory.getNo());
        list.remove(position);
        notifyDataSetChanged();
    }

    public void clearItems() {
        this.list.clear();
    }

    private class TravelHistoryHolder {
        SimpleDraweeView image;
        TextView name;
    }
}
