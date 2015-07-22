package kr.mamo.travelpoint.fragment;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.activity.MainActivity;
import kr.mamo.travelpoint.adapter.TravelPointAdapter;
import kr.mamo.travelpoint.db.TP;
import kr.mamo.travelpoint.db.domain.TravelPoint;

public class FragmentTravelPoint extends Fragment {
    private ListView travelPointList;
    private TravelPointAdapter travelPointAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        View view = inflater.inflate(R.layout.fragment_travel_point, container, false);

        travelPointList = (ListView) view.findViewById(R.id.travel_point_list);
        travelPointAdapter = new TravelPointAdapter();
        travelPointList.setAdapter(travelPointAdapter);
        travelPointList.setOnItemClickListener(itemClickListener);

        ArrayList<TravelPoint> list =  TP.readTravelPointList(getActivity(), 1);

        for (TravelPoint travelPoint : list) {
            travelPointAdapter.addTravelPoint(travelPoint);
        }

        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.travel_point_image);
        simpleDraweeView.setImageURI(Uri.parse("http://www.jeju.go.kr/upload_data/board_data/BBS_0000027/140964116652938.gif"));
        return view;
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long l_position) {
            TravelPoint travel = (TravelPoint)parent.getAdapter().getItem(position);
            ((MainActivity)getActivity()).displayFragment(3);
        }
    };
}
