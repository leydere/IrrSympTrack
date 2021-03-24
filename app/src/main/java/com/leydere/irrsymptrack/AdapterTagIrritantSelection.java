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
    private ArrayList<Integer> selectedIrritantTagIDsList;
    private ArrayList<ModelIrritantTag> irritantTagList;
    private OnItemClickListener onItemClickListener;

    int lightPurple = Color.parseColor("#FFBB86FC");
    int standardPurple = Color.parseColor("#FF6200EE");



    public AdapterTagIrritantSelection(ArrayList<Integer> selectedIrritantTagIDsList, ArrayList<ModelIrritantTag> irritantTagList, Context context, OnItemClickListener onItemClickListener){
        this.selectedIrritantTagIDsList = selectedIrritantTagIDsList;
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
        void onItemClick(int irrTagModelID, boolean tagRecordSelected);
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

        // this is where the color is set via comparison between position ID and ArrayList<int>
        if (selectedIrritantTagIDsList.size() > 0){
            for (int idOfAssociatedRecords : selectedIrritantTagIDsList) {
                int idOfIrrTagModel = irritantTagList.get(position).getId();
                if(idOfAssociatedRecords == idOfIrrTagModel) {
                    holder.cardView.setCardBackgroundColor(standardPurple);
                }

            }
        }

        holder.irritantTagParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //not sure if I have any use for this on click listener after having implemented card view listener

            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            // id of model associated to clicked card is sent to activity along with toggle status - this supports creating the correct associative table data
            public void onClick(View v) {
                ColorStateList c = holder.cardView.getCardBackgroundColor();
                int i = c.getDefaultColor();

                if (i == lightPurple){
                    holder.cardView.setCardBackgroundColor(standardPurple);
                    onItemClickListener.onItemClick(irritantTagList.get(position).getId(), true);
                }else if (i == standardPurple){
                    holder.cardView.setCardBackgroundColor(lightPurple);
                    onItemClickListener.onItemClick(irritantTagList.get(position).getId(), false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return irritantTagList.size();
    }
}