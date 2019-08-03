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
    private AnalysisResultMonth monatAnalyse;

    public AnalysisMonthFragment(AnalysisResultMonth monat) {
        this.monatAnalyse = monat;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.analyse_monat_fragment, container, false);
        BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_analysis);
        ((MainActivity) Objects.requireNonNull(getActivity())).FragmentListener(bottomNav);
        ListView l = view.findViewById(R.id.listview);
        int monat = monatAnalyse.getDate().get(Calendar.MONTH);
        int jahr = monatAnalyse.getDate().get(Calendar.YEAR);
        switch(monat){
            case 0:getActivity().setTitle("Januar" + " " +jahr);break;
            case 1:getActivity().setTitle("Februar" + " " + jahr);break;
            case 2:getActivity().setTitle("März" + " " + jahr);break;
            case 3:getActivity().setTitle("April" + " " + jahr);break;
            case 4:getActivity().setTitle("Mai" + " " + jahr);break;
            case 5:getActivity().setTitle("Juni" + " " + jahr);break;
            case 6:getActivity().setTitle("Juli" + " " + jahr);break;
            case 7:getActivity().setTitle("August" + " " + jahr);break;
            case 8:getActivity().setTitle("September" + " " + jahr);break;
            case 9:getActivity().setTitle("Oktober" + " " + jahr);break;
            case 10:getActivity().setTitle("November" + " " + jahr);break;
            case 11:getActivity().setTitle("Dezember" + " " + jahr);break;
        }

        AnalysisDayListAdapter listAdapter = new AnalysisDayListAdapter(monatAnalyse);
        l.setAdapter(listAdapter);

        return view;
    }
}