package edu.ucsb.cs.cs184.speedrun.speedrunapp;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.Category;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.CategoryList;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.Game;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.GameList;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.Leaderboard;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.run.PlacedRun;

//Fragment represents a single game with it's name, cover, and leaderboards
public class GameFragment extends android.support.v4.app.Fragment {
    static Game game;
    Handler customHandler = new Handler();
    TextView gameTitle;
    ImageView gameCover;
    RecyclerView recyclerView;
    Spinner categorySpinner;
    private GameAdapter gameAdapter;

    public static GameFragment newInstance(Game game1) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        game = game1;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        //Initialize the different parts
        gameTitle = (TextView)view.findViewById(R.id.gameTitle2);
        gameCover = (ImageView) view.findViewById(R.id.gameCover2);
        categorySpinner = (Spinner)view.findViewById(R.id.categorySpinner);
        recyclerView = view.findViewById(R.id.gameRecycler);
        gameTitle.setText(game.getNames().get("international"));
        try {
            URL myUrl = new URL(game.getAssets().getCoverMedium().getUri());
            InputStream inputStream = (InputStream) myUrl.getContent();
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            gameCover.setImageDrawable(drawable);
        }
        catch (Exception e){

        }

        //Get the categories from the game
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        CategoryList categoryList=null;
        try {
            categoryList = game.getCategories();
        } catch (Exception e) {
            System.out.println("catch");
            e.printStackTrace();
        }

        //Set the spinner's items to the catgories
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item);
        Category categories[]=categoryList.getCategories();
        for (int i = 0; i< Array.getLength(categories); i++){
            adapter.add(categories[i].getName());
        }
        categorySpinner.setAdapter(adapter);

        //Sets up a listener for when an item in the spinner is selected
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Leaderboard leaderboard=null;

                try {
                    leaderboard = Leaderboard.forCategory(categories[i]);

                } catch (Exception e) {
                    System.out.println("catch");
                    e.printStackTrace();
                }
                gameAdapter = new GameAdapter(getContext(), leaderboard);
                recyclerView.setAdapter(gameAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        return view;

    }

}
