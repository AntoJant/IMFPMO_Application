package com.imfpmo.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

public class WaysRideFragement extends Fragment {
    private Ride ride;
    private ArrayList<RideMode> modes;
    public WaysRideFragement(Ride ride){
        this.ride = ride;
        modes = new ArrayList<>();
        modes.add(RideMode.WALK);
        modes.add(RideMode.BIKE);
        modes.add(RideMode.OPNV);
        modes.add(RideMode.CAR);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle("Fahrten");

        View view = inflater.inflate(R.layout.fragment_ways_ride, container, false);
        BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_analysis);
        ((MainActivity) getActivity()).FragmentListener(bottomNav);

        TextView date = view.findViewById(R.id.datum);
        date.setText(ride.start.timestamp.substring(8,10)+"."+ride.start.timestamp.substring(5,7)+"." + ride.start.timestamp.substring(0,4));

        TextInputEditText startAdresse = view.findViewById(R.id.startAdresseInputTextEdit);
        TextInputEditText endAdresse = view.findViewById(R.id.endAdresseInputTextEdit);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner2);
        spinner.setAdapter(new RideModeSpinnerAdapter(getActivity(),modes));
        startAdresse.setText(ride.start.name);
        endAdresse.setText(ride.end.name);

        return view;
    }
}
