package edu.ucsb.cs.cs184.speedrun.speedrunapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimerFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TimerFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button start;
    Button stop;
    Button lap;
    TextView timer;
    Context context;
    Handler customHandler = new Handler();
    LinearLayout container;
    long startTime=0L,timeInMillis=0L,timeSwapBuff=0L, updateTime=0L;
    Runnable timerThread = new Runnable() {
        @Override
        public void run() {
            timeInMillis = SystemClock.uptimeMillis()-startTime;
            updateTime = timeSwapBuff+timeInMillis;
            int secs = (int) updateTime/1000;
            int mins = secs/60;
            int hours = mins/60;
            mins%=60;
            secs%=60;
            int millis = (int) updateTime%1000;
            if(hours > 0)
                timer.setText(""+mins+":"+String.format("%02d",secs)+":"+String.format("%03d",millis));
            else if(hours == 0)
                timer.setText(""+mins+":"+String.format("%02d",secs)+":"+String.format("%03d",millis));
            customHandler.postDelayed(this,0);
        }
    };
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public TimerFrag() {
        // Required empty public constructor
    }

    public static TimerFrag newInstance(String param1, String param2) {
        TimerFrag fragment = new TimerFrag();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  =  inflater.inflate(R.layout.fragment_timer, container, false);
        start = (Button) view.findViewById(R.id.start);
        stop = (Button) view.findViewById(R.id.stop);
        lap = (Button) view.findViewById(R.id.lap);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = SystemClock.uptimeMillis();

                customHandler.postDelayed(timerThread,0);

            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSwapBuff += timeInMillis;

                customHandler.removeCallbacks(timerThread);
            }
        });
        lap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View addView = inflater.inflate(R.layout.row, null);
                TextView value = (TextView) addView.findViewById(R.id.textContent);
                value.setText(timer.getText());
                container.addView(addView);
            }
        });
        timer = (TextView) view.findViewById(R.id.display);

        this.container = (LinearLayout) view.findViewById(R.id.container);
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    this.context = context;

    }

    @Override
    public void onDetach() {
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
