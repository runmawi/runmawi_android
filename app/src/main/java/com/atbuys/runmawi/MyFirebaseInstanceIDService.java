package com.atbuys.runmawi;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    private static final String TAG = "mFirebaseIIDService";
    private static final String SUBSCRIBE_TO = "userABC";

    @Override
    public void onNewToken(String token) {
        /*
          This method is invoked whenever a new token is generated.
          Send the token to your server if necessary and subscribe to topics.
        */
        Log.i(TAG, "onNewToken completed with token: " + token);

        // Subscribe to a topic once the token is generated
        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO)
                .addOnCompleteListener(task -> {
                    String msg = task.isSuccessful() ? "Subscribed to " + SUBSCRIBE_TO : "Subscription failed";
                    Log.d(TAG, msg);
                });

        // Optionally, save the token to shared preferences or send it to your server
        // SharedPreferences.Editor editor = getSharedPreferences(shared.My_preference_name, MODE_PRIVATE).edit();
        // editor.putString(shared.token, token);
    }
}
