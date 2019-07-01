package com.example.mobileapp_praktikum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AnalysisMonatFragment extends Fragment {
    public AnalysisMonatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Analyse");
        View view = inflater.inflate(R.layout.fragment_monat_analysis, container, false);
        BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_analysis);
        ((MainActivity) getActivity()).FragmentListener(bottomNav);

        AnalyseMonatListAdapter adapter = new AnalyseMonatListAdapter(getActivity(), AnalyseRandomErgebnisMaker.getYear(), getActivity().getSupportFragmentManager());

        return view;
    }
}