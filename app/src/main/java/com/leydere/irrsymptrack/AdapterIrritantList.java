package com.leydere.irrsymptrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterIrritantList extends RecyclerView.Adapter<AdapterIrritantList.MyViewHolder> {
    private ArrayList<ModelIrritant> irritantList;

    public AdapterIrritantList(ArrayList<ModelIrritant> irritantList){
        this.irritantList = irritantList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView irritantText;

        public MyViewHolder(final View view){
            super(view);
            irritantText = view.findViewById(R.id.irritant_text_display);
        }
    }

    @NonNull
    @Override
    public AdapterIrritantList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_irritant_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterIrritantList.MyViewHolder holder, int position) {
        //TODO can add more String details here
        String titleFound = irritantList.get(position).getIrrTitle();
        holder.irritantText.setText(titleFound);
    }

    @Override
    public int getItemCount() {
        return irritantList.size();
    }
}
