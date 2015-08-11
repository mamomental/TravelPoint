package kr.mamo.travelpoint.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.ArrayList;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.activity.ImageActivity;
import kr.mamo.travelpoint.activity.MainActivity;
import kr.mamo.travelpoint.adapter.TravelHistoryAdapter;
import kr.mamo.travelpoint.constant.Constants;
import kr.mamo.travelpoint.db.TP;
import kr.mamo.travelpoint.db.domain.TravelHistory;
import kr.mamo.travelpoint.db.domain.TravelPoint;
import kr.mamo.travelpoint.util.DipUtil;
import kr.mamo.travelpoint.util.GPSTracker;

public class FragmentTravelHistory extends Fragment implements FragmentTravelPoint.OnClickTravelPointListener {
    private SwipeMenuListView travelHistoryList;
    private TravelHistoryAdapter travelHistoryAdapter;

    OnCaptureImageListener captureImageListener;

    GoogleMap googleMap;
    TextView travelPointDescription;
    Button camera;
    TravelPoint travelPoint;

    GPSTracker gpsTracker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travel_history, container, false);
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.travel_point_map)).getMap();
        travelHistoryList = (SwipeMenuListView) view.findViewById(R.id.travel_history_list);
        travelHistoryAdapter = new TravelHistoryAdapter(getActivity());
        travelHistoryList.setAdapter(travelHistoryAdapter);
        travelHistoryList.setOnItemClickListener(itemClickListener);
        travelHistoryList.setMenuCreator(creator);
        travelHistoryList.setOnMenuItemClickListener(swipeMenuListener);
        travelPointDescription = (TextView) view.findViewById(R.id.travel_point_description);
        camera = (Button) view.findViewById(R.id.travel_history_camera);
        camera.setOnClickListener(cameraClickListener);
        gpsTracker = new GPSTracker(getActivity());
        return view;
    }

    private void startImageActivity(TravelHistory travelHistory) {
        Intent intent = new Intent(getActivity(), ImageActivity.class);
        intent.putExtra(kr.mamo.travelpoint.db.table.TravelHistory.Schema.COLUMN.NO.getName(), travelHistory.getNo());
        startActivity(intent);
    }

    @Override
    public void OnClickTravelPoint(TravelPoint travelPoint) {
        this.travelPoint = travelPoint;
        travelHistoryAdapter.setTravelPoint(travelPoint);

        LatLng ll = new LatLng(travelPoint.getLatitude(), travelPoint.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ll, 15);
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions()
                        .position(ll)
                        .title(travelPoint.getName())
                        .draggable(false)
        );
        googleMap.moveCamera(cameraUpdate);
        travelPointDescription.setText(travelPoint.getDescription());
        loadTravelHistory();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            loadTravelHistory();
        }
    }

    private void loadTravelHistory() {
        ArrayList<TravelHistory> list =  TP.readTravelHistoryList(getActivity(), travelPoint.getNo());

        travelHistoryAdapter.clearItems();
        for (TravelHistory travelHistory : list) {
            travelHistoryAdapter.addItem(travelHistory);
        }
        travelHistoryAdapter.notifyDataSetInvalidated();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.ACTIVITY_RESULT.CAMERA && resultCode == getActivity().RESULT_OK) {
            String[] projection = {MediaStore.Images.ImageColumns.DATA};
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

            if (null != cursor && cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                if (null != captureImageListener) {
                    ((MainActivity)getActivity()).displayFragment(Constants.Fragment.MainActivity.F4);
                    captureImageListener.OnCaptureImage(Uri.fromFile(new File(path)), gpsTracker.getLocation());
                }
            }

        }
    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "open" item
            SwipeMenuItem openItem = new SwipeMenuItem(getActivity());
            // set item background
            openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
            // set item width
            openItem.setWidth(DipUtil.convertDpToPixel(80, getActivity()));
            // set item title
            openItem.setTitle("Open");
            // set item title fontsize
            openItem.setTitleSize(18);
            // set item title font color
            openItem.setTitleColor(Color.WHITE);
            // add to menu
            menu.addMenuItem(openItem);

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(DipUtil.convertDpToPixel(80, getActivity()));
            // set a icon
            deleteItem.setIcon(R.drawable.penguin);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };

    private SwipeMenuListView.OnMenuItemClickListener swipeMenuListener = new SwipeMenuListView.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
            switch (index) {
                case 0:
                    // open
                    Toast.makeText(getActivity(), "open", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    // delete
                    travelHistoryAdapter.delItem(getActivity(), position);
                    break;
            }
            // false : close the menu; true : not close the menu
            return false;
        }
    };

    private View.OnClickListener cameraClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Location location = gpsTracker.getLocation();
            String distance = GPSTracker.calcDistance(travelPoint.getLatitude(), travelPoint.getLongitude(), location.getLatitude(), location.getLongitude());

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage(String.format(getActivity().getString(R.string.alert_distance), travelPoint.getName(), distance)).setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, Constants.ACTIVITY_RESULT.CAMERA);
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // no
                }
            });

            AlertDialog dialog = builder.create();
            dialog.setTitle("Test");
            dialog.show();
        }
    };

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long l_position) {
            TravelHistory travelHistory = (TravelHistory)parent.getAdapter().getItem(position);
            startImageActivity(travelHistory);
        }
    };

    public OnCaptureImageListener getCaptureImageListener() {
        return captureImageListener;
    }

    public void setCaptureImageListener(OnCaptureImageListener captureImageListener) {
        this.captureImageListener = captureImageListener;
    }

    public interface OnCaptureImageListener {
        public void OnCaptureImage(Uri uri, Location location);
    }
}
