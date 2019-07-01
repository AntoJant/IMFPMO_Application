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
public class ChangeMailFragment extends Fragment {
    private OnFragmentInteractionListener mListener;


    public ChangeMailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_mail, container, false);

        ((DrawerLocker) getActivity()).setDrawerLocked(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        getActivity().setTitle("E-Mail-Adresse ändern");
        Button changemail = (Button) view.findViewById(R.id.change_mail_changemail_button);
        changemail.setOnClickListener(new View.OnClickListener() {

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
