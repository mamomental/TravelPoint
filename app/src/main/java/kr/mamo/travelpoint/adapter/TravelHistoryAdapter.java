package kr.mamo.travelpoint.adapter;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.constant.Constants;
import kr.mamo.travelpoint.db.TP;
import kr.mamo.travelpoint.db.domain.TravelHistory;
import kr.mamo.travelpoint.db.domain.TravelPoint;
import kr.mamo.travelpoint.util.GPSTracker;
import kr.mamo.travelpoint.util.TPUtil;

/**
 * Created by alucard on 2015-07-17.
 */
public class TravelHistoryAdapter extends BaseAdapter {
    Context context;
    ArrayList<TravelHistory> list;
    TravelPoint travelPoint;

    public TravelHistoryAdapter(Context context) {
        this.context = context;
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
        TextView diary = null;
        TextView distance = null;
        TextView date = null;
        TravelHistoryHolder holder = null;

        if (null == convertView) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.travel_history_item, parent, false);

            image = (SimpleDraweeView) convertView.findViewById(R.id.travel_history_item_image);
            diary    = (TextView) convertView.findViewById(R.id.travel_history_item_diary);
            distance    = (TextView) convertView.findViewById(R.id.travel_history_item_distance);
            date    = (TextView) convertView.findViewById(R.id.travel_history_item_date);

            holder = new TravelHistoryHolder();
            holder.image = image;
            holder.diary = diary;
            holder.distance = distance;
            holder.date = date;
            convertView.setTag(holder);
        }
        else {
            holder  = (TravelHistoryHolder) convertView.getTag();
            image = holder.image;
            diary = holder.diary;
            distance = holder.distance;
            date = holder.date;
        }
        if (null != data) {
            Uri uri = Uri.parse("android.resource://"+context.getPackageName()+"/drawable/penguin");
            if (null != data.getImagePath()) {
                File imageFile = new File(data.getImagePath());
                if (null != imageFile && imageFile.exists() && imageFile.isFile()) {
                    uri = Uri.fromFile(imageFile);
                }
            }
            image.setImageURI(uri);
            if (null != travelPoint) {
                diary.setText(data.getDiary());
                distance.setText(GPSTracker.calcDistance(travelPoint.getLatitude(), travelPoint.getLongitude(), data.getLatitude(), data.getLongitude()));
                date.setText(TPUtil.convertString(context, data.getCreateDate()));
            }
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
            context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    MediaStore.Images.Media.DATA + "='" + path + "'", null);
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
        TextView diary;
        TextView distance;
        TextView date;
    }
}
