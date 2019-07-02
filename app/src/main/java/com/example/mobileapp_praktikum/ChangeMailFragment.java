package com.example.mobileapp_praktikum;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeMailFragment extends Fragment {
    private OnFragmentInteractionListener mListener;


    public ChangeMailFragment() {
        // Required empty public constructor
}
    private static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_mail, container, false);

        final EditText mailfield = view.findViewById(R.id.change_mail_mail_textfield);

        ((DrawerLocker) Objects.requireNonNull(getActivity())).setDrawerLocked(false);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).show();

        getActivity().setTitle("E-Mail-Adresse ändern");
        Button changemail = view.findViewById(R.id.change_mail_changemail_button);
        changemail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isValidEmail(mailfield.getText().toString())) {
                    mListener.changeFragment(8);
                }
                if (!isValidEmail(mailfield.getText().toString())) {
                    mailfield.setError("Geben Sie eine valide E-Mail-Adresse an");
                }
            }
        });
        mListener = new OnFragmentInteractionListener() {
            @Override
            public void changeFragment(int id) {
                ((MainActivity) Objects.requireNonNull(getActivity())).changeFragment(id);
            }
        };
        // Inflate the layout for this fragment
        return view;
    }


}
