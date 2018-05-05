package com.us47codex.tictactoe;

import android.app.Application;

/**
 * Created by Upen on 06/01/17.
 */
public class TicTacToeApplication extends Application {
    private static final String TAG = TicTacToeApplication.class.getSimpleName();
    private static TicTacToeApplication mInstance = null;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized TicTacToeApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}