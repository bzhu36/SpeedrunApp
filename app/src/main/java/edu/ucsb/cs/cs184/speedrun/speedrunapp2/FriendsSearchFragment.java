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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.GameList;



public class FriendsSearchFragment extends Fragment {
    private Handler customHandler = new Handler();
    private EditText editText;
    private Button button;
    private RecyclerView recyclerView;
    private FriendsAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends_search, container, false);
        editText = (EditText)view.findViewById(R.id.searchBar2);
        button = (Button)view.findViewById(R.id.searchButton2);
        recyclerView = view.findViewById(R.id.recyclerView2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("users");
                Query query = db.orderByChild("email").equalTo(editText.getText().toString());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            for (DataSnapshot snap: dataSnapshot.getChildren()) {
                                UserDatabase userDatabase = snap.getValue(UserDatabase.class);
                                recyclerView.setVisibility(View.VISIBLE);
                                adapter = new FriendsAdapter(getContext(), userDatabase, snap.getKey());
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                adapter.notifyDataSetChanged();
                            }
                        }
                        else{
                            Toast.makeText(getContext(), "No user found", Toast.LENGTH_LONG).show();
                            recyclerView.setVisibility(View.INVISIBLE);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
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
