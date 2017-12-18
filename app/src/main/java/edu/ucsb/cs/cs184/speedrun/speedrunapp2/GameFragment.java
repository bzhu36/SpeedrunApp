package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.Category;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.CategoryList;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.Game;

//Fragment represents a single game with it's name, cover, and leaderboards

public class GameFragment extends Fragment {
    static Game game;
    private Handler customHandler = new Handler();
    private TextView gameTitle;
    private TextView releaseDate;
    private ImageView gameCover;
    private RecyclerView recyclerView;
    private Spinner categorySpinner;
    private ProgressBar progressBar;
    static Drawable drawable;
    private GameAdapter gameAdapter;


    //Firebase Variables
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private UserDatabase userDatabase;
    private ArrayList<String> friendsList;
    private ArrayList<RunsDatabase> runs;
    private LocalGameAdapter localgameAdapter;


    public static GameFragment newInstance(Game game1, Drawable drawable1) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        game = game1;
        drawable=drawable1;
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
        releaseDate = (TextView)view.findViewById(R.id.dateView);
        categorySpinner = (Spinner)view.findViewById(R.id.categorySpinner);
        progressBar = view.findViewById(R.id.progressBar3);
        recyclerView = view.findViewById(R.id.gameRecycler);

        //Set Header UI elements
        gameTitle.setText(game.getNames().get("international"));
        gameCover.setImageDrawable(drawable);
        releaseDate.setText(releaseDate(game.getReleaseDate()));

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



        //Initialize Database Variables
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        friendsList = new ArrayList<>();
        runs = new ArrayList<>();



        //Sets up a listener for when an item in the spinner is selected
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (MainActivity.local == false){
                    GameRetriever.getRun(categories, i, progressBar, recyclerView, new GameRetriever.RunResultListener() {
                        @Override
                        public void onRun(LeaderboardPlayers leaderboard) {
                            gameAdapter = new GameAdapter(getContext(), leaderboard);
                            recyclerView.setAdapter(gameAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            gameAdapter.notifyDataSetChanged();
                        }
                    });
                }
                else {
                    runs.clear();
                    setAdapter();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;

    }

    public String releaseDate(String dateString){
        String year=dateString.substring(0, 4);
        String date=dateString.substring(8, 10);
        String month="";

        switch (dateString.substring(5,7)){
            case "01":
                month="January";
                break;
            case "02":
                month="February";
                break;
            case "03":
                month="March";
                break;
            case "04":
                month="April";
                break;
            case "05":
                month="May";
                break;
            case "06":
                month="June";
                break;
            case "07":
                month="July";
                break;
            case "08":
                month="August";
                break;
            case "09":
                month="September";
                break;
            case "10":
                month="October";
                break;
            case "11":
                month="November";
                break;
            case "12":
                month="December";
                break;

        }
        if(date.charAt(0)==0){
            date=dateString.substring(9,10);
        }
        return month+" "+date+", "+year;

    }

    private void setAdapter(){

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("friends");
        Query query = db.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap: dataSnapshot.getChildren()){
                    if(snap.getKey().equals(user.getUid())){
                        for (DataSnapshot friends: snap.getChildren()){
                            friendsList.add(friends.getKey());
                        }
                    }
                }
                //include yourself in leaderboard
                friendsList.add(user.getUid());
                //setAdapter with leaderboard
                setGamesLeaderboard(friendsList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setGamesLeaderboard(ArrayList<String> friends) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("runs");
        Query query = db.orderByChild("game").equalTo(game.getNames().get("international"));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap: dataSnapshot.getChildren()){
                    System.out.println(categorySpinner.getSelectedItem().toString());
                    if ((friendsList.contains(snap.getValue(RunsDatabase.class).getUserid()))
                            && (snap.getValue(RunsDatabase.class).getCategory().equals(categorySpinner.getSelectedItem().toString()))) {
                        System.out.println(snap.getValue(RunsDatabase.class).getCategory());
                        runs.add(snap.getValue(RunsDatabase.class));
                    }
                }

                localgameAdapter = new LocalGameAdapter(getContext(), runs);
                recyclerView.setAdapter(localgameAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                localgameAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
