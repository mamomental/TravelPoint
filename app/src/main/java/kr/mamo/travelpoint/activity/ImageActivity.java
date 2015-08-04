package kr.mamo.travelpoint.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.db.TP;
import kr.mamo.travelpoint.db.domain.TravelHistory;

public class ImageActivity extends AppCompatActivity {
    SubsamplingScaleImageView travelHistoryImage;
    SimpleDraweeView travalHistoryStamp;
    TextView diaryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        travelHistoryImage = (SubsamplingScaleImageView) findViewById(R.id.travel_history_image);
        travelHistoryImage.setOrientation(SubsamplingScaleImageView.ORIENTATION_USE_EXIF);
        travalHistoryStamp = (SimpleDraweeView) findViewById(R.id.travel_history_stamp);
        diaryTextView = (TextView) findViewById(R.id.travel_history_diary);

        setData();
    }

    private void setData() {
        Intent intent = getIntent();
        if (null != intent) {
            int no = intent.getIntExtra(kr.mamo.travelpoint.db.table.TravelHistory.Schema.COLUMN.NO.getName(), 0);
            if (0 < no) {
                TravelHistory travelHistory = TP.readTravelHistory(this, no);

                Uri uri = Uri.parse("android.resource://"+getPackageName()+"/drawable/penguin");
                if (null != travelHistory) {
                    String path = travelHistory.getImagePath();
                    if (null != path) {
                        File file = new File (path);
                        if (file.exists() && file.isFile()) {
                            uri = Uri.fromFile(file);
                        } else {
                            Toast.makeText(this, R.string.alert_file, Toast.LENGTH_LONG).show();
                        }
                    }
                    diaryTextView.setText(travelHistory.getDiary());
                }
                travelHistoryImage.setImage(ImageSource.uri(uri));

                if (travelHistory.getLatitude() > 0 && travelHistory.getLongitude() > 0) {

                } else {
                    travalHistoryStamp.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
