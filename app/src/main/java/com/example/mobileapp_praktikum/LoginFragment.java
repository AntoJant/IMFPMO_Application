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
public class LoginFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        getActivity().setTitle("Anmelden");
        Button registerButton = (Button) view.findViewById(R.id.login_register_button);
        Button forgotPasswordButton = (Button) view.findViewById(R.id.login_forgot_password_button);
        Button loginButton = (Button) view.findViewById(R.id.login_login_button);

        ((DrawerLocker) getActivity()).setDrawerLocked(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mListener.changeFragment(1);
            }
        });
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mListener.changeFragment(2);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mListener.changeFragment(3);
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
