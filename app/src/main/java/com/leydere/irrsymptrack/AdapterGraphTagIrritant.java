package com.leydere.irrsymptrack;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterGraphTagIrritant extends RecyclerView.Adapter<AdapterGraphTagIrritant.MyViewHolder> {
    private final Context context;
    private ArrayList<ModelIrritantTag> irritantTagList;
    private AdapterGraphTagIrritant.OnItemClickListener onItemClickListener;


    public AdapterGraphTagIrritant(ArrayList<ModelIrritantTag> irritantTagList, Context context, AdapterGraphTagIrritant.OnItemClickListener onItemClickListener){
        this.irritantTagList = irritantTagList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView titleText;
        LinearLayout parentLayout;
        CardView cardView;

        public MyViewHolder(final View view){
            super(view);
            titleText = view.findViewById(R.id.irritant_graph_textView);
            parentLayout = view.findViewById(R.id.irritantGraphParentLayout);
            cardView = view.findViewById(R.id.irritant_graph_card_view_item);
        }
    }

    public interface OnItemClickListener {
        void onIrrItemClick(int irrTagModelID, String irrTagModelTitle);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_graph_irritant_card, parent, false);
        return new AdapterGraphTagIrritant.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String titleFound = irritantTagList.get(position).getIrrTagTitle();
        holder.titleText.setText(titleFound);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            // id of model associated to clicked card is sent to activity
            public void onClick(View v) {
                onItemClickListener.onIrrItemClick(irritantTagList.get(position).getId(), irritantTagList.get(position).getIrrTagTitle());
            }
        });

    }

    @Override
    public int getItemCount() {
        return irritantTagList.size();
    }


}
