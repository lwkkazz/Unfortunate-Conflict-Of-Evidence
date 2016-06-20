package br.edu.ufabc.mobile.spacecombat.game;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import br.edu.ufabc.mobile.spacecombat.comm.Params;

public class Background extends GameEntity {

    private static final String TAG = "BackgroundController";
    private static final int STEP = -10;

    int height, width;

    private Bitmap background;
    private Rect origin, first, second;

    Background(Context ctx){

        try{
            InputStream is = ctx.getAssets().open("back.png");
            background  = BitmapFactory.decodeStream(is);

            Params.SCREEN_FACTOR = Params.SCREEN_HEIGHT / (float) background.getHeight();

            height  = background.getHeight();
            width   = background.getWidth();

            origin = new Rect();
            origin.left     = 0;
            origin.right    = width;
            origin.top      = 0;
            origin.bottom   = height;

            first   = new Rect();

            first.left      = 0;
            first.right     = (int)(width * Params.SCREEN_FACTOR);
            first.top       = 0;
            first.bottom    = (int)(height * Params.SCREEN_FACTOR);

            second = new Rect();
            second.left     = 0;
            second.right    =(int)(width * Params.SCREEN_FACTOR);
            second.top      = - Params.SCREEN_HEIGHT;
            second.bottom   = first.top;

        }catch (IOException e){
            e.printStackTrace();
            Log.d(TAG, "Falha ao abrir arquivo");
        }
    }

    @Override
    public void update(){
        if (second.top<Params.SCREEN_HEIGHT){
            second.top      -= STEP;
            second.bottom   -= STEP;
        }else{
            second.top      = -Params.SCREEN_HEIGHT;
            second.bottom   = first.top;
        }

        if(first.top<Params.SCREEN_HEIGHT){
            first.top       -= STEP;
            first.bottom    -= STEP;

        }else{
            first.top       = -Params.SCREEN_HEIGHT;
            first.bottom    = second.top;
        }
    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawRGB(13,10,7);
        canvas.drawBitmap(background, origin, first, null);
        canvas.drawBitmap(background, origin, second, null);
    }
}
