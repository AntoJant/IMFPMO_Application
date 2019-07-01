package com.example.mobileapp_praktikum;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {
    private OnFragmentInteractionListener mListener;


    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        ((DrawerLocker) getActivity()).setDrawerLocked(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        getActivity().setTitle("E-Mail-Adresse ändern");
        Button changepassword = (Button) view.findViewById(R.id.change_password_changepassword_button);
        changepassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mListener.changeFragment(8);
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
