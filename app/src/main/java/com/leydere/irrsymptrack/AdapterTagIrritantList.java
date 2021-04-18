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

/**
 * Adapter class for recyclerview utilized in ActivityNewIrritantTags.java.
 */
public class AdapterTagIrritantList extends RecyclerView.Adapter<AdapterTagIrritantList.MyViewHolder> {
    private final Context context;
    private ArrayList<ModelIrritantTag> irritantTagList;
    private OnItemClickListener onItemClickListener;



    public AdapterTagIrritantList(ArrayList<ModelIrritantTag> irritantTagList, Context context, OnItemClickListener onItemClickListener){
        this.irritantTagList = irritantTagList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView titleText;
        LinearLayout irritantTagParentLayout;
        CardView cardView;

        public MyViewHolder(final View view){
            super(view);
            titleText = view.findViewById(R.id.irritant_tag_list_text_display);
            irritantTagParentLayout = view.findViewById(R.id.irritantTagListParentLayout);
            cardView = view.findViewById(R.id.irritant_tag_list_card_view_item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public AdapterTagIrritantList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_irritant_tag_list_card, parent, false);
        return new AdapterTagIrritantList.MyViewHolder(view);
    }

    /**
     * Sets details of recyclerview cards.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull AdapterTagIrritantList.MyViewHolder holder, int position) {
        String titleFound = irritantTagList.get(position).getIrrTagTitle();
        holder.titleText.setText(titleFound);

        holder.irritantTagParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: remove onClickListener and test functionality
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: remove onClickListener and test functionality
            }
        });
    }

    @Override
    public int getItemCount() {
        return irritantTagList.size();
    }
}