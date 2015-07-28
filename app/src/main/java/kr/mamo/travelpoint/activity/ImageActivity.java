package kr.mamo.travelpoint.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;


import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.db.TP;
import kr.mamo.travelpoint.db.domain.TravelHistory;

public class ImageActivity extends AppCompatActivity {
    SubsamplingScaleImageView travelHistoryImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        travelHistoryImage = (SubsamplingScaleImageView) findViewById(R.id.travel_history_image);
        travelHistoryImage.setOrientation(SubsamplingScaleImageView.ORIENTATION_USE_EXIF);

        setData();
    }

    private void setData() {
        Intent intent = getIntent();
        if (null != intent) {
            int no = intent.getIntExtra(kr.mamo.travelpoint.db.table.TravelHistory.Schema.COLUMN.NO.getName(), 0);
            if (0 < no) {
                TravelHistory travelHistory = TP.readTravelHistory(this, no);

                if (null != travelHistory) {
                    travelHistoryImage.setImage(ImageSource.uri(Uri.fromFile(new File(travelHistory.getImagePath()))));
                }
            }
        }
    }
}
