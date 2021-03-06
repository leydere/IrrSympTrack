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
 * Adapter class for the recyclerview utilized in ActivityAddSymptom.java.
 */
public class AdapterTagSymptomSelection extends RecyclerView.Adapter<AdapterTagSymptomSelection.MyViewHolder> {
    private final Context context;
    private ArrayList<Integer> selectedSymptomTagIDsList;
    private ArrayList<ModelSymptomTag> symptomTagList;
    private AdapterTagSymptomSelection.OnItemClickListener onItemClickListener;

    int lightBlue = Color.parseColor("#5c5cff");
    int standardBlue = Color.parseColor("#1f1fff");

    public AdapterTagSymptomSelection(ArrayList<Integer> selectedSymptomTagIDsList, ArrayList<ModelSymptomTag> symptomTagList, Context context, AdapterTagSymptomSelection.OnItemClickListener onItemClickListener){
        this.selectedSymptomTagIDsList = selectedSymptomTagIDsList;
        this.symptomTagList = symptomTagList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView titleText;
        private TextView symbolText;
        LinearLayout symptomTagParentLayout;
        CardView cardView;

        public MyViewHolder(final View view){
            super(view);
            titleText = view.findViewById(R.id.symptom_tag_text_display1);
            symbolText = view.findViewById(R.id.symptom_tag_text_display2);
            symptomTagParentLayout = view.findViewById(R.id.symptomTagParentLayout);
            cardView = view.findViewById(R.id.symptom_tag_card_view_item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int symTagModelID, boolean tagRecordSelected);
    }

    @NonNull
    @Override
    public AdapterTagSymptomSelection.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_symptom_tag_selection_card, parent, false);
        return new AdapterTagSymptomSelection.MyViewHolder(view);
    }

    /**
     * Sets details of recyclerview cards including color based on whether or not the tag record is selected or not.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull AdapterTagSymptomSelection.MyViewHolder holder, int position) {
        String titleFound = symptomTagList.get(position).getSymTagTitle();
        holder.titleText.setText(titleFound);
        holder.symbolText.setText("+");

        // this is where the color is set via comparison between position ID and ArrayList<int>
        if (selectedSymptomTagIDsList.size() > 0){
            for (int idOfAssociatedRecords : selectedSymptomTagIDsList) {
                int idOfSymTagModel = symptomTagList.get(position).getId();
                if(idOfAssociatedRecords == idOfSymTagModel) {
                    holder.cardView.setCardBackgroundColor(standardBlue);
                }

            }
        }

        holder.symptomTagParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /**
         * Functions as toggle for is tag selected or not.  Card color is set accordingly.  Tag record id is sent to AddSymptomActivity to support correct tag associative record data.
         */
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            // id of model associated to clicked card is sent to activity along with toggle status - this supports creating the correct associative table data
            public void onClick(View v) {
                ColorStateList c = holder.cardView.getCardBackgroundColor();
                int i = c.getDefaultColor();

                if (i == lightBlue){
                    holder.cardView.setCardBackgroundColor(standardBlue);
                    onItemClickListener.onItemClick(symptomTagList.get(position).getId(), true);
                }else if (i == standardBlue){
                    holder.cardView.setCardBackgroundColor(lightBlue);
                    onItemClickListener.onItemClick(symptomTagList.get(position).getId(), false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return symptomTagList.size();
    }
}