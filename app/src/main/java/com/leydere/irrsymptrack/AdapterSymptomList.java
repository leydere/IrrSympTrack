package com.leydere.irrsymptrack;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Adapter class for the recyclerview utilized in ActivityRecordsListSymptoms.java.
 */
public class AdapterSymptomList extends RecyclerView.Adapter<AdapterSymptomList.MyViewHolder> {
    private final Context context;
    private ArrayList<ModelSymptom> symptomList;

    public AdapterSymptomList(ArrayList<ModelSymptom> symptomList, Context context){
        this.symptomList = symptomList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView symptomText;
        private TextView dateText;
        LinearLayout symptomParentLayout;

        public MyViewHolder(final View view){
            super(view);
            symptomText = view.findViewById(R.id.symptom_text_display);
            dateText = view.findViewById(R.id.symptom_under_text_display);
            symptomParentLayout = view.findViewById(R.id.symptomParentLayout);
        }
    }

    @NonNull
    @Override
    public AdapterSymptomList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record_symptom_row, parent, false);
        return new AdapterSymptomList.MyViewHolder(view);
    }

    /**
     * Sets the details displayed in the recyclerview cards. OnClick navigates to ActivityAddSymptom.java.  Edit record variant is utilized by sending the symptom record ID as an extra.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull AdapterSymptomList.MyViewHolder holder, int position) {
        //get time-date and format for use
        String irrTimeDate = symptomList.get(position).getSymTimeDate();
        SimpleDateFormat dbStringToCalendar = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        try {calendar.setTime(dbStringToCalendar.parse(irrTimeDate));
        } catch (Exception e) { }
        CharSequence timeDateCharSequence = DateFormat.format("MM/dd/yyyy hh:mm a", calendar);

        String titleFound = symptomList.get(position).getSymTitle();
        holder.symptomText.setText(titleFound);
        holder.dateText.setText(timeDateCharSequence);
        holder.symptomParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
