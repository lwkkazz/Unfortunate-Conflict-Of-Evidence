package br.edu.ufabc.mobile.spacecombat.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import br.edu.ufabc.mobile.spacecombat.R;
import br.edu.ufabc.mobile.spacecombat.comm.RemoteTimeTask;
import br.edu.ufabc.mobile.spacecombat.comm.Params;

public class SplashController extends Activity {

    protected void onCreate(Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        Params.setImmersion(getWindow(), this);
        setUpParams();
        setContentView(R.layout.splash_screen);

        Thread thread = new Thread(){

            public void run(){
                try {
                    Thread.sleep(Params.TIME_OUT);
                    Intent i = new Intent(getApplicationContext(), MenuController.class);
                    startActivity(i);
                    finish();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void setUpParams(){

        if(Params.isConnected(this)) {
            new RemoteTimeTask().execute(Params.URL);
        }else{
            Params.EASTEREGGED = false;
        }

        DisplayMetrics metrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);

        Params.SCREEN_HEIGHT = metrics.heightPixels;
        Params.SCREEN_WIDTH = metrics.widthPixels;
    }
}
