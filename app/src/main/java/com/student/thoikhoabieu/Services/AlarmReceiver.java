package com.student.thoikhoabieu.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.student.thoikhoabieu.MainActivity;
import com.student.thoikhoabieu.R;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentServices = new Intent(context,Music.class);
        String state = intent.getExtras().getString("data");
        if(state.equals("off")){
            //Chuyển key off qua services Music tắt nhạc
            intentServices.putExtra("data",intent.getExtras().getString("data"));
            context.startService(intentServices);
        }else{
            //Đẩy thông báo
            notification(context);

            //Chuyển key on qua services Music bật nhạc
            intentServices.putExtra("data",intent.getExtras().getString("data"));
            context.startService(intentServices);
        }
    }

    private void notification(Context context) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        SharedPreferences sharedPreferences = context.getSharedPreferences("data",MODE_PRIVATE);
        String strhoten = sharedPreferences.getString("hoten", "Guest - 123 ");
        String[] arr = strhoten.split("-");
        String content = "Chào bạn "+arr[0] + " gần đến giờ học, vui lòng chuẩn bị";

        //Android 8 trở lên
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String CHANNEL_ID = "my_channel";
            CharSequence name = context.getString(R.string.app_name);

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    name,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setDescription(content);
            Notification notification = new Notification.Builder(context)
                    .setContentTitle("Nhắc nhở học tập")
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID)
                    .setSmallIcon(R.drawable.add_icon)
                    .setContentIntent(pendingIntent)
                    .build();
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(0,notification);
        }else {
            Notification notification = new Notification.Builder(context)
                    .setContentTitle("Nhắc nhở học tập")
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.add_icon)
                    .setContentIntent(pendingIntent)
                    .build();
            notificationManager.notify(0,notification);
        }




    }
}
