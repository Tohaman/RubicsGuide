package ru.tohaman.rubicsguide;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by Toha on 10.05.2017.
 */

public class YouTubeActivity extends Activity
{
//        private static String VIDEO_ID = "0TvO_rpG_aM";

    public static final int REQ_START_STANDALONE_PLAYER = 101;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;
    private Date date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Проверяем значения из настроек, выключать экран или нет при прсмотре видео
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean  sleep_youtube = sp.getBoolean("videoscreen_on", false);
        if (sleep_youtube) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }  else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

/**
 В активность передается ссылка на видео и время с которого надо воспроизводить видео в виде
 <a href="rubic_activity://ytactivity?time=1:15&link=Vt9dHndW7-E">Time 1:15</a>
 в манифесте надо для активности прописать <intent-filter>
 <data android:host="ytactivity" android:scheme="test_activity" />
 а манифест перед <application добавить строку
 <uses-permission android:name="android.permission.INTERNET" />
 */
        String Text1 = getIntent().getData().getQueryParameter("time");
        String VIDEO_ID = getIntent().getData().getQueryParameter("link");
        Intent intent = null;
        intent = YouTubeStandalonePlayer.createVideoIntent(this, DeveloperKey.DEVELOPER_KEY, VIDEO_ID, StringToTimeMillis(Text1), true, true);
        if (intent !=null) {
            if (canResolveIntent(intent)) {
                startActivityForResult(intent,REQ_START_STANDALONE_PLAYER);
            } else {
                YouTubeInitializationResult.SERVICE_MISSING.getErrorDialog(this,REQ_RESOLVE_SERVICE_MISSING).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_START_STANDALONE_PLAYER && resultCode != RESULT_OK) {
            YouTubeInitializationResult errorReason =
                    YouTubeStandalonePlayer.getReturnedInitializationResult(data);
            if (errorReason.isUserRecoverableError()) {
                errorReason.getErrorDialog(this, 0).show();
            } else {
                String errorMessage =
                        String.format(getString(R.string.error_player), errorReason.toString());
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == REQ_START_STANDALONE_PLAYER && resultCode == RESULT_OK) {finish();}
    }

    private boolean canResolveIntent(Intent intent) {
        List<ResolveInfo> resolveInfo = this.getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }

    private int StringToTimeMillis (String text){
        Calendar calendar = Calendar.getInstance();
        DateFormat format = new SimpleDateFormat("m:s", Locale.ENGLISH);
        try {
            date = format.parse(text);
        } catch (ParseException ex) {
            System.out.println("Это не должно произойти. Ошибка при преобразовании даты.");
        }
        calendar.setTime(date);
        int second = calendar.get(Calendar.SECOND);
        int minute = calendar.get(Calendar.MINUTE);
        int Time = (minute * 60 + second) * 1000;
        return Time;
    }

}
