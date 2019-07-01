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
public class ResetPasswordFragment extends Fragment {
    private OnFragmentInteractionListener mListener;


    public ResetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        ((DrawerLocker) getActivity()).setDrawerLocked(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        getActivity().setTitle("Passwort zurücksetzen");
        Button sendmail = (Button) view.findViewById(R.id.reset_password_sendmail_button);
        sendmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mListener.changeFragment(5);
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
