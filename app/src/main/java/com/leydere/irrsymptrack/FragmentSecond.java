package com.leydere.irrsymptrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FragmentSecond extends Fragment {

    ArrayList<ModelSymptom> allSymptomsList;
    DatabaseHelper databaseHelper;
    RecyclerView symptomRecyclerView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View rootView = inflater.inflate(R.layout.fragment_second, container, false);
        //TODO how to I findViewById for the irritantRecyclerView? *solved?*
        symptomRecyclerView = (RecyclerView)rootView.findViewById(R.id.symptomRecyclerView);

        databaseHelper = new DatabaseHelper(getActivity());
        allSymptomsList = getAllSymptoms();

        setAdapter();



        // Inflate the layout for this fragment
        return rootView;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_second_navToFirst).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentSecond.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        view.findViewById(R.id.fab_navToAddSymptom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentAddSymptom = new Intent(getActivity(), ActivityAddSymptom.class);
                startActivity(intentAddSymptom);

            }
        });


    }

    private ArrayList<ModelSymptom> getAllSymptoms(){
        ArrayList<ModelSymptom> arrayListToReturn = new ArrayList<ModelSymptom>();
        arrayListToReturn.addAll(databaseHelper.getAllSymptoms());
        return arrayListToReturn;
    }

    private void setAdapter() {
        AdapterSymptomList adapter = new AdapterSymptomList(allSymptomsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        symptomRecyclerView.setLayoutManager(layoutManager);
        symptomRecyclerView.setItemAnimator(new DefaultItemAnimator());
        symptomRecyclerView.setAdapter(adapter);
    }

}