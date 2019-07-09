package com.imfpmo.app;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {
    private OnFragmentInteractionListener mListener;


    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    private static boolean passwordsMatch(CharSequence password1, CharSequence password2) {
        return !TextUtils.isEmpty(password1) && password1.equals(password2);
    }

    private static boolean passwordLength(CharSequence password) {
        return password.length() > 5;
    }

    private static boolean passwordNotEmpty(CharSequence password1, CharSequence password2, CharSequence password3) {
        return !(TextUtils.isEmpty(password1) && TextUtils.isEmpty(password2) && TextUtils.isEmpty(password3));
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        final EditText oldpassword = view.findViewById(R.id.change_password_oldpassword_textfield);
        final EditText newpassword = view.findViewById(R.id.change_password_newpassword_textfield);
        final EditText confirmnewpassword = view.findViewById(R.id.change_password_confirmnewpassword_textfield);

        ((DrawerLocker) Objects.requireNonNull(getActivity())).setDrawerLocked(false);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).show();

        getActivity().setTitle("Passwort ändern");
        Button changepassword = view.findViewById(R.id.change_password_changepassword_button);
        changepassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (passwordsMatch(newpassword.getText().toString(), confirmnewpassword.getText().toString()) &&
                        passwordLength(newpassword.getText().toString()) && passwordNotEmpty(newpassword.getText().toString(), confirmnewpassword.getText().toString(), oldpassword.getText().toString())) {
                    mListener.changeFragment(8);
                }
                if (!passwordLength(newpassword.getText().toString())) {
                    newpassword.setError("Das Passwort muss mindestens 6 Zeichen lang sein");
                    confirmnewpassword.setError("Das Passwort muss mindestens 6 Zeichen lang sein");
                }
                if (!passwordsMatch(newpassword.getText().toString(), confirmnewpassword.getText().toString())) {
                    newpassword.setError("Die Passwörter stimmen nicht überein");
                    confirmnewpassword.setError("Die Passwörter stimmen nicht überein");
                    confirmnewpassword.setText("");
                }
                if (!passwordNotEmpty(newpassword.getText().toString(), confirmnewpassword.getText().toString(), oldpassword.getText().toString())) {
                    newpassword.setError("Das neue Passwort muss mindestens 6 Zeichen lang sein");
                    confirmnewpassword.setError("Das neue Passwort muss mindestens 6 Zeichen lang sein");
                    oldpassword.setError("Das alte Passwort muss mindestens 6 Zeichen lang sein");
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
