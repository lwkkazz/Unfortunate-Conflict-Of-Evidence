package br.edu.ufabc.mobile.spacecombat.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.io.IOException;
import java.io.InputStream;

import br.edu.ufabc.mobile.spacecombat.comm.Params;


public class Explosion extends GameEntity{

    private final String TAG = this.getClass().getCanonicalName();
    private Bitmap explosion;
    private Rect origin, scale;

    private int width, height, sprite, slow;
    private int x,y;
    public boolean isDone = false;

    Explosion(Context ctx, int x, int y) {

        this.x = x;
        this.y = y;

        try {
            InputStream is = ctx.getAssets().open("explosion.png");
            explosion = BitmapFactory.decodeStream(is);

            width = height = 144;

            slow = sprite = 0;

            origin = new Rect();
            origin.left = 0;
            origin.right = width;//origin.left + (int) (width * Params.SCREEN_FACTOR);
            origin.top = 0;
            origin.bottom = height;//origin.top + (int) (height * Params.SCREEN_FACTOR);

            scale = new Rect();
            scale.left = x;
            scale.right = scale.left + (int) (width * Params.SCREEN_FACTOR);
            scale.top = y;
            scale.bottom = scale.top + (int) (height * Params.SCREEN_FACTOR);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public void update() {
            origin.left = sprite * width;
            origin.right = origin.left + width;

            scale.left = x;
            scale.right = scale.left + (int) (width * Params.SCREEN_FACTOR);
            scale.top = y;
            scale.bottom = scale.top + (int) (height * Params.SCREEN_FACTOR);

            if (sprite == 16) {
                isDone = true;
            }
            spriteItUp();
        }

        public void draw(Canvas canvas) {
            canvas.drawBitmap(explosion, origin, scale, null);
        }

    private void spriteItUp(){
        if(slow == 8)
            slow=0;
        if(slow == 0) {
            sprite = (sprite + 1) % 18;

        }
        slow++;
    }
}

