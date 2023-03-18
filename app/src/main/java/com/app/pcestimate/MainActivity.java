package com.app.pcestimate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.app.pcestimate.view.estimate.ActivityPriceSelector;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // test
//        startActivity(new Intent(this, ActivityPriceSelector.class));
//        finish();

        startActivity(new Intent(this, ActivityPriceSelector.class));
//        finish();

    }
}