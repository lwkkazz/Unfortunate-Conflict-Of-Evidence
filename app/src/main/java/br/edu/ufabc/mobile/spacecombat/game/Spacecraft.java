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


public class Spacecraft extends GameEntity {

    private static final int STEP = 10;
    private static final String TAG = "SpacecraftController";
    private Bitmap craft;
    private Rect origin;
    private Rect scale;
    private InputStream is;

    private boolean easte;


    private int dir;
    int height, width, x, y;

    Spacecraft(Context ctx){

        easte = Params.EASTEREGGED;

        try{

            if(!easte) {
                is = ctx.getAssets().open("craft.png");
            }else{
                is = ctx.getAssets().open("android2.png");
            }

            craft   = BitmapFactory.decodeStream(is);

            height  =(int) (craft.getHeight()* Params.SCREEN_FACTOR);
            width   =(int) (craft.getWidth() * Params.SCREEN_FACTOR);

            origin = new Rect();
            origin.left     = 0;
            origin.right    = craft.getWidth();
            origin.top      = 0;
            origin.bottom   = craft.getHeight();

            scale = new Rect();
            scale.left   = Params.locationOnX(50, craft.getWidth()/2);
            scale.right  = scale.left + width;
            scale.top    = Params.locationOnY(95, craft.getHeight()/2);
            scale.bottom = scale.top + height;

        } catch (IOException e){
            Log.d(TAG, "Falha ao abrir craft.png");
        }
    }

    @Override
    public void update() {
        scale.left = dir;
        scale.right = scale.left + width;
        if(!easte) {
            x = scale.left + width / 2;
        }else{
            x = scale.left;
        }
        y = scale.top;
        setBox(scale);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(craft, origin, scale, null);
    }

    @Override
    public Rect getBox() {
        return super.getBox();
    }

    @Override
    public void setBox(Rect box) {
        super.setBox(box);
    }

    public void moveMe(int direction){

        if(scale.left>=0 && scale.right <= Params.SCREEN_WIDTH) {
            dir = scale.left - direction * STEP;

        }else if(scale.left<0){
                scale.left = 1;
                scale.right = scale.left + width;

        }else if(scale.right > Params.SCREEN_WIDTH){
                scale.right = Params.SCREEN_WIDTH-1;
                scale.left = scale.right - width;
        }
    }
}
