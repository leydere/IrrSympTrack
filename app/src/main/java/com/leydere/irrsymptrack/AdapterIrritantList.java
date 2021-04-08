package com.leydere.irrsymptrack;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AdapterIrritantList extends RecyclerView.Adapter<AdapterIrritantList.MyViewHolder> {
    private final Context context;
    private ArrayList<ModelIrritant> irritantList;

    public AdapterIrritantList(ArrayList<ModelIrritant> irritantList, Context context){
        this.irritantList = irritantList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView irritantText;
        private TextView dateText;
        LinearLayout irritantParentLayout;

        public MyViewHolder(final View view){
            super(view);
            irritantText = view.findViewById(R.id.irritant_text_display);
            dateText = view.findViewById(R.id.irritant_under_text_display);
            irritantParentLayout = view.findViewById(R.id.irritantParentLayout);
        }
    }

    @NonNull
    @Override
    public AdapterIrritantList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record_irritant_row, parent, false);
        return new AdapterIrritantList.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterIrritantList.MyViewHolder holder, int position) {
        //TODO improvements to card UI object data can be made here
        //get time-date and format for use
        String irrTimeDate = irritantList.get(position).getIrrTimeDate();
        SimpleDateFormat dbStringToCalendar = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        try {calendar.setTime(dbStringToCalendar.parse(irrTimeDate));
        } catch (Exception e) { }
        CharSequence timeDateCharSequence = DateFormat.format("MM/dd/yyyy hh:mm a", calendar);

        String titleFound = irritantList.get(position).getIrrTitle();
        holder.irritantText.setText(titleFound);
        holder.dateText.setText(timeDateCharSequence);
        holder.irritantParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send control to the add-edit symptom activity?
                Intent intent = new Intent(context, ActivityAddIrritant.class);
                intent.putExtra("id", irritantList.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return irritantList.size();
    }
}
