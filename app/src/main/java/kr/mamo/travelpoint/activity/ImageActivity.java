package kr.mamo.travelpoint.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.adapter.SlideMenuAdapter;
import kr.mamo.travelpoint.constant.Constants;
import kr.mamo.travelpoint.constant.SlideMenu;
import kr.mamo.travelpoint.db.TP;
import kr.mamo.travelpoint.fragment.FragmentTravel;
import kr.mamo.travelpoint.fragment.FragmentTravelHistory;
import kr.mamo.travelpoint.fragment.FragmentTravelPoint;
import kr.mamo.travelpoint.model.SlideMenuItem;

public class ImageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}
