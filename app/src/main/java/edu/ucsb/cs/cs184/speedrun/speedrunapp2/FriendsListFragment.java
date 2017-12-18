package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FriendsListFragment extends Fragment {
    Handler customHandler = new Handler();
    RecyclerView recyclerView;
    private FriendsListAdapter adapter;
    private ArrayList<UserDatabase> userDatabases;
    private ArrayList<String> keys;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private  UserDatabase userDatabase1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends_list, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        keys = new ArrayList<>();
        userDatabases = new ArrayList<>();
        recyclerView = view.findViewById(R.id.followList);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Query query = db.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                keys.clear();
                userDatabases.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    if(snap.getKey().equals("friends")) {
                        for (DataSnapshot snap2 : snap.getChildren()) {
                            if (snap2.getKey().equals(user.getUid())) {
                                for (DataSnapshot snap3 : snap2.getChildren()) {
                                    keys.add(snap3.getKey());
                                }
                            }
                        }
                    }
                    if(snap.getKey().equals("users")){
                        for(DataSnapshot snap2: snap.getChildren()){
                            if(keys.contains(snap2.getKey())){
                                userDatabases.add(snap2.getValue(UserDatabase.class));
                            }
                        }
                    }
                }
                adapter = new FriendsListAdapter(getContext(), userDatabases, keys);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        return view;

    }
}
