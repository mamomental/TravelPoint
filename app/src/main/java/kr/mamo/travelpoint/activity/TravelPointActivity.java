package kr.mamo.travelpoint.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.adapter.TravelPointAdapter;
import kr.mamo.travelpoint.db.TP;
import kr.mamo.travelpoint.db.domain.TravelPoint;


public class TravelPointActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView travelPointList;
        TravelPointAdapter travelPointAdapter;

        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_travel_point);
        travelPointList = (ListView) findViewById(R.id.travel_point_list);
        travelPointAdapter = new TravelPointAdapter();
        travelPointList.setAdapter(travelPointAdapter);



        ArrayList<TravelPoint> list =  TP.readTravelPointList(this, 1);

        for (TravelPoint travelPoint : list) {
            travelPointAdapter.addTravelPoint(travelPoint);
        }

        SimpleDraweeView simpleDraweeView = (SimpleDraweeView)findViewById(R.id.travel_point_image);
        simpleDraweeView.setImageURI(Uri.parse("http://www.jeju.go.kr/upload_data/board_data/BBS_0000027/140964116652938.gif"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void doActionLogout() {
        TP.signOut(this);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings :
                break;
            case R.id.action_logout :
                doActionLogout();
                return true;
//                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
