package kr.mamo.travelpoint.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.activity.MainActivity;
import kr.mamo.travelpoint.adapter.TravelAdapter;
import kr.mamo.travelpoint.db.TP;
import kr.mamo.travelpoint.db.domain.Travel;

public class FragmentTravel extends Fragment {
    private ListView travelList;
    private TravelAdapter travelAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travel, container, false);

        travelList = (ListView) view.findViewById(R.id.travel_list);
        travelAdapter = new TravelAdapter();
        travelList.setAdapter(travelAdapter);
        travelList.setOnItemClickListener(itemClickListener);

        ArrayList<Travel> list =  TP.readTravelList(getActivity());

        for (Travel travel : list) {
            travelAdapter.addTravel(travel);
        }
        return view;
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long l_position) {
            Travel travel = (Travel)parent.getAdapter().getItem(position);
            ((MainActivity)getActivity()).displayFragment(2);
        }
    };
}
