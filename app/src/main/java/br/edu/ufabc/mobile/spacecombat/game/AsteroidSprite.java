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

public class AsteroidSprite extends GameEntity {

    private static final String TAG = "AsteroidController";
    private static final int STEP = 20;

    private Bitmap asteroid;
    private Random rand;
    private InputStream is;

    private Rect origin;
    private Rect scale;

    private Rect box;
    private int slow, sprite;

    private float rotate=0;

    int height, width, x, y;
    private int random;

    private boolean isValid = true, isHard, Easteregg;

    AsteroidSprite(Context ctx, boolean isHard){
        try{
            Easteregg = Params.EASTEREGGED;
            this.isHard = isHard;


            if(!Easteregg) {
                 is = ctx.getAssets().open("asteroid_sprite.png");
            }
            else{
                is = ctx.getAssets().open("apple.png");
            }

            asteroid    = BitmapFactory.decodeStream(is);

            rand = new Random();

            slow = sprite = 0;

            height = width = 72;

            origin = new Rect();
            origin.left     = 0;
            origin.right    = width;
            origin.top      = 0;
            origin.bottom   = width;

            scale   = new Rect();
            box = new Rect();

            scale.left  = Params.locationOnX((rand.nextInt(95)+2), width /2);
            scale.right = (int) ( scale.left + width*2*Params.SCREEN_FACTOR);
            scale.top   = Params.locationOnY(0, height /2);
            scale.bottom= (int) ( scale.top + width*2*Params.SCREEN_FACTOR);

            random = rand.nextInt(10)-5;

            Log.d(TAG, "I'm Alive!!!!");

            setIsValid(true);


        }catch (IOException e){
            Log.e(TAG, "Falha ao abrir arquivo");
        }
    }

    @Override
    public void update() {

        setBox(scale);

        if(!Easteregg) {
            origin.left = sprite * width;
            origin.right = origin.left + width;
        }

        scale.left += random;
        scale.right += random;

        if(isHard) {
            scale.top += STEP * 1.3f * Params.SCREEN_FACTOR;
            scale.bottom += STEP * 1.3f * Params.SCREEN_FACTOR;
        }else{
            scale.top += STEP * Params.SCREEN_FACTOR;
            scale.bottom += STEP * Params.SCREEN_FACTOR;
        }

        x = scale.left + width / 2;
        y = scale.top + height / 2;


        if(scale.left>Params.SCREEN_WIDTH)
            setIsValid(false);

        if(scale.right<0)
            setIsValid(false);

        if(scale.top>Params.SCREEN_HEIGHT)
            setIsValid(false);


        spriteItUp();

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(asteroid, origin, scale, null);
        //canvas.rotate(rotate, width/2, height/2);
    }

    private void spriteItUp(){


        if(slow == 4){
            slow=0;
        }

        if(slow == 0) {
            if(Easteregg){
                rotate = rotate + 18.9f;
            }else
                sprite = (sprite + 1) % 19;

        }
        slow++;
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void setIsValid(boolean isValid) {
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
