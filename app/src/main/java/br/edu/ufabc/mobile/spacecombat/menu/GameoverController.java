package br.edu.ufabc.mobile.spacecombat.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import br.edu.ufabc.mobile.spacecombat.R;
import br.edu.ufabc.mobile.spacecombat.comm.LocalDatabaseTask;
import br.edu.ufabc.mobile.spacecombat.comm.Params;


public class GameoverController extends Activity {

    private LocalDatabaseTask dbTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.get_name);

        dbTask = new LocalDatabaseTask(this);

        Intent i = getIntent();

        Bundle bundle = i.getExtras();

        final int score =  bundle.getInt("score");

        final TextView scoreText = (TextView) findViewById(R.id.score_txt_game_over);
        final EditText nameText  = (EditText) findViewById(R.id.get_name_game_over);

        String temp = getResources().getString(R.string.your_score)+score;

        scoreText.setText(temp);

        ImageButton btn = (ImageButton) findViewById(R.id.buttonRecord);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getName = nameText.getText().toString();

                if(getName == null || getName == "")
                    getName = "---";


                dbTask.insertValue(getName, score);

                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putString("name", nameText.getText().toString());
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus)
            Params.setImmersion(getWindow(), this);
    }

}
