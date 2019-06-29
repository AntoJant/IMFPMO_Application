package com.example.mobileapp_praktikum;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogOffFragment extends Fragment {


    public LogOffFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Abmelden");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_off, container, false);
    }

}
