package br.edu.ufabc.mobile.spacecombat.game;

import android.app.Activity;
import android.os.Bundle;

import br.edu.ufabc.mobile.spacecombat.comm.Params;


public class GameActivity extends Activity{

    private static final String TAG = "GameActivity";
    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Params.setImmersion(getWindow(), this);
        gameView = new GameView(this);
        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        gameView.stop();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus)
            Params.setImmersion(getWindow(), this);
    }
}
