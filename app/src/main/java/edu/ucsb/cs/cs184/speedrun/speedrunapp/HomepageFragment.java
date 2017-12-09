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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.lang.reflect.Array;

import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.GameList;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.run.Run;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.run.RunList;


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

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            runList = RunList.forStatus("verified&orderby=verify-date&direction=desc");
            System.out.println("verify");
        } catch (Exception e) {
            System.out.println("not Verified");
            e.printStackTrace();
        }

        adapter = new HomepageAdapter(getContext(), runList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.notifyDataSetChanged();

        return view;
    }


}
