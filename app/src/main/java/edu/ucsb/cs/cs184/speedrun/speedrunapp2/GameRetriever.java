package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.Category;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.Game;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.GameList;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.Leaderboard;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.run.PlacedRun;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.run.RunList;

public class GameRetriever {

    public static void getGameList(String gameName, final GameListResultListener listener) {
        RetrieveGameListTask retrieveGameListTask = new RetrieveGameListTask(gameName, listener);
        retrieveGameListTask.execute();
    }
    public static void getRun(Category[]categories, int position, ProgressBar progressBar, RecyclerView recyclerView, final RunResultListener listener) {
        RetrieveRunsTask retrieveRunTask = new RetrieveRunsTask(categories, position, progressBar, recyclerView, listener);
        retrieveRunTask.execute();
    }
    public static void getHome(ProgressBar progressBar, final HomeListResultListener listener) {
        RetrieveHomeListTask retrieveHomeListTask = new RetrieveHomeListTask(progressBar, listener);
        retrieveHomeListTask.execute();
    }

    interface GameListResultListener {
        void onGameList(GameList gameList);
    }
    interface RunResultListener {
        void onRun(LeaderboardPlayers leaderboard);
    }
    interface HomeListResultListener {
        void onHome(RunListGames runList);
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
        private ProgressBar progressBar;
        private RecyclerView recyclerView;

        public RetrieveRunsTask(Category[] categories, int position, ProgressBar progressBar, RecyclerView recyclerView, RunResultListener listener) {
            super();
            this.recyclerView = recyclerView;
            this.listener = listener;
            this.categories = categories;
            this.position = position;
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute()
        {
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected LeaderboardPlayers doInBackground(Void... params) {
            LeaderboardPlayers leaderboardPlayers = new LeaderboardPlayers();
            try {
                leaderboardPlayers.setLeaderboard(Leaderboard.forCategory(categories[position]));
                PlacedRun[] runs=leaderboardPlayers.getLeaderboard().getRuns();
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
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            if (listener != null) {
                listener.onRun(leaderboard);
            }
        }
    }

    private static class RetrieveHomeListTask extends AsyncTask<Void, Void, RunListGames> {
        private HomeListResultListener listener;
        private ProgressBar progressBar;
        //private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        public RetrieveHomeListTask(ProgressBar progressBar, HomeListResultListener listener) {
            super();
            this.listener = listener;
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);

        };

        @Override
        protected RunListGames doInBackground(Void... params) {
            RunListGames runListGames=new RunListGames();
            try {
                RunList runList = RunList.forStatus("verified&orderby=verify-date&direction=desc");
                ArrayList<Game>games=new ArrayList<>();
                for(int i=0; i<Array.getLength(runList.getRuns());i++){
                    games.add(i,Game.fromID(runList.getRuns()[i].getGameNames()));
                }
                runListGames.setRunList(runList);
                runListGames.setGames(games);

            } catch (Exception e) {
                System.out.println("not Verified");
                e.printStackTrace();
            }
            return runListGames;
        }

        @Override
        protected void onPostExecute(RunListGames runList) {
            progressBar.setVisibility(View.INVISIBLE);
            if (listener != null) {
                listener.onHome(runList);
            }
        }
    }

}

