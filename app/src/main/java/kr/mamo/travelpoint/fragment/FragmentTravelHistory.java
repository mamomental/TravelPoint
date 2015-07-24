package kr.mamo.travelpoint.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.activity.ImageActivity;
import kr.mamo.travelpoint.activity.MainActivity;
import kr.mamo.travelpoint.adapter.TravelHistoryAdapter;
import kr.mamo.travelpoint.constant.Constants;
import kr.mamo.travelpoint.db.TP;
import kr.mamo.travelpoint.db.domain.TravelHistory;
import kr.mamo.travelpoint.db.domain.TravelPoint;
import kr.mamo.travelpoint.db.domain.User;

public class FragmentTravelHistory extends Fragment implements FragmentTravelPoint.OnClickTravelPointListener {
    private ListView travelHistoryList;
    private TravelHistoryAdapter travelHistoryAdapter;

    OnCaptureImageListener captureImageListener;

    GoogleMap googleMap;
    TextView travelPointDescription;
    Button camera;
    String cameraPath;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travel_history, container, false);
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.travel_point_map)).getMap();
        travelHistoryList = (ListView) view.findViewById(R.id.travel_history_list);
        travelHistoryAdapter = new TravelHistoryAdapter();
        travelHistoryList.setAdapter(travelHistoryAdapter);
        travelHistoryList.setOnItemClickListener(itemClickListener);
        travelPointDescription = (TextView) view.findViewById(R.id.travel_point_description);
        camera = (Button) view.findViewById(R.id.travel_history_camera);
        camera.setOnClickListener(cameraClickListener);
        return view;
    }

    private void startImageActivity() {
        Intent intent = new Intent(getActivity(), ImageActivity.class);
        startActivity(intent);
    }

    @Override
    public void OnClickTravelPoint(TravelPoint travelPoint) {
        LatLng ll = new LatLng(travelPoint.getLatitude(), travelPoint.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ll, 15);
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions()
                        .position(ll)
                        .title(travelPoint.getName())
                        .draggable(false)
        );
        googleMap.moveCamera(cameraUpdate);

        User user = TP.autoLogin(getActivity());
        ArrayList<TravelHistory> list =  TP.readTravelHistoryList(getActivity(), user.getNo(), travelPoint.getNo());

        for (TravelHistory travelHistory : list) {
            travelHistoryAdapter.addItem(travelHistory);
        }

        travelPointDescription.setText(travelPoint.getDescription());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.ACTIVITY_RESULT.CAMERA && resultCode == getActivity().RESULT_OK) {
            Log.i(Constants.LOGCAT_TAGNAME, "camera");
            String[] projection = {MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.LATITUDE, MediaStore.Images.ImageColumns.LONGITUDE};
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

            if (null != cursor && cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
                double lat = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE));
                double lon = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE));

                Log.i(Constants.LOGCAT_TAGNAME, "id : " + id);
                Log.i(Constants.LOGCAT_TAGNAME, "lat : " + lat);
                Log.i(Constants.LOGCAT_TAGNAME, "lon : " + lon);

                if (null != captureImageListener) {
                    ((MainActivity)getActivity()).displayFragment(Constants.Fragment.MainActivity.F4);
                }
            }

        }
    }

    private View.OnClickListener cameraClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(takePictureIntent, Constants.ACTIVITY_RESULT.CAMERA);

        Log.i(Constants.LOGCAT_TAGNAME, "사진 찍었다 치고");
        String[] projection = {MediaStore.Images.ImageColumns.DATA};
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        if (null != cursor && cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
            if (null != captureImageListener) {
                ((MainActivity)getActivity()).displayFragment(Constants.Fragment.MainActivity.F4);
                captureImageListener.OnCaptureImage(Uri.fromFile(new File(path)));
            }
        }
        }
    };

    // 저장하기
    final static String JPEG_FILE_PREFIX = "IMG_";
    final static String JPEG_FILE_SUFFIX = ".jpg";

    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat( "yyyyMMdd_HHmmss").format( new Date());
        Log.i(Constants.LOGCAT_TAGNAME, "timeStamp : " + timeStamp);
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        Log.i(Constants.LOGCAT_TAGNAME, "imageFileName : " + imageFileName);
        File image = File.createTempFile(
                imageFileName,			// prefix
                JPEG_FILE_SUFFIX,		// suffix
                getAlbumDir()				// directory
        );

        cameraPath = image.getAbsolutePath();
        Log.i(Constants.LOGCAT_TAGNAME, "cameraPath : " + cameraPath);
        return image;
    }

    public File getAlbumDir(){
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                ),
               "TravelPoint"
        );

        Log.i(Constants.LOGCAT_TAGNAME, "storageDir : " + storageDir.getAbsolutePath());
        return storageDir;
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long l_position) {
            TravelHistory travel = (TravelHistory)parent.getAdapter().getItem(position);

            startImageActivity();
        }
    };

    public OnCaptureImageListener getCaptureImageListener() {
        return captureImageListener;
    }

    public void setCaptureImageListener(OnCaptureImageListener captureImageListener) {
        this.captureImageListener = captureImageListener;
    }

    public interface OnCaptureImageListener {
        public void OnCaptureImage(Uri uri);
    }
}
