package com.atbuys.runmawi;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String ADMIN_CHANNEL_ID = "admin_channel";
    Bitmap bigSizeImg;
    public static final String ACTION_1 = "action_1";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        SharedPreferences prefs = getSharedPreferences(Constant.MY_PREFS_NAME, MODE_PRIVATE);
        String state = prefs.getString(Constant.notify_On, "");
        if(state.equalsIgnoreCase("off")){

        }else{
            if (remoteMessage.getData() != null) {
                String title = remoteMessage.getNotification().getTitle();
                String messageBody = remoteMessage.getNotification().getBody();
                String imageUrl = "https://runmawi.com/public/uploads/settings/ROD%20TV%20V5%20200x133.png";
                String name = String.valueOf(remoteMessage.getNotification().getTag());
                String smallIcon = String.valueOf(remoteMessage.getNotification().getLink());
                sendNotification(title, messageBody, imageUrl,name,smallIcon);
            }
        }
    }

    private void sendNotification(String title, String messageBody, String imageUrl, String name, String smallIcon) {

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bigSizeImg = BitmapFactory.decodeStream(input);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap b = Bitmap.createScaledBitmap(bigSizeImg, 120, 120, false);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class)
                .setAction(ACTION_1);
       // TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addCategory(Intent. CATEGORY_LAUNCHER ) ;
        intent.setAction(Intent. ACTION_MAIN ) ;
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplication(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel("CHANNEL_1", "Example channel", NotificationManager.IMPORTANCE_DEFAULT);
        }
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(messageBody);
        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(this, "CHANNEL_1");
        notification
                .setSmallIcon(R.drawable.favicon) // can use any other icon
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setContentTitle(title)
                .setContentText(name)
                .setDefaults(Notification.DEFAULT_ALL) // must requires VIBRATE permission
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLargeIcon(b)
                .setStyle(bigText)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_1");
        builder.setContentIntent(pendingIntent);
        if (!TextUtils.isEmpty(title)) {
            notification.setContentTitle(title);
        } else {
            notification.setContentTitle(messageBody);
        }
        if (!TextUtils.isEmpty(imageUrl)) {
            try {
                URL url = new URL(imageUrl);
                InputStream in;
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap image = BitmapFactory.decodeStream(in);
                NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(image);
                s.setSummaryText(title);
                notification.setStyle(s);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(11221, notification.build());
        if (Build.VERSION. SDK_INT >= Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new
                    NotificationChannel( "10001" , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            builder.setChannelId( "10001" ) ;
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert notificationManager != null;
      /*  notificationManager.notify(( int ) System. currentTimeMillis () ,
                builder.build()) ;*/
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void makeNotificationChannel(String id, String name, int importance)
    {
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setShowBadge(true); // set false to disable badges, Oreo exclusive

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int width ) {
        float aspectRatio = bm.getWidth() /
                (float) bm.getHeight();
        int height = Math.round(width / aspectRatio);

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                bm, width, height, false);

        return resizedBitmap;
    }
}