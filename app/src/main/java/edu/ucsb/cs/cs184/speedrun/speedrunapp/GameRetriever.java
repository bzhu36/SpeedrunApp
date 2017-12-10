package edu.ucsb.cs.cs184.speedrun.speedrunapp;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.Category;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.Game;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.GameList;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.Leaderboard;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.run.PlacedRun;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.run.Player;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.run.Run;

public class GameRetriever {

    public static void getGameList(String gameName, final GameListResultListener listener) {
        RetrieveGameListTask retrieveGameListTask = new RetrieveGameListTask(gameName, listener);
        retrieveGameListTask.execute();
    }
    public static void getRun(Category[]categories, int position, final RunResultListener listener) {
        RetrieveRunsTask retrieveRunTask = new RetrieveRunsTask(categories, position, listener);
        retrieveRunTask.execute();
    }

    interface GameListResultListener {
        void onGameList(GameList gameList);
    }
    interface RunResultListener {
        void onRun(LeaderboardPlayers leaderboard);
    }

    private static class RetrieveGameListTask extends AsyncTask<Void, Void, GameList> {
        private GameListResultListener listener;
        private String gameName;

        public RetrieveGameListTask(String gameName, GameListResultListener listener) {
            super();
            this.listener = listener;
            this.gameName = gameName;
        }

        @Override
        protected GameList doInBackground(Void... params) {
            GameList gameList = null;
            try {
                gameList = GameList.withName(gameName);
            } catch (Exception e) {
                System.out.println("catch");
                e.printStackTrace();
            }
            return gameList;
        }

        @Override
        protected void onPostExecute(GameList gameList) {
            if (listener != null) {
                listener.onGameList(gameList);
            }
        }
    }

    private static class RetrieveRunsTask extends AsyncTask<Void, Void, LeaderboardPlayers> {
        private RunResultListener listener;
        private Category[]categories;
        private int position;

        public RetrieveRunsTask(Category[] categories, int position, RunResultListener listener) {
            super();
            this.listener = listener;
            this.categories = categories;
            this.position = position;
        }

        @Override
        protected LeaderboardPlayers doInBackground(Void... params) {
            LeaderboardPlayers leaderboardPlayers = new LeaderboardPlayers();
            try {
                leaderboardPlayers.setLeaderboard(Leaderboard.forCategory(categories[position]));
                PlacedRun[] runs=leaderboardPlayers.getLeaderboard().getRuns();
                PlacedRun runs2[]=new PlacedRun[20];
                ArrayList<String> usernames=new ArrayList<>();
                for (int i = 0; i < runs.length; i++) {
                    usernames.add(i, runs[i].getRun().getPlayers()[0].getName());
                }
                leaderboardPlayers.setPlayers(usernames);

            } catch (Exception e) {
                System.out.println("catch");
                e.printStackTrace();
            }
            return  leaderboardPlayers;
        }

        @Override
        protected void onPostExecute(LeaderboardPlayers leaderboard) {
            if (listener != null) {
                listener.onRun(leaderboard);
            }
        }
    }

}

