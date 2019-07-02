package com.example.mobileapp_praktikum;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class AnalysisFahrtFragment extends Fragment {
    private  AnalyseergebnisWeg weg;

    public AnalysisFahrtFragment(AnalyseergebnisWeg weg) {
        this.weg = weg;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Weg");

        View view = inflater.inflate(R.layout.analyse_tag_fragment, container, false);
        BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_analysis);
        ((MainActivity) getActivity()).FragmentListener(bottomNav);
        ListView l = (ListView) view.findViewById(R.id.listview);

        AnalyseFahrtListAdapter listAdapter = new AnalyseFahrtListAdapter(weg);
        l.setAdapter(listAdapter);
        return view;
    }
}
