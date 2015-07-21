package kr.mamo.travelpoint.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.adapter.SettingsAdapter;
import kr.mamo.travelpoint.constant.Settings;
import kr.mamo.travelpoint.model.SettingsItem;

public class SettingsActivity extends AppCompatActivity {
    private ListView settingsList;
    private SettingsAdapter settingsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        settingsList = (ListView) findViewById(R.id.settings_list);
        settingsAdapter = new SettingsAdapter();
        settingsList.setAdapter(settingsAdapter);
        settingsList.setOnItemClickListener(itemClickListener);

        for (Settings setting : Settings.values()) {
            settingsAdapter.addItem(new SettingsItem(setting.getMenuId(), setting.getName(getResources())));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void startActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    private boolean onSettingsItemSelected(SettingsItem menu) {
        boolean result = false;
        switch(menu.getId()) {
            case R.id.action_settings :
//                startActivity(SettingsAccountActivity.class);
                result = true;
                break;
            default:
                break;
        }
        return result;
    }

    private AdapterView.OnItemClickListener itemClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long l_position) {
            SettingsItem item = (SettingsItem)parent.getAdapter().getItem(position);
            onSettingsItemSelected(item);

        }
    };
}
