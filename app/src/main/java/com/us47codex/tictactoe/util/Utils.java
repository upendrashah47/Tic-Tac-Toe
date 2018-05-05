package com.us47codex.tictactoe.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Patterns;
import android.webkit.URLUtil;

import com.us47codex.tictactoe.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Utils {
    /**
     * System Upgrade for Database.
     */
    public static void systemUpgrade(Context context) {
        DBHelper dbHelper = null;
        int level = 0;
        try {
            dbHelper = new DBHelper(context);
            level = Integer.parseInt(Pref.getValue(context, Config.LEVEL, "0"));

            if (level == 0) {
                dbHelper.upgrade(level);
                level++;
            }
            Pref.setValue(context, Config.LEVEL, level + "");
        } catch (Exception e) {
            Log.error(":: systemUpgrade() ::", e);
        } finally {
            if (dbHelper != null)
                dbHelper.close();
            dbHelper = null;
            level = 0;
        }
    }

    /**
     * Check Connectivity of network.
     */
    public static boolean isOnline(Context context) {
        try {
            if (context == null)
                return false;

            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (cm != null) {
                if (cm.getActiveNetworkInfo() != null) {
                    return cm.getActiveNetworkInfo().isConnected();
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.error("Exception", e);
            return false;
        }
    }

    /**
     * Customize fonts are used using this function.
     */
    public static Typeface getFont(Context context, int tag) {
        if (tag == 100) {
            return Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
        } else if (tag == 200) {
            return Typeface.createFromAsset(context.getAssets(), "Roboto-Medium.ttf");
        } else if (tag == 300) {
            return Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        } else if (tag == 400) {
            return Typeface.createFromAsset(context.getAssets(), "Roboto-Bold.ttf");
        } else if (tag == 500) {
            return Typeface.createFromAsset(context.getAssets(), "abril_fatface.ttf");
        }
        return Typeface.DEFAULT;
    }

    /**
     * Generate Device UDID.
     */
    public static void getDeviceID(Context context) {
        if (Pref.getValue(context, Config.PREF_UDID, "").equals("")) {
            String udid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            Pref.setValue(context, Config.PREF_UDID, udid);
        }
    }

    /**
     * EspProgressbar showProgress / HideProgress function.
     */
//    public static void showProgress(Context context, EspProgressbar progressBar, boolean isShow) {
//        if (isShow) {
//            progressBar.setCancelable(false);
//            progressBar.show();
//        } else {
//            if (progressBar != null) {
//                progressBar.dismiss();
//                progressBar = null;
//            }
//        }
//    }

    /**
     * No Internet Connection Alert.
     */
//    public static void noInternetConnection(Context context) {
//        if (Pref.getValue(context, Config.PREF_IS_INTERNET_DIALOG_OPEN, 0) == 0) {
//            Pref.setValue(context, Config.PREF_IS_INTERNET_DIALOG_OPEN, 1);
//            AlertDailogView.showAlert(context, getResourceSting(context, R.string.internetConnection),
//                    getResourceSting(context, R.string.connectionErrorMessage), getResourceSting(context, R.string.ok)).show();
//        }
//    }

    public final static Pattern Email_address_pattern = Pattern.compile("^[-a-zA-Z0-9~!$%^&*_=+}{\\'?]+(\\.[-a-zA-Z0-9~!$%^&*_=+}{\\'?]+)*@([a-zA-Z0-9_][-a-z0-9_]*(\\.[-a-zA-Z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-zA-Z][a-zA-Z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?$");
    public final static Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9.]*$");

    /*
    * Function free the memory.
    * */
    public static void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    /*
    * Common intent function to move from
    * one activity to another activity also
    * pass the bundle data.
    * */
    public static void intentCall(Activity activity, Class classname, Bundle bundle, boolean isFinish) {
        Intent intent = new Intent(activity, classname);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(intent);
        if (isFinish)
            activity.finish();
//        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /*
    * Common intent function to move from
    * one activity to another activity.
    * */
    public static void intentCall(Activity activity, Class classname, boolean isFinish) {
        Intent intent = new Intent(activity, classname);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(intent);
        if (isFinish)
            activity.finish();
//        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /*
    * Function which returns resources
    * */
    public static String getResourceSting(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    /*
    * Function which returns Host resources
    * */
//    public static String getHostResourceSting(Context context, int resId) {
//        return Utils.getResourceSting(context, R.string.apiHost) + Utils.getResourceSting(context, R.string.apiVersion) + Utils.getResourceSting(context, resId);
//    }

    /*
    * add activity stack function
    * */
    public static void addActivities(String actName, Activity _activity) {
        if (Config.screenStack == null)
            Config.screenStack = new HashMap<String, Activity>();
        if (_activity != null)
            Config.screenStack.put(actName, _activity);
    }

    /*
    * Remove activity stack function
    * */
    public static void removeActivity(String key) {
        if (Config.screenStack != null && Config.screenStack.size() > 0) {
            Activity _activity = Config.screenStack.get(key);
            if (_activity != null) {
                freeMemory();
                _activity.finish();
            }
        }
    }

    /*
   * Function which opens browser Intent
   * */
    public static void intentBrowser(Context context, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        if (url.startsWith("http://") || url.startsWith("https://"))
            i.setData(Uri.parse(url));
        else
            i.setData(Uri.parse("http://" + url));
        context.startActivity(i);
    }

//    public static void startPaypal(Context context, PayPalConfiguration config) {
//        Intent intent = new Intent(context, PayPalService.class);
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//        context.startService(intent);
//    }

//    public static void doPayment(Activity activity, PayPalConfiguration config, String eventName, String charge) {
//        PayPalPayment payment = new PayPalPayment(new BigDecimal(charge), "USD", eventName, PayPalPayment.PAYMENT_INTENT_SALE);
//        Intent intent = new Intent(activity, PaymentActivity.class);
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
//        activity.startActivityForResult(intent, 1089);
//    }

    public static int getLoginType(Context context) {
        return Pref.getValue(context, Config.PREF_ROLE, 0);
    }

    public static boolean clearPref(Context context, boolean removeAllData) {
        Pref.setValue(context, Config.PREF_IS_LOGIN, 0);
        Pref.setValue(context, Config.PREF_ROLE, 0);
        Pref.setValue(context, Config.PREF_USER_ID, 0);
        Pref.setValue(context, Config.PREF_FNAME, "");
        Pref.setValue(context, Config.PREF_LNAME, "");
        Pref.setValue(context, Config.PREF_EMAIL, "");
        Pref.setValue(context, Config.PREF_PASSWORD, "");
        Pref.setValue(context, Config.PREF_PROFILE_PIC, "");
        Pref.setValue(context, Config.PREF_FB_ID, "");
        Pref.setValue(context, Config.PREF_CREDITS, "");
        Pref.setValue(context, Config.PREF_BIO, "");
        Pref.setValue(context, Config.PREF_WEB_LINK, "");
        Pref.setValue(context, Config.PREF_FACEBOOK_LINK, "");
        Pref.setValue(context, Config.PREF_TWITTER_LINK, "");
        Pref.setValue(context, Config.PREF_LINKEDIN_LINK, "");
        Pref.setValue(context, Config.PREF_YOUTUBE_LINK, "");

        Pref.setValue(context, Config.PREF_SUBSCRIPTION_ID, "");
        Pref.setValue(context, Config.PREF_EXP_DATE, "");
        Pref.setValue(context, Config.PREF_PLAN_FOR, "");
        Pref.setValue(context, Config.PREF_DURATION, "");
        Pref.setValue(context, Config.PREF_DATE_CANCELLED, "");
        Pref.setValue(context, Config.PREF_IS_NEW, 0);
        Pref.setValue(context, Config.PREF_PLAN_TYPE, "");

//        if (removeAllData) {
//            CommonBll commonBll = new CommonBll(context);
//            for (int i = 1; i <= 4; i++) {
//                commonBll.deleteAllRecords(i);
//            }
//        }
        return true;
    }

    public static Date convertStringToDate(String strDate, String parseFormat) {
        try {
            return new SimpleDateFormat(parseFormat).parse(strDate);
        } catch (Exception e) {
            Log.error("convertStringToDate", e);
            return null;
        }
    }

    public static String convertDateToString(Date strDate, String parseFormat) {
        try {
            return new SimpleDateFormat(parseFormat).format(strDate);
        } catch (Exception e) {
            Log.error("convertDateToString", e);
            return null;
        }
    }

    /**
     * Convert Current String to String formate with apply new date formate
     * Function
     */
    public static String convertDateStringToString(String strDate, String currentFormat, String parseFormat) {
        try {
            return convertDateToString(convertStringToDate(strDate, currentFormat), parseFormat);
        } catch (Exception e) {
            Log.error("convertDateStringToString", e);
            return null;
        }
    }

    /**
     * Compare Two Date
     * Function
     * example : current date : 18 => (18,18) :true, (17,18) :true, (19,18) :false
     */
    public static boolean CheckDates(String startDate, String currDate) {
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
        boolean b = false;
        try {

            if (dfDate.parse(startDate).after(dfDate.parse(currDate))) {
                b = false;  // If start date is after end date.
            } else if (dfDate.parse(startDate).equals(dfDate.parse(currDate))) {
                b = true;  // If two dates are equal.
            } else if (dfDate.parse(startDate).before(dfDate.parse(currDate))) {
                b = true; // If start date is before the end date.
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return b;
        // if (liveEventsBean.avgRating > 0 && Utils.CheckDates(liveEventsBean.eventDate, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) {
    }

//    public static String getProfilePic(Context context) {
//        return Utils.getResourceSting(context, R.string.apiHostPic) + Pref.getValue(context, Config.PREF_PROFILE_PIC, "");
//    }
//
//    public static String getProfilePic(Context context, String profilePic) {
//        return Utils.getResourceSting(context, R.string.apiHostPic) + profilePic;
//    }
//
//    public static String getWebinarAudio(Context context, String audioPath) {
//        return Utils.getResourceSting(context, R.string.apiHostAudio) + audioPath;
//    }

    /*
   * Function which checks is image can crop
   * */
//    public static boolean isGotoCrop(Context context, Uri imgDestination, int minWidth, int minHeight) {
//        boolean isGoto = false;
//        try {
//            Bitmap bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imgDestination);
//            if (bm.getWidth() >= minWidth && bm.getHeight() >= minHeight) {
//                isGoto = true;
//
//            } else {
////                Toast.makeText(context, "Minimum image dimension must be " + minWidth + " X " + minHeight, Toast.LENGTH_LONG).show();
//                AlertDailogView.showAlert(context, Utils.getResourceSting(context, R.string.Alert), "Minimum image dimension must be " + minWidth + " X " + minHeight, Utils.getResourceSting(context, R.string.ok)).show();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return isGoto;
//    }

    /*
       * Function which checks photo orientation
       * */
    public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static void ConvertImage(Context context, Bitmap bitmap, String name) {
        try {
            File imageFile = new File(context.getApplicationInfo().dataDir, name);
            OutputStream os;
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
        }
    }

    public static Bitmap rotateBitmap(Bitmap b, int degrees) {

        Matrix m = new Matrix();
        if (degrees != 0) {
            // clockwise
            m.postRotate(degrees, (float) b.getWidth() / 2,
                    (float) b.getHeight() / 2);
        }
        try {
            Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                    b.getHeight(), m, true);
            if (b != b2) {
                b.recycle();
                b = b2;
            }
        } catch (OutOfMemoryError ex) {
            // We have no memory to rotate. Return the original bitmap.
        }
        return b;
    }

    public static boolean isValid(String urlString) {
        return URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches();
    }
}