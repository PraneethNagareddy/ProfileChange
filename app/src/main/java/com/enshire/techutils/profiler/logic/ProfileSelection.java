package com.enshire.techutils.profiler.logic;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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

import com.enshire.techutils.profiler.R;
import com.enshire.techutils.profiler.dao.PreferencesDAO;
import com.enshire.techutils.profiler.helperclasses.StateDescriber;

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
                        modeDescription  = "Ring at "+seekBar.getProgress()+"% volume";
                        break;
                }
                if(dao.insertASilentProfile(new StateDescriber(mode,ssid))) {
                    Toast.makeText(context, "Preference for " + ssid + ":" + modeDescription, Toast.LENGTH_LONG).show();
                    //ConnectivityManager connMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    //NetworkInfo netInfo = connMan.getActiveNetworkInfo();
                    WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiMan.getConnectionInfo();
                   // Toast.makeText(context, "wifi:"+wifiInfo.getSSID(), Toast.LENGTH_SHORT).show();
                    if(ssid.equals("Default")
                            && wifiInfo.getSSID().equals("")){
                        applyMode(mode);
                    }
                    if(wifiInfo.getSSID().equals("\""+ssid+"\"")) {
                        applyMode(mode);
                    }
                }
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



    private void applyMode(int preferredMode){
        AudioManager audio_mngr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (preferredMode == 1) {
            audio_mngr.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        } else {
            if (preferredMode == 2) {
                audio_mngr.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else {
                if (preferredMode == 3) {
                    audio_mngr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                } else {
                    audio_mngr.setStreamVolume(AudioManager.STREAM_RING, (preferredMode - 3), AudioManager.FLAG_ALLOW_RINGER_MODES | AudioManager.FLAG_PLAY_SOUND);
                }
            }
        }
        //Toast.makeText(context, "Preferred Mode:" + preferredMode, Toast.LENGTH_SHORT).show();
    }


}






