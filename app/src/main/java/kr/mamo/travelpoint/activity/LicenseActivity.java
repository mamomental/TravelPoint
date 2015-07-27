package kr.mamo.travelpoint.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.constant.Constants;

public class LicenseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Constants.LOGCAT_TAGNAME, "licenseActivity2");
        setContentView(R.layout.activity_license);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}
