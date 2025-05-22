package com.atbuys.runmawi;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.atbuys.runmawi.Vdocipher.DatabaseHelper;
import com.vdocipher.aegis.media.ErrorDescription;
import com.vdocipher.aegis.player.PlayerHost;
import com.vdocipher.aegis.player.VdoInitParams;
import com.vdocipher.aegis.player.VdoPlayer;
import com.vdocipher.aegis.ui.view.VdoPlayerUIFragment;

public class MainActivity extends AppCompatActivity implements PlayerHost.InitializationListener {
    private VdoPlayerUIFragment playerUIFragment;
    private String otp, playbackinfo;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        // Initialize database
        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Initialize VdoCipher player
        playerUIFragment = (VdoPlayerUIFragment) getSupportFragmentManager().findFragmentById(R.id.vdo_player_fragment);
        otp = "20160313versASE323j8VcIptKxFVTxgAdQatX4Ezc7foaIEeTx18bZ79KfEtaN9";
        playbackinfo = "eyJ2aWRlb0lkIjoiMGViYjUwMGVmNmMzNGIyZGJjMzRjNmYzZWMzNjgxMzYifQ==";
        playerUIFragment.initialize(this);
    }

    @Override
    public void onInitializationSuccess(PlayerHost playerHost, VdoPlayer vdoPlayer, boolean b) {
        VdoInitParams vdoInitParams = new VdoInitParams.Builder()
                .setOtp(otp)
                .setPlaybackInfo(playbackinfo)
                .build();
        vdoPlayer.load(vdoInitParams);
    }

    @Override
    public void onInitializationFailure(PlayerHost playerHost, ErrorDescription errorDescription) {
     //   Log.d("demo", "Initialization failed: " + errorDescription.get);
    }

    @Override
    public void onDeInitializationSuccess() {

    }
}
