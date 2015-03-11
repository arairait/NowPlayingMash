package net.fronoske.nowplayingmash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String action = intent.getAction();
        String cmd = intent.getStringExtra("command");
        Log.v("NowPlayingMash", action + " / " + cmd);
        String artist = intent.getStringExtra("artist");
        String album = intent.getStringExtra("album");
        String track = intent.getStringExtra("track");
        Log.v("NowPlayingMash", "(write) " + artist + " / " + album + " / " + track);
        // artist, album, trackを保存する http://www.tekboy.net/archives/995
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("artist", artist);
        editor.putString("album", album);
        editor.putString("track", track);
        editor.apply();
    }
}
