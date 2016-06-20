package br.edu.ufabc.mobile.spacecombat.game;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.edu.ufabc.mobile.spacecombat.R;
import br.edu.ufabc.mobile.spacecombat.comm.Params;

public class GameView extends SurfaceView implements Runnable, SensorEventListener {

    private static final String TAG = "GameView";

    private Explosion  explosion;
    private Background back;
    private Spacecraft craft;


    private List<Shoot> shoot;
    private List<AsteroidSprite> astro;

    private Vibrator vibra;

    private Paint paint;
    private SurfaceHolder holder;
    private Thread thread;

    private SensorManager sensorManager;
    private Sensor senAccel;

    private MediaPlayer mPlayer1, mPlayer2;

    private SoundPool sound;
    private int shootID, explosionID, bangID;

    private int score = 0;
    private float asteroidsPerScreen = 8f;

    private long startTime, currentTime, startShotTime;
    private static boolean okToRun, hasEnded, isHard, hasSound, hasVibra, can=true;

    public GameView(Context ctx){
        super(ctx);
        init(ctx);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void init(Context ctx) {

        SharedPreferences sharedPrefs = ctx.getSharedPreferences(Params.SETTINGS, Context.MODE_PRIVATE);
        isHard      = sharedPrefs.getBoolean("isHard", false);
        hasSound    = sharedPrefs.getBoolean("hasSound", true);
        hasVibra    = sharedPrefs.getBoolean("hasVibra", true);

        if(hasVibra)
            vibra   =  (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);

        back    = new Background(ctx);
        craft   = new Spacecraft(ctx);
        astro = Collections.synchronizedList(new ArrayList<AsteroidSprite>());
        shoot = Collections.synchronizedList(new ArrayList<Shoot>());


        if(hasSound) {
            mPlayer1 = MediaPlayer.create(ctx, R.raw.sound1);
            mPlayer1.setLooping(true);
            mPlayer2 = MediaPlayer.create(ctx, R.raw.sound2);
            mPlayer2.setLooping(true);

            if ((android.os.Build.VERSION.SDK_INT) >= Build.VERSION_CODES.LOLLIPOP) {

                AudioAttributes aa = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .build();

                sound = new SoundPool.Builder()
                        .setMaxStreams(3)
                        .setAudioAttributes(aa)
                        .build();
                shootID     = sound.load(getContext(), R.raw.laser_shoot, 1);
                explosionID = sound.load(getContext(), R.raw.explosion, 1);
                bangID      = sound.load(getContext(), R.raw.bang, 1);

            } else {
                sound = new SoundPool(3, AudioManager.USE_DEFAULT_STREAM_TYPE, 0);
                shootID = sound.load(getContext(), R.raw.laser_shoot, 1);
            }
        }
        paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setAntiAlias(true);
        paint.setTextSize(40f);
        paint.setStrokeWidth(18);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

        sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        senAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, senAccel, SensorManager.SENSOR_DELAY_GAME);

        okToRun     = true;
        hasEnded    = false;
        holder = getHolder();
        thread = new Thread(this);
    }

    private void update() {
        systemUpdate();

        back.update();

        if(hasEnded) {
            explosion.update();
        }else{
            craft.update();
        }

        for(AsteroidSprite aero:astro){
            if(Params.trigger(aero.getBox(), craft.getBox())){
                if(!hasEnded)
                    endRoutine();
            }
            for(Shoot tiro:shoot){
                if(Params.trigger(tiro.getBox(), aero.getBox())){
                    tiro.setIsValid(false);
                    aero.setIsValid(false);

                    //shoot.remove(tiro);
                    //astro.remove(aero);

                    if(hasSound)
                        sound.play(bangID, 1,1,1,0,1);

                    if(isHard) {
                        score += 10;
                    }else {
                        score += 20;
                    }
                }
            }
        }


        for (Shoot tiro:shoot) {
            if (tiro.isValid()) {
                tiro.update();
            } else {
                tiro.setIsValid(false);
                //shoot.remove(tiro);
            }
        }
        for (AsteroidSprite aerolito: astro) {
            if (aerolito.isValid()) {
                aerolito.update();
            } else {
                aerolito.setIsValid(false);
                //astro.remove(aerolito);
            }
        }

        removeMe();
    }


