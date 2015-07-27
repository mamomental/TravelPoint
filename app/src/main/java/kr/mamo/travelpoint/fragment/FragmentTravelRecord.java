package kr.mamo.travelpoint.fragment;

import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.activity.MainActivity;
import kr.mamo.travelpoint.constant.Constants;
import kr.mamo.travelpoint.db.TP;
import kr.mamo.travelpoint.db.domain.TravelPoint;

public class FragmentTravelRecord extends Fragment implements FragmentTravelHistory.OnCaptureImageListener, FragmentTravelPoint.OnClickTravelPointListener {
    SimpleDraweeView travelRecordImage;
    FragmentTravelHistory.OnCaptureImageListener captureImageListener;
    EditText travelRecordText;
    Button travelRecordBtn;
    Uri currentUri;
    TravelPoint travelPoint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        View view = inflater.inflate(R.layout.fragment_travel_record, container, false);

        travelRecordImage = (SimpleDraweeView) view.findViewById(R.id.travel_record_image);
        travelRecordText = (EditText) view.findViewById(R.id.travel_record_text);
        travelRecordBtn = (Button) view.findViewById(R.id.travel_record_btn);
        travelRecordBtn.setOnClickListener(recordClickListener);

        return view;
    }

    @Override
    public void OnClickTravelPoint(TravelPoint travelPoint) {
        this.travelPoint = travelPoint;
    }

    @Override
    public void OnCaptureImage(Uri uri) {
        this.currentUri = uri;
    }

    private View.OnClickListener recordClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
        String diary = travelRecordText.getText().toString();
        travelRecordImage.setImageURI(currentUri);
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.LATITUDE, MediaStore.Images.ImageColumns.LONGITUDE};
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, MediaStore.Images.ImageColumns.DATA + "='" + currentUri.getPath() + "'", null, null);

        if (null != cursor && cursor.moveToNext()) {
            double lat = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE));
            double lon = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE));
            TP.createTravelHistory(getActivity(), travelPoint, currentUri.getPath(), lat, lon, diary);
        }
            ((MainActivity)getActivity()).displayFragment(Constants.Fragment.MainActivity.F3);
        }
    };
}
