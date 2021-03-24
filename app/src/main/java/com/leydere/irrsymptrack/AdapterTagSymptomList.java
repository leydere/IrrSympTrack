package com.leydere.irrsymptrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterTagSymptomList extends RecyclerView.Adapter<AdapterTagSymptomList.MyViewHolder> {
    private final Context context;
    private ArrayList<ModelSymptomTag> symptomTagList;
    private AdapterTagSymptomList.OnItemClickListener onItemClickListener;



    public AdapterTagSymptomList(ArrayList<ModelSymptomTag> symptomTagList, Context context, AdapterTagSymptomList.OnItemClickListener onItemClickListener){
        this.symptomTagList = symptomTagList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView titleText;
        LinearLayout symptomTagParentLayout;
        CardView cardView;

        public MyViewHolder(final View view){
            super(view);
            titleText = view.findViewById(R.id.symptom_tag_list_text_display);
            symptomTagParentLayout = view.findViewById(R.id.symptomTagListParentLayout);
            cardView = view.findViewById(R.id.symptom_tag_list_card_view_item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public AdapterTagSymptomList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_symptom_tag_list_card, parent, false);
        return new AdapterTagSymptomList.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTagSymptomList.MyViewHolder holder, int position) {
        String titleFound = symptomTagList.get(position).getSymTagTitle();
        holder.titleText.setText(titleFound);

        holder.symptomTagParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // don't think I need on click listeners for this implementation of recyclerview adapter
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // don't think I need on click listeners for this implementation of recyclerview adapter
            }
        });
    }

    @Override
    public int getItemCount() {
        return symptomTagList.size();
    }
}