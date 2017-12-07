package edu.ucsb.cs.cs184.speedrun.speedrunapp;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.GameList;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.Leaderboard;


public class LeaderboardFragment extends android.support.v4.app.Fragment {
    Handler customHandler = new Handler();
    EditText editText;
    Button button;
    RecyclerView recyclerView;
    String gameName;
    private LeaderboardAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        editText = (EditText)view.findViewById(R.id.searchBar);
        button = (Button)view.findViewById(R.id.searchButton);
        gameName="mario";
        recyclerView = view.findViewById(R.id.recyclerView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameName = editText.getText().toString();
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                GameList gameList=null;
                try {
                    gameList = GameList.withName(gameName);
                } catch (Exception e) {
                    System.out.println("catch");
                    e.printStackTrace();
                }

                adapter = new LeaderboardAdapter(getContext(), gameList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter.notifyDataSetChanged();
            }
        });

        return view;

    }

}
