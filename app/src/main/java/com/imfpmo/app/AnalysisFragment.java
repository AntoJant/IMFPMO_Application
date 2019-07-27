package com.imfpmo.app;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnalysisFragment extends Fragment implements AbsListView.OnScrollListener{


    public AnalysisFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle("Analyse");
        View view = inflater.inflate(R.layout.fragment_analysis, container, false);
        BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_analysis);
        ((MainActivity) getActivity()).FragmentListener(bottomNav);
        AnalysisLoader.getInstance().loadFirst(12);
        AnalysisMonthListAdapter adapter = new AnalysisMonthListAdapter(getActivity(),AnalysisLoader.getInstance().getResults(), getActivity().getSupportFragmentManager());
        ListView monatAnalyseergebnistListView  = view.findViewById(R.id.listview);

        ((DrawerLocker) getActivity()).setDrawerLocked(false);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).show();

        monatAnalyseergebnistListView.setAdapter(adapter);
       monatAnalyseergebnistListView.setOnScrollListener(this);
        return view;
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }
}
