package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.Button;
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
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.Category;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.CategoryList;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.Game;

/**
 * Created by Ben Zhu on 12/19/2017.
 */

public class AddGameCategoryFrag extends DialogFragment {

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
    private Button button;

    //Firebase Variables
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private UserDatabase userDatabase;
    private ArrayList<String> friendsList;
    private ArrayList<RunsDatabase> runs;
    private LocalGameAdapter localgameAdapter;
    private String category;
    private String name;
    private Uri cover;
    private Context context;

    public static AddGameCategoryFrag newInstance(Game game1, Drawable drawable1) {
        AddGameCategoryFrag fragment = new AddGameCategoryFrag();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        game = game1;
        drawable=drawable1;
        return fragment;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_game_category, container, false);

        //Initialize the different parts
        gameTitle = (TextView)view.findViewById(R.id.gameTitleCategory);
        gameCover = (ImageView) view.findViewById(R.id.gameCoverCategory);
        categorySpinner = (Spinner)view.findViewById(R.id.categorySpinnerCategory);
        //Set Header UI elements
        gameTitle.setText(game.getNames().get("international"));
        gameCover.setImageDrawable(drawable);
        button = view.findViewById(R.id.createTimer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = ((Activity) context).getFragmentManager();
                Fragment fragment = new TimerFrag();
                FragmentTransaction ft = manager.beginTransaction();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("timer/" + user.getUid());
                    DatabaseReference pushedRef = ref.push();
                    String postId = pushedRef.getKey();
                    Map<String,Object> childUpdates = new HashMap<>();
                    childUpdates.put("game", gameTitle.getText().toString());
                    childUpdates.put("category", category);
                    childUpdates.put("uri", game.getAssets().getCoverMedium().getUri());
                    pushedRef.updateChildren(childUpdates);
                    DatabaseReference splitsListRef = pushedRef.child("splitsList");

                ft.replace(R.id.content_main, fragment).addToBackStack(null).commit();
                getDialog().dismiss();
            }
        });
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
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item);
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
                category = categorySpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;

    }

    public interface DialogCategoryListener{
        void gameCategoryListener(String game, String category);
    }
}

