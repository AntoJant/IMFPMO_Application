package com.imfpmo.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class AnalysisPathFragment extends Fragment {
    private AnalysisResultPath weg;
    private AnalysisRideListAdapter listAdapter;
    private ListView lv;
    public AnalysisPathFragment(AnalysisResultPath weg) {
        this.weg = weg;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle("Weg");

        View view = inflater.inflate(R.layout.analyse_tag_fragment, container, false);
        BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_analysis);
        ((MainActivity) getActivity()).FragmentListener(bottomNav);
        lv = view.findViewById(R.id.listview);
        listAdapter = new AnalysisRideListAdapter(weg);
        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                klappAus(i);
            }
        });
        return view;
    }
    private void klappAus(int i){
        listAdapter.setAusgeklappteItems(i);
        lv.invalidateViews();
    }


}
