package br.edu.ufabc.mobile.spacecombat.menu;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import br.edu.ufabc.mobile.spacecombat.R;
import br.edu.ufabc.mobile.spacecombat.comm.LocalDatabaseTask;
import br.edu.ufabc.mobile.spacecombat.comm.Params;


public class LeaderboardsController extends Activity {

    private LocalDatabaseTask dbTask;
    private Cursor cursor;
    private ListView ranking;

    private static final String[] ROW_ID= {"Jogadores","Score"};
    private static final int[]  ROW_ORG = {android.R.id.text1,android.R.id.text1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Params.setImmersion(getWindow(), this);

        setContentView(R.layout.ranking_screen);

        if(Params.isConnected(this)){
            Toast.makeText(this, "Is Connected!",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "No Connection!",Toast.LENGTH_SHORT).show();
        }

        //TextView txtView = (TextView) findViewById(R.id.rank_title_text);
        int[] textViews = {R.id.rank_title_text,
                            R.id.rank_title_text};

        dbTask = new LocalDatabaseTask(this);

        String[] fromFieldNames = {dbTask.KEY_PLAYER_NAME, dbTask.KEY_PLAYER_SCORE};
        int[]   toViewIds       = {R.id.textView3, R.id.textView4};

        cursor = dbTask.listAll();

        if (cursor==null)
            Log.d("LEADERBOARS", "Cursor has no values");

        ranking = (ListView) findViewById(R.id.rank_list_view);
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(getApplicationContext(),
                                                                R.layout.list_layout,
                                                                cursor,
                                                                fromFieldNames,
                                                                toViewIds,
                                                                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        ranking.setAdapter(mAdapter);

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus)
            Params.setImmersion(getWindow(), this);
    }
}