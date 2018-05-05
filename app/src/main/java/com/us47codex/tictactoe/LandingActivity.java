package com.us47codex.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by astics-08 on 6/1/18.
 */

public class LandingActivity extends Activity implements View.OnClickListener {
    private static final String TAG = LandingActivity.class.getSimpleName();
    private Context context;
    private LinearLayout llPlayerWithComputer;
    private LinearLayout llPlayersSameDevice;
    private LinearLayout llPlayersMultiDevice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = LandingActivity.this;
        setContentView(R.layout.activity_landing);

        initializeView();
    }

    private void initializeView() {

        llPlayerWithComputer = (LinearLayout) findViewById(R.id.llPlayerWithComputer);
        llPlayersSameDevice = (LinearLayout) findViewById(R.id.llPlayersSameDevice);
        llPlayersMultiDevice = (LinearLayout) findViewById(R.id.llPlayersMultiDevice);

        llPlayerWithComputer.setOnClickListener(this);
        llPlayersSameDevice.setOnClickListener(this);
        llPlayersMultiDevice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llPlayerWithComputer:

                break;

            case R.id.llPlayersSameDevice:

                break;

            case R.id.llPlayersMultiDevice:

                break;
        }
    }
}
