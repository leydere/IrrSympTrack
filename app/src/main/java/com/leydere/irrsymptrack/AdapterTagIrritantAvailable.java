package com.leydere.irrsymptrack;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterTagIrritantAvailable extends RecyclerView.Adapter<AdapterTagIrritantAvailable.MyViewHolder> {
    private final Context context;
    private ArrayList<ModelIrritantTag> irritantTagList;

    public AdapterTagIrritantAvailable(ArrayList<ModelIrritantTag> irritantTagList, Context context){
        this.irritantTagList = irritantTagList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView titleText;
        private TextView symbolText;
        LinearLayout irritantTagParentLayout;

        public MyViewHolder(final View view){
            super(view);
            titleText = view.findViewById(R.id.irritant_tag_text_display1);
            symbolText = view.findViewById(R.id.irritant_tag_text_display2);
            irritantTagParentLayout = view.findViewById(R.id.irritantTagParentLayout);
        }
    }

    @NonNull
    @Override
    public AdapterTagIrritantAvailable.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_irritant_tag_card, parent, false);
        return new AdapterTagIrritantAvailable.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTagIrritantAvailable.MyViewHolder holder, int position) {
        String titleFound = irritantTagList.get(position).getIrrTagTitle();
        holder.titleText.setText(titleFound);
        holder.symbolText.setText("+");
        holder.irritantTagParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: color should update to indicate has been selected, id should be added to list which will be passed back upon return




            }
        });
    }

    @Override
    public int getItemCount() {
        return irritantTagList.size();
    }
}