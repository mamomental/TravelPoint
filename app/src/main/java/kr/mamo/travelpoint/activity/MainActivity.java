package kr.mamo.travelpoint.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.constant.Constants;
import kr.mamo.travelpoint.fragment.FragmentTravel;
import kr.mamo.travelpoint.fragment.FragmentTravelPoint;

public class MainActivity extends AppCompatActivity {
    String[] menu;
    DrawerLayout dLayout;
    ListView dList;
    ArrayAdapter<String> adapter;
    Fragment fragmentTravel;
    Fragment fragmentTravelPoint;
//    private ListView travelList;
//    private TravelAdapter travelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentTravel = new FragmentTravel();
        fragmentTravelPoint = new FragmentTravelPoint();
        replaceFragment(1);
//        travelList = (ListView) findViewById(R.id.travel_list);
//        travelAdapter = new TravelAdapter();
//        travelList.setAdapter(travelAdapter);
////        travelList.setOnItemClickListener(itemClickListener);
//
//        ArrayList<Travel> list =  TP.readTravelList(this);
//
//        for (Travel travel : list) {
//            travelAdapter.addTravel(travel);
//        }
//
        menu = new String[]{"Home","Android","Windows","Linux","Raspberry Pi","WordPress","Videos","Contact Us"};
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        dList = (ListView) findViewById(R.id.left_drawer);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menu);

        dList.setAdapter(adapter);
        dList.setSelector(android.R.color.holo_blue_dark);

        dList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                dLayout.closeDrawers();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void replaceFragment(int fragment) {

        Log.i(Constants.LOGCAT_TAGNAME, "replaceFrament : " + fragment);

        Fragment newFragment = fragmentTravel;
        if (fragment == 1) {
            newFragment = fragmentTravel;

        } else if (fragment == 2) {
            newFragment = fragmentTravelPoint;

        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
