package kr.mamo.travelpoint.fragment;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.activity.MainActivity;
import kr.mamo.travelpoint.adapter.TravelPointAdapter;
import kr.mamo.travelpoint.db.TP;
import kr.mamo.travelpoint.db.domain.Travel;
import kr.mamo.travelpoint.db.domain.TravelPoint;

public class FragmentTravelPoint extends Fragment implements FragmentTravel.OnClickTravelListener {
    private ListView travelPointList;
    private TravelPointAdapter travelPointAdapter;
    GoogleMap googleMap;
    SimpleDraweeView travelImage;
    TextView travelDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Fresco.initialize(getActivity());


        View view = inflater.inflate(R.layout.fragment_travel_point, container, false);
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.travel_map)).getMap();
        travelPointList = (ListView) view.findViewById(R.id.travel_point_list);
        travelPointAdapter = new TravelPointAdapter();
        travelPointList.setAdapter(travelPointAdapter);
        travelPointList.setOnItemClickListener(itemClickListener);

        travelImage = (SimpleDraweeView) view.findViewById(R.id.travel_image);
        travelImage.setImageURI(Uri.parse("http://www.jeju.go.kr/upload_data/board_data/BBS_0000027/140964116652938.gif"));

        travelDescription = (TextView) view.findViewById(R.id.travel_description);
        return view;
    }

    @Override
    public void OnClickTravel(Travel travel) {
        LatLng ll = new LatLng(travel.getLatitude(), travel.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ll, 7);
        googleMap.addMarker(new MarkerOptions()
                        .position(ll)
                        .title(travel.getName())
                        .draggable(false)
        );
        googleMap.moveCamera(cameraUpdate);

        ArrayList<TravelPoint> list =  TP.readTravelPointList(getActivity(), travel.getNo());
        travelPointAdapter.addTravelPoint(list);
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long l_position) {
            TravelPoint travel = (TravelPoint)parent.getAdapter().getItem(position);
            ((MainActivity)getActivity()).displayFragment(3);
        }
    };
}
