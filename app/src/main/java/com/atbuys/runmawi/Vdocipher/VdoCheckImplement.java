package com.atbuys.runmawi.Vdocipher;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.atbuys.runmawi.HomePageActivitywithFragments;
import com.atbuys.runmawi.R;
import com.vdocipher.aegis.media.ErrorDescription;
import com.vdocipher.aegis.player.PlayerHost;
import com.vdocipher.aegis.player.VdoInitParams;
import com.vdocipher.aegis.player.VdoPlayer;
import com.vdocipher.aegis.ui.view.VdoPlayerUIFragment;

import java.util.HashMap;

public class VdoCheckImplement extends AppCompatActivity implements PlayerHost.InitializationListener {
    private VdoPlayerUIFragment playerUIFragment;
    private String otp, playbackinfo,playerId, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main1);
        playerUIFragment = (VdoPlayerUIFragment) getSupportFragmentManager().findFragmentById(R.id.vdo_player_fragment);

        playerId="rB863ffmSqHHeHdt";
        Intent in = getIntent();
        id = in.getStringExtra("id");
        otp = in.getStringExtra("otp");
        playbackinfo = in.getStringExtra("playback");
        /*id="67";
        otp="20160313versASE323QoGYaiY1xlOzyaig4hFxety6mvUC2xlGwo8NsiKPyUVmo2";
        playbackinfo="eyJ2aWRlb0lkIjoiZWQ3ZGNmODZmZDkwNDRiZThjZmZiNWY3ZDhkZTVmODcifQ==";*/
        Log.w("runmawi", "id: " + id + " otp: " + otp + " playbackinfo: " + playbackinfo);
        //20160313versASE323PQqBLLzV43d91uBCdIfTzMtT2BabbtwsLCWKB3rh6UbLke
        //20160313versASE3235WDmWgIwf7HPooijdSG5x81Bn8z6e6qCfrw9nrXY5WJcBL


        playerUIFragment.initialize(this);


    }

    @Override
    public void onInitializationSuccess(PlayerHost playerHost, VdoPlayer vdoPlayer, boolean b) {
        if (otp == null || otp.isEmpty() || playbackinfo == null || playbackinfo.isEmpty()) {
            // Pass the intent to another activity if otp or playbackinfo is null or empty
            new AlertDialog.Builder(this)
                    .setTitle("Video Unavailable")
                    .setMessage("We apologize for the inconvenience. The video is currently not available. Please try again later.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();  // Dismiss the dialog when OK is pressed
                            //Intent intent = new Intent(Videoplayer_cipher.this, HomePageActivitywithFragments.class);
                            //startActivity(intent);
                            finish(); // Optionally close the current activity
                        }
                    }).show();


        } else {
            // If otp and playbackinfo are not empty, proceed with loading the video

            VdoInitParams vdoInitParams = new VdoInitParams.Builder()
                    .setOtp(otp)
                    .setPlaybackInfo(playbackinfo)
                    .setCustomPlayer(playerId)
                    .build();
            vdoPlayer.load(vdoInitParams);

            /*HashMap<String, Object> map = new HashMap<>();
            map.put("enableChromecast", true);

           VdoInitParams receivedParams = new VdoInitParams.Builder()
                    .setOtp(otp)
                    .setPlaybackInfo(playbackinfo)
                   .setCustomPlayer(playerId)
                    .setPreferredCaptionsLanguage("en")
                    .updateConfig(map)
                    .build();
            vdoPlayer.load(receivedParams);*/
        }
    }

    @Override
    public void onInitializationFailure(PlayerHost playerHost, ErrorDescription errorDescription) {
    }
    @Override
    public void onDeInitializationSuccess() {
    }

}
