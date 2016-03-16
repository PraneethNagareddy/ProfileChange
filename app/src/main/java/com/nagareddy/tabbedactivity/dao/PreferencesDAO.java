package com.nagareddy.tabbedactivity.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.AudioManager;

import com.nagareddy.tabbedactivity.helperclasses.StateDescriber;

import java.util.ArrayList;

/**
 * Created by praneeth on 27/09/2015.
 */
public class PreferencesDAO extends SQLiteOpenHelper{
    Context context;
    public static final String DATABASE_NAME = "Preferences.db";

    public PreferencesDAO(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
        this.context = context;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        //db.execSQL("DROP TABLE IF EXISTS preferences");
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        final int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        db.execSQL(
                "create table preferences " +
                        "(id integer primary key, ssid text,mode number)"
        );
        db.execSQL("INSERT INTO preferences VALUES (1,'Default',"+maxVol+")");
    }

    public int getPreferenceof(String ssid){
        int returnValue=-1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select mode from preferences WHERE ssid = '"+ssid+"'", null);
        res.moveToFirst();
        if(res.getCount()>0)
            returnValue = res.getInt(res.getColumnIndex("mode"));
        return returnValue;
    }

    public ArrayList<StateDescriber> getSilentProfiles() {
        ArrayList<StateDescriber> list=new ArrayList<StateDescriber>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from preferences", null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            list.add(new StateDescriber(res.getInt(res.getColumnIndex("mode")),res.getString(res.getColumnIndex("ssid"))));
            res.moveToNext();
        }
        return list;
    }

    public ArrayList<String> getAllSSIDs() {
        ArrayList<String> list=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from preferences", null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            list.add(res.getString(res.getColumnIndex("ssid")));
            res.moveToNext();
        }
        return list;
    }

    public  boolean insertASilentProfile(StateDescriber state)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ssid", state.getSsid());
        contentValues.put("mode", state.getMode());
        Cursor res =  db.rawQuery( "select * from preferences where ssid='"+state.getSsid()+"'", null );
        if( (res.getCount()>0)) {
            db.update("preferences", contentValues, " ssid = '" + state.getSsid() + "'", null);
           // Toast.makeText(context, "Updated "+state.getSsid()+" to mode to:"+getPreferenceof(state.getSsid()), Toast.LENGTH_LONG).show();
        } else
            db.insert("preferences", null, contentValues);
        return true;
    }

}
