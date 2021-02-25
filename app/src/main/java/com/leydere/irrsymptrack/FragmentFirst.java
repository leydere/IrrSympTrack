package com.leydere.irrsymptrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FragmentFirst extends Fragment {

    ArrayList<ModelIrritant> allIrritantsList;

    DatabaseHelper databaseHelper;
    RecyclerView irritantRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_first, container, false);
        //TODO how to I findViewById for the irritantRecyclerView? *solved?*
        irritantRecyclerView = (RecyclerView)rootView.findViewById(R.id.irritantRecyclerView);

        databaseHelper = new DatabaseHelper(getActivity());
        allIrritantsList = getAllIrritants();

        setAdapter();
        
        // Inflate the layout for this fragment
        return rootView;


    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first_navToSecond).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentFirst.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        view.findViewById(R.id.fab_navToAddIrritant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentAddIrritant = new Intent(getActivity(), ActivityAddIrritant.class);
                startActivity(intentAddIrritant);

            }
        });

    }

   private ArrayList<ModelIrritant> getAllIrritants(){
        ArrayList<ModelIrritant> arrayListToReturn = new ArrayList<ModelIrritant>();
        arrayListToReturn.addAll(databaseHelper.getAllIrritants());
        return arrayListToReturn;
    }

    private void setAdapter() {
        AdapterIrritantList adapter = new AdapterIrritantList(allIrritantsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        irritantRecyclerView.setLayoutManager(layoutManager);
        irritantRecyclerView.setItemAnimator(new DefaultItemAnimator());
        irritantRecyclerView.setAdapter(adapter);
    }


}