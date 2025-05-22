package com.atbuys.runmawi;

/**
 * Created by Mayur Solanki (mayursolanki120@gmail.com) on 27/05/20, 2:32 PM.
 */

import androidx.media3.common.Format;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.source.TrackGroupArray;




@UnstableApi
public final class TrackKey {
    private TrackGroupArray trackGroupArray;
    private TrackGroup trackGroup;
    private Format trackFormat;

    public TrackKey(TrackGroupArray trackGroupArray, TrackGroup trackGroup, Format trackFormat) {
        this.trackGroupArray = trackGroupArray;
        this.trackGroup = trackGroup;
        this.trackFormat = trackFormat;
    }


    public TrackGroupArray getTrackGroupArray() {
        return trackGroupArray;
    }

    public void setTrackGroupArray(TrackGroupArray trackGroupArray) {
        this.trackGroupArray = trackGroupArray;
    }

    public TrackGroup getTrackGroup() {
        return trackGroup;
    }

    public void setTrackGroup(TrackGroup trackGroup) {
        this.trackGroup = trackGroup;
    }

    public Format getTrackFormat() {
        return trackFormat;
    }

    public void setTrackFormat(Format trackFormat) {
        this.trackFormat = trackFormat;
    }
}
