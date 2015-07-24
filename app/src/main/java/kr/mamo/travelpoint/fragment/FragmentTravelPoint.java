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

import java.util.ArrayList;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.activity.MainActivity;
import kr.mamo.travelpoint.adapter.TravelPointAdapter;
import kr.mamo.travelpoint.constant.Constants;
import kr.mamo.travelpoint.db.TP;
import kr.mamo.travelpoint.db.domain.Travel;
import kr.mamo.travelpoint.db.domain.TravelPoint;

public class FragmentTravelPoint extends Fragment implements FragmentTravel.OnClickTravelListener {
    private ListView travelPointList;
    private TravelPointAdapter travelPointAdapter;
    SimpleDraweeView travelImage;
    TextView travelDescription;
    OnClickTravelPointListener travelPointListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Fresco.initialize(getActivity());


        View view = inflater.inflate(R.layout.fragment_travel_point, container, false);

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
        ArrayList<TravelPoint> list =  TP.readTravelPointList(getActivity(), travel.getNo());
        travelPointAdapter.addItem(list);
        travelDescription.setText(travel.getDescription());
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long l_position) {
            TravelPoint travelPoint = (TravelPoint)parent.getAdapter().getItem(position);
            if (null != travelPointListener) {
                travelPointListener.OnClickTravelPoint(travelPoint);
            }

            ((MainActivity)getActivity()).displayFragment(Constants.Fragment.MainActivity.F3);
        }
    };

    public OnClickTravelPointListener getTravelPointListener() {
        return travelPointListener;
    }

    public void setTravelPointListener(OnClickTravelPointListener travelPointListener) {
        this.travelPointListener = travelPointListener;
    }

    public interface OnClickTravelPointListener {
        public void OnClickTravelPoint(TravelPoint travelPoint);
    }
}