    private void removeMe() {

        synchronized (astro) {
            Iterator<AsteroidSprite> i = astro.iterator();
            while (i.hasNext()) {
                AsteroidSprite aero = i.next();
                if (!aero.isValid())
                    i.remove();
            }
        }

        synchronized (shoot) {
            Iterator<Shoot> i = shoot.iterator();
            while (i.hasNext()) {
                Shoot tiro = i.next();
                if (!tiro.isValid())
                    i.remove();
            }
        }
    }

    private void drawCalls(Canvas canvas){

        back.draw(canvas);

        if(hasEnded) {
            explosion.draw(canvas);
        }else{
            craft.draw(canvas);
        }

        for(Shoot tiro:shoot)
            tiro.draw(canvas);

        for (AsteroidSprite aerolito : astro)
            aerolito.draw(canvas);

        uiDraw(canvas);
    }

    private void systemUpdate(){

        float temp = getAsteroidsRate(score, asteroidsPerScreen);

        if(temp!=0)
            Log.d(TAG, "Temp: "+temp);

        if(temp>0)
            asteroidsPerScreen = temp;

        if(((currentTime-startTime)/100000000>asteroidsPerScreen-0.5)){
            if(asteroidsPerScreen>0) {
                astro.add(new AsteroidSprite(getContext(), isHard));
                startTime = System.nanoTime();
            }
        }
        currentTime = System.nanoTime();
    }

    private void uiDraw(Canvas canvas){
        canvas.drawText("Score: " + score, Params.locationOnX(5, 18), Params.locationOnY(5, 18), paint);
    }

    private void endRoutine() {

        explosion = new Explosion(getContext(), craft.x, craft.y);
        if(hasSound)
            sound.play(explosionID,1,1,1,0,1);

        if(hasVibra)
            vibra.vibrate(500);

        hasEnded = true;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                onGameOver();
            }
        }, 3000);
    }

    private void onGameOver(){
        Intent intent = ((Activity) getContext()).getIntent();
        Bundle bundle = new Bundle();

        if(isHard) {
            bundle.putInt("score", 2 * score);
        }else{
            bundle.putInt("score", score);
        }

        intent.putExtras(bundle);
        ((Activity) getContext()).setResult(Activity.RESULT_OK, intent);
        ((Activity) getContext()).finish();


    }

    private float getAsteroidsRate(int param, float actual) {

        if (param % 4 == 2) {
            can = true;
        }

        if ((param % 100 == 0) && (param != 0) && can) {

            if (!isHard) {
                actual -= 0.35f;
            } else {
                actual -= 0.5f;
            }

            Log.d(TAG, "REDUCING");
            can = false;

            return actual;
        } else {
            return 0;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int)event.getY();

        if(y<Params.locationOnY(10,0))
            score = 250;


        if (!hasEnded){
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if ((currentTime - startShotTime) / 100000000 > 1) {
                    shoot.add(new Shoot(getContext(), craft.y, craft.x));
                    if(hasSound)
                        sound.play(shootID,1,1,1,0,1);
                    startShotTime = System.nanoTime();
                }
            }
        }
        return false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int x = (int)event.values[0];
            craft.moveMe(x);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void pause(){
        okToRun = false;
            try {
                sensorManager.unregisterListener(this);
                thread.join();
            }catch (Exception e){
                e.printStackTrace();
            }


        if(hasSound) {
            mPlayer1.pause();
            mPlayer2.pause();
        }
        thread = null;
    }

    public void stop(){
        if(hasSound) {
            mPlayer1.reset();
            mPlayer1.stop();
            mPlayer1.release();
            mPlayer1 = null;

            mPlayer2.reset();
            mPlayer2.stop();
            mPlayer2.release();
            mPlayer2 = null;
        }
    }

    public void resume(){
        okToRun = true;
        sensorManager.registerListener(this, senAccel, SensorManager.SENSOR_DELAY_GAME);

        if(hasSound) {
            mPlayer1 = MediaPlayer.create(getContext(), R.raw.sound1);
            mPlayer1.setLooping(true);
            mPlayer1.start();

            mPlayer2 = MediaPlayer.create(getContext(), R.raw.sound2);
            mPlayer2.setLooping(true);
            mPlayer2.start();
        }

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        if(hasSound) {
            mPlayer1.start();
            mPlayer2.start();
        }
        startTime = System.nanoTime();
        while (okToRun){
            while (true){
                if(!holder.getSurface().isValid()) {
                    break;
                }else{
                    Canvas canvas = holder.lockCanvas();
                    update();
                    drawCalls(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }
                break;
            }
        }
    }
}