package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.example.notification.activity.ToActivity;
import com.example.notification.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NotificationManagerCompat manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //使用NotificationManagerCompat.from产生的是使用NotificationManagerCompat对象。
        //也可以使用getSystemService(NOTIFICATION_SERVICE)产生NotificationManager对象。
        //用于管理Notification。
        //NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager = NotificationManagerCompat.from(MainActivity.this);

    }

    //按钮点击事件
    public void click(View view) {
        //检测用户是否允许通知
        if (manager.areNotificationsEnabled()) {
            sendNotify();
        } else {
            //让用户设置允许通知，适用于安卓SDK大于26的通知设置，更多版本兼容请查看
            //requestNotification();
            Intent intent = new Intent();
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
            startActivity(intent);
        }
    }

    //发出通知
    private void sendNotify() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify", "This Notification's name", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            Intent intent = new Intent(MainActivity.this, ToActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
            Notification notification = new NotificationCompat.Builder(MainActivity.this, "notify")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(getString(R.string.ma_notify_title))
                    .setContentText(getString(R.string.ma_notify_text))
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.message))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build();
            manager.notify(1, notification);
        } else {
            Intent intent = new Intent(MainActivity.this, ToActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
            Notification notification = new NotificationCompat.Builder(MainActivity.this)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.message))
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(getString(R.string.ma_notify_title))
                    .setContentText(getString(R.string.ma_notify_text))
                    .setContentIntent(pendingIntent)
                    .build();
            manager.notify(2, notification);
        }
    }

    //适用于各个版本安卓的通知允许设置
    private void requestNotification() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //5.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
            startActivity(intent);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {  //4.4
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + getPackageName()));
        } else if (Build.VERSION.SDK_INT >= 15) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        }
        startActivity(intent);
    }
}