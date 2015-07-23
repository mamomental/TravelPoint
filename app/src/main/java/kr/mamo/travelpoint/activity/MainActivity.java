package kr.mamo.travelpoint.activity;

import android.app.Fragment;
import android.app.FragmentManager;
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

public class MainActivity extends AppCompatActivity {
    DrawerLayout slideMenuLayout;
    ListView slideMenuList;
    SlideMenuAdapter adapter;
    FragmentTravel fragmentTravel;
    FragmentTravelPoint fragmentTravelPoint;
    FragmentTravelHistory fragmentTravelHistory;

    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        displayFragment(Constants.Fragment.MainActivity.F1);
        initSlideMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
      return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getFragmentManager().findFragmentByTag("travelHistory");
        if (null != fragment && fragment.isVisible()) {
            displayFragment(Constants.Fragment.MainActivity.F2);
            return;
        }
        fragment = getFragmentManager().findFragmentByTag("travelPoint");
        if (null != fragment && fragment.isVisible()) {
            displayFragment(Constants.Fragment.MainActivity.F1);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case Constants.ACTIVITY_RESULT.SETTINGS :
                boolean logout = data.getBooleanExtra(Constants.Preference.Account.LOGOUT, false);
                if (logout) {
                    TP.signOut(this);
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                break;
            default:
                break;
        }
    }

    public void displayFragment(int poistion) {
        FragmentManager fm = getFragmentManager();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (null == fm.findFragmentByTag("travel")) {
            fragmentTravel = new FragmentTravel();
            transaction.add(R.id.content_frame, fragmentTravel, "travel");
        }
        if (null == fm.findFragmentByTag("travelPoint")) {
            fragmentTravelPoint = new FragmentTravelPoint();
            fragmentTravel.setTravelListener(fragmentTravelPoint);
            transaction.add(R.id.content_frame, fragmentTravelPoint, "travelPoint");
        }
        if (null == fm.findFragmentByTag("travelHistory")) {
            fragmentTravelHistory = new FragmentTravelHistory();
            fragmentTravelPoint.setTravelPointListener(fragmentTravelHistory);
            transaction.add(R.id.content_frame, fragmentTravelHistory, "travelHistory");
        }

        switch(poistion) {
            case Constants.Fragment.MainActivity.F1 :
                transaction.show(fragmentTravel);
                transaction.hide(fragmentTravelPoint);
                transaction.hide(fragmentTravelHistory);
                break;
            case Constants.Fragment.MainActivity.F2 :
                transaction.hide(fragmentTravel);
                transaction.show(fragmentTravelPoint);
                transaction.hide(fragmentTravelHistory);
                break;
            case Constants.Fragment.MainActivity.F3 :
                transaction.hide(fragmentTravel);
                transaction.hide(fragmentTravelPoint);
                transaction.show(fragmentTravelHistory);
                break;
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initSlideMenu() {
        slideMenuLayout = (DrawerLayout) findViewById(R.id.side_menu_layout);
        slideMenuList = (ListView) findViewById(R.id.side_menu);

        toggle = new ActionBarDrawerToggle (
                this,
                slideMenuLayout,
                R.string.app_name,
                R.string.app_name);
        slideMenuLayout.setDrawerListener(toggle);
        toggle.syncState();

        adapter = new SlideMenuAdapter();
        for (SlideMenu menu : SlideMenu.values()) {
            adapter.addMenu(new SlideMenuItem(menu.getMenuId(), menu.getName(getResources())));
        }
        slideMenuList.setAdapter(adapter);
        slideMenuList.setOnItemClickListener(new SideMenuItemClickListener());
    }

    private boolean onSlideMenuItemSelected(SlideMenuItem menu) {
        boolean result = false;
        switch(menu.getId()) {
            case R.id.action_settings :
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, Constants.ACTIVITY_RESULT.SETTINGS);
                result = true;
                break;
            default:
                break;
        }
        return result;
    }

    private class SideMenuItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
            slideMenuLayout.closeDrawers();
            onSlideMenuItemSelected((SlideMenuItem)adapter.getItem(position));
        }
    }
}
