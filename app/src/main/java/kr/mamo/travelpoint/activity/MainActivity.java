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
import kr.mamo.travelpoint.constant.SlideMenu;
import kr.mamo.travelpoint.db.TP;
import kr.mamo.travelpoint.fragment.FragmentTravel;
import kr.mamo.travelpoint.fragment.FragmentTravelPoint;
import kr.mamo.travelpoint.model.SlideMenuItem;

public class MainActivity extends AppCompatActivity {
    DrawerLayout slideMenuLayout;
    ListView slideMenuList;
    SlideMenuAdapter adapter;
    Fragment fragmentTravel;
    Fragment fragmentTravelPoint;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        fragmentTravel = new FragmentTravel();
        fragmentTravelPoint = new FragmentTravelPoint();

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
        Fragment fragment = getFragmentManager().findFragmentByTag("travelPoint");
        if (null != fragment && fragment.isVisible()) {
            displayFragment(1);
            return;
        }
        super.onBackPressed();
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

    private void startLoginActivity(Class clazz, boolean finish) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        if (finish) {
            TP.signOut(this);
            finish();
        }
    }

    private boolean onSlideMenuItemSelected(SlideMenuItem menu) {
        boolean result = false;
        switch(menu.getId()) {
            case R.id.action_settings :
                startLoginActivity(SettingsActivity.class, false);
                result = true;
                break;
//            case R.id.action_logout :
//                startLoginActivity(LoginActivity.class, true);
//                result = true;
//                break;
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
