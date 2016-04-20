package br.edu.ufabc.mobile.spacecombat.menu;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import br.edu.ufabc.mobile.spacecombat.R;
import br.edu.ufabc.mobile.spacecombat.comm.Params;


public class SettingsController extends Activity{

    Switch diffSwitch, soundSwitch, vibraSwitch;
    SharedPreferences sharedPref;
    SharedPreferences.Editor  editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Params.setImmersion(getWindow(), this);
        sharedPref = getSharedPreferences(Params.SETTINGS, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        setContentView(R.layout.settings);

        diffSwitch  = (Switch) findViewById(R.id.diff_sett_switch);
        soundSwitch = (Switch) findViewById(R.id.sound_sett_switch);
        vibraSwitch = (Switch) findViewById(R.id.vibra_conff_switch);

        diffSwitch.setChecked(sharedPref.getBoolean("isHard", false));
        soundSwitch.setChecked(sharedPref.getBoolean("hasSound", false));
        vibraSwitch.setChecked(sharedPref.getBoolean("hasVibra", false));


        diffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    editor.putBoolean("isHard", true);
                }else{
                    editor.putBoolean("isHard", false);
                }
            }
        });


        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    editor.putBoolean("hasSound", true);
                }else{
                    editor.putBoolean("hasSound", false);
                }
            }
        });

        vibraSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    editor.putBoolean("hasVibra", true);
                }else{
                    editor.putBoolean("hasVibra", false);
                }
            }
        });
    }

    public void onAccept(View view){
        editor.commit();

        Params.isHard   = sharedPref.getBoolean("isHard",false);
        Params.hasSound = sharedPref.getBoolean("hasSound", true);
        Params.hasVibra = sharedPref.getBoolean("hasVibra",true);

        finish();
    }

    public void onCancel(View view){
        editor.clear();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
