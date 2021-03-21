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

public class AdapterSymptomList extends RecyclerView.Adapter<AdapterSymptomList.MyViewHolder> {
    private final Context context;
    private ArrayList<ModelSymptom> symptomList;

    public AdapterSymptomList(ArrayList<ModelSymptom> symptomList, Context context){
        this.symptomList = symptomList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView symptomText;
        LinearLayout symptomParentLayout;

        public MyViewHolder(final View view){
            super(view);
            symptomText = view.findViewById(R.id.symptom_text_display);
            symptomParentLayout = view.findViewById(R.id.symptomParentLayout);
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
        //TODO improvements to card UI object data can be made here
        String titleFound = symptomList.get(position).getSymTitle();
        holder.symptomText.setText(titleFound);
        holder.symptomParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send control to the add-edit symptom activity?
                Intent intent = new Intent(context, ActivityAddSymptom.class);
                intent.putExtra("id", symptomList.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return symptomList.size();
    }
}
