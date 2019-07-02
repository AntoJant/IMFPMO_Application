package com.example.mobileapp_praktikum;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class WaysFragment extends Fragment {


    public WaysFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle("Fahrten");
        View view = inflater.inflate(R.layout.fragment_ways, container, false);
        BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_ways);

        Spinner spinner;
        spinner = view.findViewById(R.id.ways_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.months, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setNotifyOnChange(true);
        spinner.setAdapter(adapter);

        ((DrawerLocker) getActivity()).setDrawerLocked(false);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).show();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getActivity(), item + " ausgewählt ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ((MainActivity) getActivity()).FragmentListener(bottomNav);

        return view;
    }

}
