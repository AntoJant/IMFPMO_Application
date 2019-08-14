package com.imfpmo.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.Objects;

public class AnalysisDayFragment extends Fragment {
    private Day dayAnalysis;

    public AnalysisDayFragment(Day day) {
        this.dayAnalysis = day;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle("Analyse");

        View view = inflater.inflate(R.layout.analyse_tag_fragment, container, false);
        BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_analysis);
        ((MainActivity) getActivity()).FragmentListener(bottomNav);
        ListView l = view.findViewById(R.id.listviewMonth);

        int month = dayAnalysis.getDay().get(Calendar.MONTH)+1;
        int year = dayAnalysis.getDay().get(Calendar.YEAR);
        int day = dayAnalysis.getDay().get(Calendar.DAY_OF_MONTH);
        getActivity().setTitle(day + "." + month+ "." + year);

        AnalysisPathListAdapter listAdapter = new AnalysisPathListAdapter(dayAnalysis);
        l.setAdapter(listAdapter);
        return view;
    }
}
