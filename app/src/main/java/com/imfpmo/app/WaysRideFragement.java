package com.imfpmo.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

        int ride_mode = 0;
        switch (ride.mode) {
            case "walk":
                ride_mode = 0;
                break;
            case "bike":
                ride_mode = 1;
                break;
            case "opnv":
                ride_mode = 2;
                break;
            case "car":
                ride_mode = 3;
                break;
        }

        spinner.setSelection(ride_mode);

        Button save_btn = view.findViewById(R.id.save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ride_mode = "walk";
                switch (spinner.getSelectedItemPosition()) {
                    case 0:
                        ride_mode = "walk";
                        break;
                    case 1:
                        ride_mode = "bike";
                        break;
                    case 2:
                        ride_mode = "opnv";
                        break;
                    case 3:
                        ride_mode = "car";
                        break;
                }

                Usermanagement.getInstance().patchRide(WaysRideFragement.this.getActivity(), WaysRideFragement.this.ride.id, startAdresse.getText().toString(), endAdresse.getText().toString(), WaysRideFragement.this.ride.start.timestamp, ride_mode);
                Toast.makeText(WaysRideFragement.this.getActivity(), "Saved", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
