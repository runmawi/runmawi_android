package com.atbuys.runmawi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class CreateNotification {

    public static final String CHANNEL_ID = "channel1";
    public static final String ACTION_PREVIUOS = "actionprevious";
    public static final String ACTION_PLAY = "actionplay";
    public static final String ACTION_NEXT = "actionnext";
    public static Notification notification;

    public static void createNotification(Context context, Track track, int playbutton, int pos, int size){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

            //Bitmap icon = BitmapFactory.decodeResource(context.getResources(), Integer.parseInt(track.getImage()));

            PendingIntent pendingIntentPrevious;
            int drw_previous;
            if (pos == 0){
                pendingIntentPrevious = null;
                drw_previous = 0;
            } else {
                Intent intentPrevious = new Intent(context, NotificationActionService.class)
                        .setAction(ACTION_PREVIUOS);
                pendingIntentPrevious = PendingIntent.getBroadcast(context, 0,
                        intentPrevious, PendingIntent.FLAG_MUTABLE);
                drw_previous = R.drawable.previous;
            }

            Intent intentPlay = new Intent(context, NotificationActionService.class)
                    .setAction(ACTION_PLAY);
            PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(context, 0,
                    intentPlay, PendingIntent.FLAG_MUTABLE);

            PendingIntent pendingIntentNext;
            int drw_next;
            if (pos == size){
                pendingIntentNext = null;
                drw_next = 0;
            } else {
                Intent intentNext = new Intent(context, NotificationActionService.class)
                        .setAction(ACTION_NEXT);
                pendingIntentNext = PendingIntent.getBroadcast(context, 0,
                        intentNext, PendingIntent.FLAG_MUTABLE);
                drw_next = R.drawable.next;
            }

            //create notification
            Intent intent = new Intent(context, MediaPlayerPageActivity.class);
            intent.putExtra("yourpackage.notifyId", 1);
            intent.putExtra("id",track.getId());
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
                    PendingIntent.FLAG_MUTABLE);


            notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.favicon)
                    .setContentTitle(track.getTitle())
                    .setContentText(track.getArtist())

                    .setOnlyAlertOnce(true)//show notification for only first time
                    .setShowWhen(true)
                    //.setLargeIcon(icon)
                    .addAction(drw_previous, "Previous", pendingIntentPrevious)
                    .addAction(playbutton, "Play", pendingIntentPlay)
                    .addAction(drw_next, "Next", pendingIntentNext)
                    .setStyle(new NotificationCompat.BigTextStyle())
                    .setContentIntent(pIntent)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()

                            .setShowActionsInCompactView(0,1,2))
                    .build();

            notificationManagerCompat.notify(1, notification);

        }
    }
}
