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

public class AnalysisMonthFragment extends Fragment {
    private AnalysisResultMonth monthAnalysis;

    public AnalysisMonthFragment(AnalysisResultMonth month) {
        this.monthAnalysis = month;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.analyse_monat_fragment, container, false);
        BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_analysis);
        ((MainActivity) Objects.requireNonNull(getActivity())).FragmentListener(bottomNav);
        ListView l = view.findViewById(R.id.listviewMonth);
        int month = monthAnalysis.getDate().get(Calendar.MONTH);
        int year = monthAnalysis.getDate().get(Calendar.YEAR);
        switch(month){
            case 0:getActivity().setTitle("Januar" + " " +year);break;
            case 1:getActivity().setTitle("Februar" + " " + year);break;
            case 2:getActivity().setTitle("März" + " " + year);break;
            case 3:getActivity().setTitle("April" + " " + year);break;
            case 4:getActivity().setTitle("Mai" + " " + year);break;
            case 5:getActivity().setTitle("Juni" + " " + year);break;
            case 6:getActivity().setTitle("Juli" + " " + year);break;
            case 7:getActivity().setTitle("August" + " " + year);break;
            case 8:getActivity().setTitle("September" + " " + year);break;
            case 9:getActivity().setTitle("Oktober" + " " + year);break;
            case 10:getActivity().setTitle("November" + " " + year);break;
            case 11:getActivity().setTitle("Dezember" + " " + year);break;
        }

        AnalysisDayListAdapter listAdapter = new AnalysisDayListAdapter(monthAnalysis);
        l.setAdapter(listAdapter);

        return view;
    }
}