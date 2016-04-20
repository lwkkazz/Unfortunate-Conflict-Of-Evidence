package br.edu.ufabc.mobile.spacecombat.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import br.edu.ufabc.mobile.spacecombat.R;
import br.edu.ufabc.mobile.spacecombat.game.GameActivity;
import br.edu.ufabc.mobile.spacecombat.comm.Params;

public class MenuController extends Activity {

    private static final int GAME_OVER  = 1;
    private static final int GET_NAME   = 2;
    private static final String TAG = "MENU_CONTROLLER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Params.setImmersion(getWindow(), this);

        setContentView(R.layout.menu_screen);

    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    public void startGame(View view){
        Intent i = new Intent(this, GameActivity.class);
        startActivityForResult(i, GAME_OVER);
    }

    public void leaderBoards(View view){
        Intent i = new Intent(this, LeaderboardsController.class);
        startActivity(i);

    }

    public void helpMe(View view){
        AlertDialog.Builder popup = new AlertDialog.Builder(this);

        popup.setTitle(R.string.menu_help);
        popup.setMessage(R.string.menu_help_msg);
        popup.setCancelable(true);
        popup.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
            }
        });
        popup.create();
        popup.show();
    }

    public void setSettings(View view){
        Intent i = new Intent(this, SettingsController.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GAME_OVER) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                int score = bundle.getInt("score");
                Intent i = new Intent(this, GameoverController.class);
                i.putExtra("score", score);
                startActivityForResult(i, GET_NAME);
            }
        } else if(requestCode == GET_NAME) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Pontuação guardada com sucesso!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus)
            Params.setImmersion(getWindow(), this);
    }
}
