package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.os.AsyncTask;

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
    public static void getRun(Category[]categories, int position, final RunResultListener listener) {
        RetrieveRunsTask retrieveRunTask = new RetrieveRunsTask(categories, position, listener);
        retrieveRunTask.execute();
    }
    public static void getHome(final HomeListResultListener listener) {
        RetrieveHomeListTask retrieveHomeListTask = new RetrieveHomeListTask(listener);
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

    private static class RetrieveHomeListTask extends AsyncTask<Void, Void, RunListGames> {
        private HomeListResultListener listener;
        //private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        public RetrieveHomeListTask(HomeListResultListener listener) {
            super();
            this.listener = listener;
        }

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
            if (listener != null) {
                listener.onHome(runList);
            }
        }
    }

}

