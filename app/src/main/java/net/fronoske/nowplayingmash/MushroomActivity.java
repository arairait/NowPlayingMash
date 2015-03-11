package net.fronoske.nowplayingmash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MushroomActivity extends ActionBarActivity {

    private static final String ACTION_INTERCEPT = "com.adamrocker.android.simeji.ACTION_INTERCEPT";
    private static final String REPLACE_KEY = "replace_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String artist = prefs.getString("artist", "");
        String album = prefs.getString("album", "");
        String track = prefs.getString("track", "");
        String template = prefs.getString("template", "%a / %l / %t");
        String result = template.replaceAll("%a", artist).replaceAll("%l", album).replaceAll("%t", track);

        Intent it = getIntent();
        String action = it.getAction();
        if (action != null && ACTION_INTERCEPT.equals(action)) { /* Simejiから呼出された時 */
            Intent data = new Intent();
            data.putExtra(REPLACE_KEY, result);
            setResult(RESULT_OK, data);
            finish();
        } else {
            setContentView(R.layout.activity_mushroom);
            EditText editTemplate = (EditText) findViewById(R.id.editTemplate);
            editTemplate.setText(template);
            editTemplate.setOnEditorActionListener(new TextView.OnEditorActionListener(){
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        Log.v("NowPlayingMash", "input [Done]");
                        saveTemplate(v);
                        return true;
                    }
                    return false;
                }
            });
            this.onResume();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 現在の値を表示
        SharedPreferences prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String artist = prefs.getString("artist", "");
        String album = prefs.getString("album", "");
        String track = prefs.getString("track", "");
        String template = prefs.getString("template", "%a / %l / %t");
        String result = template.replaceAll("%a", artist).replaceAll("%l", album).replaceAll("%t", track);

        TextView textArtist = (TextView) findViewById(R.id.artistName);
        textArtist.setText(artist);
        TextView textAlbum = (TextView) findViewById(R.id.albumName);
        textAlbum.setText(album);
        TextView textTrack = (TextView) findViewById(R.id.trackName);
        textTrack.setText(track);
        TextView textResult = (TextView) findViewById(R.id.resultString);
        textResult.setText(result);
        Log.v("NowPlayingMash", "(read) " + artist + " / " + album + " / " + track);
    }

    public void onBackgroundClick(View v){
        Log.v("NowPlayingMash", "on Background Click");
        saveTemplate(v);
        RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        mainLayout.requestFocus();
    }

    private void saveTemplate(View v){
        EditText editTemplate = (EditText) findViewById(R.id.editTemplate);
        String template = editTemplate.getText().toString();
        //ソフトキーボードを閉じる
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        //処理
        SharedPreferences prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String oldTemplate = prefs.getString("template", "");
        if (!oldTemplate.equals(template)){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("template", template);
            editor.apply();
            Log.v("NowPlayingMash", "(write template) " + template);
            Toast.makeText(MushroomActivity.this, "保存しました", Toast.LENGTH_SHORT).show();
            onResume();
        }
    }
}
