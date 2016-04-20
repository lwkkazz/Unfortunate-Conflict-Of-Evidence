package br.edu.ufabc.mobile.spacecombat.comm;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.Window;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Params {

    public static final String SETTINGS = "my_settings";
    private static final String TAG = "PARAMS";

    public static int SCREEN_HEIGHT;
    public static int SCREEN_WIDTH;

    public static boolean EASTEREGGED;

    public static int TIME_OUT = 1000;

    public static float SCREEN_FACTOR;

    public static boolean isHard;
    public static boolean hasSound;
    public static boolean hasVibra;

    public static String URL    = "http://time.jsontest.com";

    private static Calendar begin(){
        try {
            String temp = "09:41:00";
            Date time = new SimpleDateFormat("HH:mm:ss").parse(temp);
            Calendar begin = Calendar.getInstance();
            begin.setTime(time);

            return begin;
        }catch (ParseException ex){}
        return null;
    }

    private static Calendar finish(){
        try {
            String temp = "11:00:00";
            Date time = new SimpleDateFormat("HH:mm:ss").parse(temp);
            Calendar finish = Calendar.getInstance();
            finish.setTime(time);
            return finish;
        }catch (ParseException ex){}
        return null;
    }

    public static boolean checkEasteregg(Date time){

        if(time != null) {
            Calendar now = Calendar.getInstance();
            now.setTime(time);

            Calendar begin = begin();
            Calendar finish = finish();

            Log.d(TAG, "Now:    " + now);
            Log.d(TAG, "Begin:  " + begin);
            Log.d(TAG, "Finish: " + finish);

            return (time.after(begin.getTime()) && time.before(finish.getTime()));
        }else{
            return false;
        }
  }

    public static int locationOnX(int x, int offset){
        if(x<0)
            x=1;
        if(x>100)
            x=99;
        return (SCREEN_WIDTH/100 * x)-offset;
    }

    public static int locationOnY(int y, int offset){
        if(y<0)
            y=1;
        if(y>100)
            y=99;
        return (SCREEN_HEIGHT/100 * y)-offset;
    }

    public static boolean trigger(Rect obj1, Rect obj2){

        int obj1Left, obj1Right, obj1Top, obj1Bottom;
        int obj2Left, obj2Right, obj2Top, obj2Bottom;

        if((obj1!=null)&&(obj2!=null)) {
            //Log.d("PARAMS", "Testing things.");

            obj1Left = obj1.left;
            obj1Right = obj1.right;
            obj1Top = obj1.top;
            obj1Bottom = obj1.bottom;

            obj2Left = obj2.left;
            obj2Right = obj2.right;
            obj2Top = obj2.top;
            obj2Bottom = obj2.bottom;


            return (obj1Left   <= obj2Right  &&
                    obj1Right  >= obj2Left   &&
                    obj1Top    <= obj2Bottom &&
                    obj1Bottom >= obj2Top);
        }//else{
        //    Log.d("PARAMS", "Objects are null");
           return false;
        //}

    }

    public static void setImmersion(Window window, Activity activity){

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                                                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                                    |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                                    |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public static boolean isConnected(Context ctx){
        ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }
}
