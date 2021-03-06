package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SplitEditFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SplitEditFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SplitEditFrag extends Fragment {
    RecyclerView recyclerView;
    Context context;
    SplitAdapter adapter;
    SplitsAdd splitsAdd;
    String game;
    String category;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SplitFrag.OnFragmentInteractionListener mListener;

    public SplitEditFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SplitEditFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static SplitEditFrag newInstance(String param1, String param2) {
        SplitEditFrag fragment = new SplitEditFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_split_edit, container, false);
        recyclerView = view.findViewById(R.id.splitRecyclerView);
        splitsAdd = new SplitsAdd();
        adapter = new SplitAdapter(getContext(), splitsAdd);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SplitFrag.OnFragmentInteractionListener) {
            mListener = (SplitFrag.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if( splitsAdd != null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("splits/" + user.getUid());
            DatabaseReference pushedRef = ref.push();
            String postId = pushedRef.getKey();
            Map<String,Object> childUpdates = new HashMap<>();
            game = "";
            category = "";
            childUpdates.put("game",game);
            childUpdates.put("category", category);
            pushedRef.updateChildren(childUpdates);
            DatabaseReference splitsListRef = pushedRef.child("splitsList");
            for (int i = 0; i < splitsAdd.size()-1; i++) {
                DatabaseReference splitRef = splitsListRef.child(new Integer(i).toString());
                childUpdates.clear();
                childUpdates.put("splitName", splitsAdd.getPair(0,i));
                childUpdates.put("time", splitsAdd.getPair(1,i));
                splitRef.updateChildren(childUpdates);

            }

        }
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
