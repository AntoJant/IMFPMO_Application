package com.example.mobileapp_praktikum;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogOffFragment extends Fragment {


    public LogOffFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Objects.requireNonNull(getActivity()).setTitle("Abmelden");

        ((DrawerLocker) getActivity()).setDrawerLocked(false);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).show();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_off, container, false);
    }

}
