package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.Game;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.run.RunList;


public class HomepageFragment extends Fragment {

    RecyclerView recyclerView;
    //String gameName;
    HomepageAdapter adapter;
    ProgressBar progressBar;
    RunList runList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        progressBar = view.findViewById(R.id.progressBar2);
        recyclerView = view.findViewById(R.id.recyclerView);

        GameRetriever.getHome(progressBar,new GameRetriever.HomeListResultListener() {
            @Override
            public void onHome(RunListGames runList) {
                adapter = new HomepageAdapter(getContext(), runList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }


}
