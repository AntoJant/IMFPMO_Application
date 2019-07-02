package com.example.mobileapp_praktikum;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnalysisFragment extends Fragment {


    public AnalysisFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Analyse");
        View view = inflater.inflate(R.layout.fragment_analysis, container, false);
        BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_analysis);
        ((MainActivity) getActivity()).FragmentListener(bottomNav);

        AnalyseMonatListAdapter adapter = new AnalyseMonatListAdapter(getActivity(),((MainActivity)getActivity()).getErgebnisse(), getActivity().getSupportFragmentManager());
        ListView monatAnalyseergebnistListView  =(ListView) view.findViewById(R.id.monatAnalyseergebnistListView);

        monatAnalyseergebnistListView.setAdapter(adapter);
        //must happen only once. maybe check if service already running
        Context context = ((MainActivity) getActivity());
        context.startService(new Intent(context, LocationUpdatesService.class));
        return view;

    }
}
