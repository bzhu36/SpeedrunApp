package edu.ucsb.cs.cs184.speedrun.speedrunapp;


import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucsb.cs.cs184.speedrun.speedrunapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddGameFrag extends DialogFragment {


    public AddGameFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_game, container, false);
    }

}
