package com.atbuys.runmawi;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

public class MyReceiver  extends BroadcastReceiver {

    Dialog dialog;
    TextView nettext;


    @Override
    public void onReceive(Context context, Intent intent) {

        String status = NetworkUtil.getConnectivityStatusString(context);
    //    Log.d("network",status);
        /*if(status.equals("No internet is available")||status.equals("No Internet Connection")) {
            status="No Internet Connection";

        }
        Toast.makeText(context, status, Toast.LENGTH_LONG).show();*/
    }

    }
