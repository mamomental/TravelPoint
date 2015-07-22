package kr.mamo.travelpoint.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.activity.ImageActivity;
import kr.mamo.travelpoint.adapter.TravelHistoryAdapter;
import kr.mamo.travelpoint.db.TP;
import kr.mamo.travelpoint.db.domain.TravelHistory;

public class FragmentTravelHistory extends Fragment {
    private ListView travelHistoryList;
    private TravelHistoryAdapter travelHistoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travel_history, container, false);

        travelHistoryList = (ListView) view.findViewById(R.id.travel_history_list);
        travelHistoryAdapter = new TravelHistoryAdapter();
        travelHistoryList.setAdapter(travelHistoryAdapter);
        travelHistoryList.setOnItemClickListener(itemClickListener);

        ArrayList<TravelHistory> list =  TP.readTravelHistoryList(getActivity(), 1, 1);

        for (TravelHistory travelHistory : list) {
            travelHistoryAdapter.addItem(travelHistory);
        }
        return view;
    }

    private void startImageActivity() {
        Intent intent = new Intent(getActivity(), ImageActivity.class);
        startActivity(intent);
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long l_position) {
            TravelHistory travel = (TravelHistory)parent.getAdapter().getItem(position);

            startImageActivity();
        }
    };
}
