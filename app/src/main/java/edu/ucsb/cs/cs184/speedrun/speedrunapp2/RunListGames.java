package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import java.util.ArrayList;

import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.Game;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.run.RunList;

/**
 * Created by icema on 12/9/2017.
 */

public class RunListGames {
    private RunList runList;
    private ArrayList<Game>games;
    public RunListGames(){
        this.runList=null;
        games=new ArrayList<Game>();
    }
    public RunListGames(RunList runList, ArrayList<Game>games){
        this.runList=null;
        this.games = games;
    }

    public RunList getRunList() {
        return runList;
    }

    public void setRunList(RunList runList) {
        this.runList = runList;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }
}
