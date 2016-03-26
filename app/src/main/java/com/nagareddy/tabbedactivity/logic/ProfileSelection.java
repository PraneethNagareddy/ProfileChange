package com.nagareddy.tabbedactivity.logic;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.nagareddy.tabbedactivity.R;
import com.nagareddy.tabbedactivity.dao.PreferencesDAO;
import com.nagareddy.tabbedactivity.helperclasses.StateDescriber;

public class ProfileSelection extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    SeekBar seekBar1;
    Button button;
    RadioGroup radioGroup;
    PreferencesDAO dao;
    String ssid;
    SeekBar seekBar;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        final int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        setContentView(R.layout.activity_profile_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        button = (Button) findViewById(R.id.savebutton);
        ssid=getIntent().getStringExtra("ssid");
        dao = new PreferencesDAO(this);
        int preferenceMode = dao.getPreferenceof(ssid);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        switch(preferenceMode){
            case 1 : radioGroup.check(R.id.vibrate);
                break;
            case 2 : radioGroup.check(R.id.silent);
                break;
            default: radioGroup.check(R.id.ring);
                //set seekbar to the value
                seekBar.setProgress((preferenceMode-3)*100/maxVol);
                break;
            case -1:
            case 3 : radioGroup.check(R.id.ring);
                seekBar.setProgress(100);
                break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
             if(checkedId==R.id.ring) {

                seekBar1.setEnabled(true);
             } else
             {
                 seekBar1.setEnabled(false);
             }

            }
        });




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mode=-1;
                String modeDescription="";
                int checked = radioGroup.getCheckedRadioButtonId();
                switch(checked){
                    case R.id.silent : mode = 2;
                        modeDescription = "Silence";
                        break;
                    case R.id.vibrate : mode = 1;
                        modeDescription = "Vibrate";
                        break;
                    case R.id.ring :
                        mode = Math.round(seekBar.getProgress() * maxVol/100)+3;
                        modeDescription  = "Ring at "+((mode/maxVol)*100)+"% volume";
                        break;
                }
                if(dao.insertASilentProfile(new StateDescriber(mode,ssid)))
                    Toast.makeText(context, "Preference for "+ssid+":"+modeDescription, Toast.LENGTH_LONG).show();
            }
        });

        seekBar1 = (SeekBar) findViewById(R.id.seekbar);
        seekBar1.setOnSeekBarChangeListener(ProfileSelection.this);



        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

    }

    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.home) {
            Intent productIntent = new Intent(this, Tabs.class);
            productIntent.setFlags( productIntent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(productIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}






