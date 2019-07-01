package com.example.mobileapp_praktikum;


import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {
    private OnFragmentInteractionListener mListener;


    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        getActivity().setTitle("Registrieren");
        Button registerButton = (Button) view.findViewById(R.id.register_register_button);
        final CheckBox registercheckbox = (CheckBox) view.findViewById(R.id.register_checkbox);

        ((DrawerLocker) getActivity()).setDrawerLocked(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (registercheckbox.isChecked()) {
                    mListener.changeFragment(4);
                } else {
                    registercheckbox.setError("");
                    registercheckbox.setTextColor(Color.RED);
                }
            }
        });
        mListener = new OnFragmentInteractionListener() {
            @Override
            public void changeFragment(int id) {
                ((MainActivity) getActivity()).changeFragment(id);
            }
        };
        // Inflate the layout for this fragment
        return view;
    }

}
