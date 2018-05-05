package com.us47codex.tictactoe.util;


public class Log {
    /* Logging and Console */
    public static boolean DO_SOP = true;
    public static String WHICH_HOST = "";

    public static void print(String mesg) {
        if (Log.DO_SOP) {
            android.util.Log.v(Config.TAG, mesg);
        }
    }

    public static void print(String title, String mesg) {
        if (Log.DO_SOP) {
            android.util.Log.v(title, mesg);
        }
    }

    public static void error(String title, Exception e) {
        if (Log.DO_SOP) {
            Log.print("============:::" + title + ":::================");
            android.util.Log.e(Config.TAG + " HOST ::" + WHICH_HOST, title, e);
            Log.print("===============================================");
        }
//        FirebaseCrash.report(new Exception(Config.TAG + " HOST ::" + WHICH_HOST + " Title ::" + title, e));
    }
}