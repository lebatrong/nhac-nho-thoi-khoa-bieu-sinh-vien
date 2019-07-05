package com.student.thoikhoabieu.Services;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.student.thoikhoabieu.R;

public class Music extends Service {

    MediaPlayer mMediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPreferences = getSharedPreferences("caidat",MODE_PRIVATE);
        String amthanh = sharedPreferences.getString("uriamthanh","");
        String state = intent.getExtras().getString("data");
        int ID;
        if(state.equals("on"))
            ID = 1;
        else
            ID = 0;

        if(ID == 1) {
            if(amthanh.equals(""))
                mMediaPlayer = MediaPlayer.create(this, R.raw.baothuc);
            else
                mMediaPlayer = MediaPlayer.create(this, Uri.parse(amthanh));
            mMediaPlayer.start();
            mMediaPlayer.setLooping(true);
            ID = 0;
        } else if(mMediaPlayer!=null){
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        return START_STICKY;
    }



}
