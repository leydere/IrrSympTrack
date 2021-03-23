package com.leydere.irrsymptrack;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterTagIrritantSelection extends RecyclerView.Adapter<AdapterTagIrritantSelection.MyViewHolder> {
    private final Context context;
    private ArrayList<ModelIrritantTag> irritantTagList;
    private OnItemClickListener onItemClickListener;



    public AdapterTagIrritantSelection(ArrayList<ModelIrritantTag> irritantTagList, Context context, OnItemClickListener onItemClickListener){
        this.irritantTagList = irritantTagList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
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

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public AdapterTagIrritantSelection.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_irritant_tag_selection_card, parent, false);
        return new AdapterTagIrritantSelection.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTagIrritantSelection.MyViewHolder holder, int position) {
        String titleFound = irritantTagList.get(position).getIrrTagTitle();
        holder.titleText.setText(titleFound);
        holder.symbolText.setText("+");

        holder.irritantTagParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //not sure if I have any use for this on click listener after having implemented card view listener
                onItemClickListener.onItemClick(position);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            //TODO: send data to activity regarding the clicked or not clicked status of the cards
            public void onClick(View v) {
                ColorStateList c = holder.cardView.getCardBackgroundColor();
                int i = c.getDefaultColor();
                int lightPurple = Color.parseColor("#FFBB86FC");
                int standardPurple = Color.parseColor("#FF6200EE");

                if (i == lightPurple){
                    holder.cardView.setCardBackgroundColor(standardPurple);
                }else if (i == standardPurple){
                    holder.cardView.setCardBackgroundColor(lightPurple);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return irritantTagList.size();
    }
}