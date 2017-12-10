package edu.ucsb.cs.cs184.speedrun.speedrunapp;

import java.util.ArrayList;

import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.Leaderboard;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.run.Player;

/**
 * Created by icema on 12/8/2017.
 */

public class LeaderboardPlayers {
    private Leaderboard leaderboard;
    private ArrayList<String> players;

    public LeaderboardPlayers(){
        leaderboard = null;
        players = null;

    }

    public LeaderboardPlayers(Leaderboard leaderboard, ArrayList<String> players){
        this.leaderboard=leaderboard;
        this.players=players;
    }

    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }
}