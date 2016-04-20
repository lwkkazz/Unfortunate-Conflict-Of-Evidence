package br.edu.ufabc.mobile.spacecombat.comm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocalDatabaseTask extends SQLiteOpenHelper{

    private static final String DB_NAME = "RANKING";
    private static final int DB_VERS = 1;

    public static final String KEY_ROWID = "_id";

    public static final String KEY_PLAYER_NAME = "player_name";
    public static final String KEY_PLAYER_SCORE = "player_score";

    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_PLAYER_NAME, KEY_PLAYER_SCORE};

        public LocalDatabaseTask(Context ctx){
        super(ctx, DB_NAME, null,DB_VERS);
        
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            String createTable ="CREATE TABLE "+ DB_NAME +"(" +KEY_ROWID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
                                KEY_PLAYER_NAME+ " VARCHAR(40) NOT NULL," +
                                KEY_PLAYER_SCORE+" INTEGER NOT NULL);";
            db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTable = "DROP TABLE IF EXISTS " + DB_NAME;

        db.execSQL(dropTable);

        onCreate(db);
    }

    public void insertValue(String name, int score){

        SQLiteDatabase dbWriter = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_PLAYER_NAME,name);
        contentValues.put(KEY_PLAYER_SCORE,score);

        dbWriter.insert(DB_NAME, null, contentValues);
        dbWriter.close();
    }


    public Cursor listAll(){

        SQLiteDatabase dbReader = getReadableDatabase();
        Cursor cursor   =   dbReader.query(DB_NAME, ALL_KEYS, null, null, null, null, KEY_PLAYER_SCORE+ " DESC");

        if(cursor.getCount() == 0) {

            Log.d("DB_TASKS","Query returned no results");

            cursor = null;
        }

        if(cursor != null) {
            cursor.moveToFirst();
        }else
            return null;

        return cursor;
    }
}
