package com.imfpmo.app;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteUserFragment extends Fragment {
    private OnFragmentInteractionListener mListener;


    public DeleteUserFragment() {
        // Required empty public constructor
    }

    /*private static boolean passwordsMatch(CharSequence password1, CharSequence password2) {
        return !TextUtils.isEmpty(password1) && password1.equals(password2);
    }*/

    private static boolean passwordLength(CharSequence password) {
        return password.length() > 5;
    }

    /*private static boolean passwordNotEmpty(CharSequence password1, CharSequence password2, CharSequence password3) {
        return !(TextUtils.isEmpty(password1) && TextUtils.isEmpty(password2) && TextUtils.isEmpty(password3));
    }*/


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_user, container, false);

        final EditText password = view.findViewById(R.id.delete_user_password_textfield);
        //final EditText newpassword = view.findViewById(R.id.change_password_newpassword_textfield);
        //final EditText confirmnewpassword = view.findViewById(R.id.change_password_confirmnewpassword_textfield);

        ((DrawerLocker) Objects.requireNonNull(getActivity())).setDrawerLocked(false);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).show();

        getActivity().setTitle("Account löschen");
        Button changepassword = view.findViewById(R.id.delete_user_button);
        changepassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (passwordLength(password.getText().toString())) {
                    switch (Usermanagement.getInstance().deleteUser(password.getText().toString(),getContext())) {
                        case Usermanagement.OPERATION_SUCCESSFUL:
                            mListener.changeFragment(9);
                            break;
                        case Usermanagement.OPERATION_FAILED:
                            Toast.makeText(getContext(),"Passwort ist falsch!", Toast.LENGTH_LONG).show();
                            password.setText("");
                            break;
                        case Usermanagement.NO_INTERNET_CONNECTION:
                            Toast.makeText(getContext(),"Keine Verbindung zum Internet möglich!", Toast.LENGTH_LONG).show();
                            password.setText("");
                            break;
                        case Usermanagement.COULDNT_REACH_SERVER:
                            Toast.makeText(getContext(),"Server konnte nicht erreicht werden!", Toast.LENGTH_LONG).show();
                            password.setText("");
                            break;
                        case 10:
                            Toast.makeText(getContext(),"Passwort ist falsch!", Toast.LENGTH_LONG).show();
                            password.setText("");
                            break;
                    }

                }
                else {
                    password.setError("Das Passwort muss mindestens 6 Zeichen lang sein");
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
