package br.edu.ufabc.mobile.spacecombat.game;

import android.graphics.Canvas;
import android.graphics.Rect;

public abstract class GameEntity {

    private Rect box;
    private boolean isValid;

    abstract void update();

    abstract void draw(Canvas canvas);

    public boolean isValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public Rect getBox() {
        return box;
    }

    public void setBox(Rect box) {
        this.box = new Rect();
        this.box.left = box.left;
        this.box.right = box.right;
        this.box.top = box.top;
        this.box.bottom = box.bottom;
    }
}
