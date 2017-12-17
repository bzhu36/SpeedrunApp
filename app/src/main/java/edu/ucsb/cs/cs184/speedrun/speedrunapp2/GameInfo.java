package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.support.annotation.Nullable;

/**
 * Created by Ben Zhu on 12/2/2017.
 */

public class GameInfo {
    public static final int GAME_TYPE = 0;
    public static final int ADD_TYPE = 1;

    int iconId;
    String gameTitle;
    String worldRecord;
    int type;
    GameInfo(@Nullable int i,@Nullable String g,@Nullable String w, int t){
        this.iconId = i;
        this.gameTitle = g;
        this.worldRecord = w;
        this.type = t;
    }
}