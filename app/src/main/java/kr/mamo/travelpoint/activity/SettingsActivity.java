package kr.mamo.travelpoint.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.constant.Constants;

public class SettingsActivity extends PreferenceActivity {
    private static final boolean ALWAYS_SIMPLE_PREFS = false;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupSimplePreferencesScreen();

        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupSimplePreferencesScreen() {
        if (!isSimplePreferences(this)) {
            return;
        }

//        PreferenceCategory fakeHeader = new PreferenceCategory(this);
//        fakeHeader.setTitle(R.string.pref_header_account);
//        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_settings_account);
        PreferenceCategory fakeHeader = new PreferenceCategory(this);
        fakeHeader.setTitle(R.string.pref_header_license);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_settings_license);
        bindPreferenceClickListener(findPreference(Constants.Preference.License.LICENSE));
        /*
        // Add 'data and sync' preferences, and a corresponding header.
        fakeHeader = new PreferenceCategory(this);
        fakeHeader.setTitle(R.string.pref_header_data_sync);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_data_sync);
        */
        // Bind the summaries of EditText/List/Dialog/Ringtone preferences to
        // their values. When their values change, their summaries are updated
        // to reflect the new value, per the Android Design guidelines.
        bindPreferenceSummaryToValue(findPreference(Constants.Preference.Account.EMAIL));
        bindPreferenceClickListener(findPreference(Constants.Preference.Account.LOGOUT));
//        bindPreferenceSummaryToValue(findPreference("example_list"));
//        bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
//        bindPreferenceSummaryToValue(findPreference("sync_frequency"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this) && !isSimplePreferences(this);
    }

    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private static boolean isSimplePreferences(Context context) {
        return ALWAYS_SIMPLE_PREFS
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                || !isXLargeTablet(context);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        if (!isSimplePreferences(this)) {
            loadHeadersFromResource(R.xml.pref_settings_headers, target);
        }
    }

    private Preference.OnPreferenceClickListener onPreferenceClickListener = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (preference.getKey().equals(Constants.Preference.Account.LOGOUT)) {
                logout();
            } else if (preference.getKey().equals(Constants.Preference.License.LICENSE)) {
                startLicenseActivity();
            }
            return true;
        }
    };

    private void startLicenseActivity() {
        Log.i(Constants.LOGCAT_TAGNAME, "licenseActivity");
        Intent intent = new Intent(this, LicenseActivity.class);
        startActivity(intent);
    }

    private void logout() {
        Intent intent = new Intent();
        intent.putExtra(Constants.Preference.Account.LOGOUT, true);
        setResult(Constants.ACTIVITY_RESULT.SETTINGS, intent);
        finish();
    }

    private Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            preference.setSummary(stringValue);
            return true;
        }
    };
    private void bindPreferenceClickListener(Preference preference) {
        preference.setOnPreferenceClickListener(onPreferenceClickListener);
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(onPreferenceChangeListener);

        onPreferenceChangeListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class AccountPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_settings_account);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class LicensePreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_settings_license);
        }
    }
}
