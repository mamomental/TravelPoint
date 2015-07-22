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

public class MainActivity extends AppCompatActivity {
    DrawerLayout slideMenuLayout;
    ListView slideMenuList;
    SlideMenuAdapter adapter;
    Fragment fragmentTravel;
    Fragment fragmentTravelPoint;
    Fragment fragmentTravelHistory;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        fragmentTravel = new FragmentTravel();
        fragmentTravelPoint = new FragmentTravelPoint();
        fragmentTravelHistory = new FragmentTravelHistory();

        displayFragment(1);
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
            displayFragment(2);
            return;
        }
        fragment = getFragmentManager().findFragmentByTag("travelPoint");
        if (null != fragment && fragment.isVisible()) {
            displayFragment(1);
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
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch(poistion) {
            case 1 :
                transaction.replace(R.id.content_frame, fragmentTravel, "travel");
                break;
            case 2 :
                transaction.replace(R.id.content_frame, fragmentTravelPoint, "travelPoint");
                break;
            case 3 :
                transaction.replace(R.id.content_frame, fragmentTravelHistory, "travelHistory");
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
