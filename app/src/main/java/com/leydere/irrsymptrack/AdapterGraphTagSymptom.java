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

/**
 * Adapter class for the recyclerview utilized in ActivityGraphView.java.
 */
public class AdapterGraphTagSymptom extends RecyclerView.Adapter<AdapterGraphTagSymptom.MyViewHolder> {
    private final Context context;
    private ArrayList<ModelSymptomTag> symptomTagList;
    private AdapterGraphTagSymptom.OnItemClickListener onItemClickListener;


    public AdapterGraphTagSymptom(ArrayList<ModelSymptomTag> symptomTagList, Context context, AdapterGraphTagSymptom.OnItemClickListener onItemClickListener){
        this.symptomTagList = symptomTagList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView titleText;
        LinearLayout parentLayout;
        CardView cardView;

        public MyViewHolder(final View view){
            super(view);
            titleText = view.findViewById(R.id.symptom_graph_textView);
            parentLayout = view.findViewById(R.id.symptomGraphParentLayout);
            cardView = view.findViewById(R.id.symptom_graph_card_view_item);
        }
    }

    public interface OnItemClickListener {
        void onSymItemClick(int symTagModelID, String symTagModelTitle);
    }

    @NonNull
    @Override
    public AdapterGraphTagSymptom.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_graph_symptom_card, parent, false);
        return new AdapterGraphTagSymptom.MyViewHolder(view);
    }

    /**
     * Sets the details displayed in the recyclerview cards. OnClick sends tag record ID and title to ActivityGraphView.java.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull AdapterGraphTagSymptom.MyViewHolder holder, int position) {
        String titleFound = symptomTagList.get(position).getSymTagTitle();
        holder.titleText.setText(titleFound);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            // id of model associated to clicked card is sent to activity
            public void onClick(View v) {
                onItemClickListener.onSymItemClick(symptomTagList.get(position).getId(), symptomTagList.get(position).getSymTagTitle());
            }
        });

    }

    @Override
    public int getItemCount() {
        return symptomTagList.size();
    }


}