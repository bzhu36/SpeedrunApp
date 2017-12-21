package edu.ucsb.cs.cs184.speedrun.speedrunapp2;


import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.GameList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddGameFrag extends DialogFragment{


    private EditText editText;
    private Button button;
    private String gameName;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
//    private AddGameAdapter adapter;
    private AddGameAdapter adapter;
    public AddGameFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_game, container, false);
        editText = (EditText)view.findViewById(R.id.timer_searchBar);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar4);
        button = (Button)view.findViewById(R.id.timer_searchButton);
        recyclerView = view.findViewById(R.id.timer_SearchRecyclerView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameName = editText.getText().toString();
                GameRetriever.getGameList(gameName, progressBar, new GameRetriever.GameListResultListener() {
                    @Override
                    public void onGameList(GameList gameList) {
                        adapter = new AddGameAdapter(getContext(), gameList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        adapter.notifyDataSetChanged();

                    }
                });
                //Hides the keyboard when search button is pressed.
                InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });


        return view;

    }


}

