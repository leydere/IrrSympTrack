package com.leydere.irrsymptrack;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterSymptomList extends RecyclerView.Adapter<AdapterSymptomList.MyViewHolder> {
    private ArrayList<ModelSymptom> symptomList;

    public AdapterSymptomList(ArrayList<ModelSymptom> symptomList){
        this.symptomList = symptomList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView symptomText;

        public MyViewHolder(final View view){
            super(view);
            symptomText = view.findViewById(R.id.symptom_text_display);
        }
    }

    @NonNull
    @Override
    public AdapterSymptomList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_symptom_row, parent, false);
        return new AdapterSymptomList.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSymptomList.MyViewHolder holder, int position) {
        //TODO can add more String details here
        String titleFound = symptomList.get(position).getSymTitle();
        holder.symptomText.setText(titleFound);
    }

    @Override
    public int getItemCount() {
        return symptomList.size();
    }
}
