package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import static edu.ucsb.cs.cs184.speedrun.speedrunapp2.SplitsAdd.ADD_TYPE;
import static edu.ucsb.cs.cs184.speedrun.speedrunapp2.SplitsAdd.SPLIT_TYPE;
/**
 * Created by Ben Zhu on 12/13/2017.
 */

class SplitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    SplitsAdd splitsAdd;
    LayoutInflater inflater;
    public SplitAdapter(Context context, SplitsAdd splitsAdd){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.splitsAdd = splitsAdd;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch(viewType){
            case SPLIT_TYPE:
                view = inflater.inflate(R.layout.splits_row, parent, false);
                return new splitHolder(view);
            case ADD_TYPE:
                view = inflater.inflate(R.layout.splits_add, parent, false);
                return new addHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //nothing to do
    }

    @Override
    public int getItemCount() {
        return splitsAdd.type.size();
    }

    @Override
    public int getItemViewType(int position) { return splitsAdd.type.get(position);}

    class splitHolder extends RecyclerView.ViewHolder{
    ImageView splitImage;
    EditText splitName;
    TextView currentTime;
    EditText bestTime;

        public splitHolder(View itemView) {
            super(itemView);
            //splitImage = itemView.findViewById(R.id.imageAddSplits);
            splitName = itemView.findViewById(R.id.nameAddSplits);
            currentTime = itemView.findViewById(R.id.timeCurrentAddSplits);
            bestTime = itemView.findViewById(R.id.timeBestAddSplits);

            splitName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    splitsAdd.addSplitToList(splitHolder.this.getAdapterPosition(), Pair.create(charSequence.toString(), splitsAdd.getPair(1,splitHolder.this.getAdapterPosition())));
                    System.out.println(splitsAdd.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            bestTime.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    splitsAdd.addSplitToList(splitHolder.this.getAdapterPosition(), Pair.create(splitsAdd.getPair(0,splitHolder.this.getAdapterPosition()),charSequence.toString()));
                    System.out.println(splitsAdd.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            ImageOnClickListener listener = new ImageOnClickListener();
            splitImage.setOnClickListener(listener);
        }
    }

    class addHolder extends RecyclerView.ViewHolder{
        public addHolder(View itemView){
            super(itemView);
            AddOnClickListener listener = new AddOnClickListener();
            itemView.setOnClickListener(listener);
        }
    }

    class ImageOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //code to upload image
        }
    }
    class AddOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            int newpos = splitsAdd.addSplitToEditList();
            splitsAdd.addSplitToList(newpos, Pair.create("",""));
            SplitAdapter.this.notifyItemInserted(splitsAdd.size()-1);
        }
    }


}
