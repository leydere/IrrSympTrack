package com.leydere.irrsymptrack;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterTagIrritantAvailable extends RecyclerView.Adapter<AdapterTagIrritantAvailable.MyViewHolder> {
    private final Context context;
    private ArrayList<ModelIrritantTag> irritantTagList;
    CardView cardView; //unused?
    int row_index = -1; //unused?



    public AdapterTagIrritantAvailable(ArrayList<ModelIrritantTag> irritantTagList, Context context){
        this.irritantTagList = irritantTagList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView titleText;
        private TextView symbolText;
        LinearLayout irritantTagParentLayout;
        CardView cardView;

        public MyViewHolder(final View view){
            super(view);
            titleText = view.findViewById(R.id.irritant_tag_text_display1);
            symbolText = view.findViewById(R.id.irritant_tag_text_display2);
            irritantTagParentLayout = view.findViewById(R.id.irritantTagParentLayout);
            cardView = view.findViewById(R.id.irritant_tag_card_view_item);
        }
    }

    //unused?
    public interface IrrTagAvailableClickListener {
        void onItemClick(int position);
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

                //currently this just changes the color of the last card in the list
                //cardView.setCardBackgroundColor(Color.RED);

                //these are unused so far
                //row_index = position;
                //holder.irritantTagParentLayout.setCardBackgroundColor(Color.parseColor("#FFBB86FC"));
                //ActivitySelectIrritantTags.onIrritantTagClick(irritantTagList.get(position).getId());
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cardView.setCardBackgroundColor(Color.RED);
            }
        });

        /*
        if (row_index == position){
            cardView.setCardBackgroundColor(Color.RED);
        }else {
            //holder.irritantTagParentLayout.setBackgroundColor(Color.parseColor("#FF3700B3"));
        }*/
    }

    @Override
    public int getItemCount() {
        return irritantTagList.size();
    }
}