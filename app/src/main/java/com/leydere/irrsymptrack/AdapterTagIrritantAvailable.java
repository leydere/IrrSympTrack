package com.leydere.irrsymptrack;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        //TODO can add more String details here
        String titleFound = irritantTagList.get(position).getIrrTagTitle();
        holder.titleText.setText(titleFound);
        holder.symbolText.setText("+");
        holder.irritantTagParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: onclick add to list of selected tags

            }
        });
    }

    @Override
    public int getItemCount() {
        return irritantTagList.size();
    }
}