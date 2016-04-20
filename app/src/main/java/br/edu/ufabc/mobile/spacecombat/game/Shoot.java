package br.edu.ufabc.mobile.spacecombat.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import br.edu.ufabc.mobile.spacecombat.comm.Params;

public class Shoot extends GameEntity {

    private static final String TAG = "ShootController";
    private static final int STEP = 30;

    private Bitmap shoot;
    private Rect origin, scale;
    private InputStream is;

    private Random rand;

    int width, height, x, y;

    private boolean isValid = true;

    Shoot (Context ctx, int y, int x){

        rand = new Random();

        try{

            if(rand.nextBoolean()) {
                is = ctx.getAssets().open("shoot1.png");
            }else{
                is = ctx.getAssets().open("shoot2.png");
            }

            shoot   = BitmapFactory.decodeStream(is);

            origin = new Rect();
            origin.left     = 0;
            origin.right    = shoot.getWidth();
            origin.top      = 0;
            origin.bottom   = shoot.getHeight();

            width = (int)(shoot.getWidth() * Params.SCREEN_FACTOR);
            height= (int)(shoot.getHeight()*Params.SCREEN_FACTOR);

            scale = new Rect();
            scale.bottom    = y;
            scale.left      = x;
            scale.right     = scale.left + width;
            scale.top       = scale.bottom - height;
            setIsValid(true);


        }catch (IOException e){
            Log.d(TAG, "Falha ao abrir shoot.png");
        }
    }

    @Override
    public void update(){
            scale.top -= STEP * Params.SCREEN_FACTOR;
            scale.bottom -= STEP * Params.SCREEN_FACTOR;

            x = scale.left + width / 2;
            y = scale.top + height / 2;

            if(scale.bottom<0)
                setIsValid(false);

            setBox(scale);
    }

    @Override
    public void draw(Canvas canvas) {

        canvas.drawBitmap(shoot, origin, scale, null);
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void setIsValid(boolean isValid) {
        super.setIsValid(isValid);
        this.isValid = isValid;
    }

    @Override
    public Rect getBox() {
        return super.getBox();
    }

    @Override
    public void setBox(Rect box) {
        super.setBox(box);
    }
}
