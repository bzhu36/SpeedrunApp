package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.ucsb.cs.cs184.speedrun.speedrunapp2.R;


public class ProfileFragment extends Fragment {
    //UI Variables
    private Handler customerHandler = new Handler();
    private TextView textView;
    private RecyclerView recyclerView;
    private ImageView profPic;
    private ProfileAdapter adapter;
    private static String userID;
    private static String username;


    //User Specific Variables
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private UserDatabase userDatabase;
    private ArrayList<RunsDatabase> runs;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }
    public static ProfileFragment newInstance(String userID1, String username1) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        userID = userID1;
        username = username1;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        recyclerView = view.findViewById(R.id.myRuns);
        runs = new ArrayList<>();
        textView = view.findViewById(R.id.usernameText);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        textView.setText(username);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("runs");
        Query query2 = db.orderByChild("userid").equalTo(userID);
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snap: dataSnapshot.getChildren()){
                        System.out.println(snap.getValue(RunsDatabase.class).getName());
                        runs.add(snap.getValue(RunsDatabase.class));
                    }
                }
                adapter = new ProfileAdapter(getContext(),runs);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapse_toolbar);

        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("My Runs");
                    collapsingToolbar.setCollapsedTitleGravity(Gravity.CENTER_HORIZONTAL);
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });

        return view;
    }

}
