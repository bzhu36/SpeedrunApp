package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.Game;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.run.RunList;


public class HomepageFragment extends Fragment {

    RecyclerView recyclerView;
    //String gameName;
    HomepageAdapter adapter;
    RunList runList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
        RunListGames runListGames=new RunListGames();
        new Thread(new Runnable() {
            public void run() {
                try {
                    RunList runList = RunList.forStatus("verified&orderby=verify-date&direction=desc");
                    ArrayList<Game> games=new ArrayList<>();
                    for(int i=0; i<Array.getLength(runList.getRuns());i++){
                        games.add(i,Game.fromID(runList.getRuns()[i].getGameNames()));
                    }
                    runListGames.setRunList(runList);
                    runListGames.setGames(games);

                } catch (Exception e) {
                    System.out.println("not Verified");
                    e.printStackTrace();
                }
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new HomepageAdapter(getContext(), runListGames);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        }).start();
/*
        GameRetriever.getHome(new GameRetriever.HomeListResultListener() {
            @Override
            public void onHome(RunListGames runList) {
                adapter = new HomepageAdapter(getContext(), runList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter.notifyDataSetChanged();
            }
        });*/

        return view;
    }


}
