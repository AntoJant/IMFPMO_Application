package com.example.mobileapp_praktikum;


import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {
    private OnFragmentInteractionListener mListener;


    public RegistrationFragment() {
        // Required empty public constructor
    }

    private static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private static boolean passwordsMatch(CharSequence password1, CharSequence password2) {
        return !TextUtils.isEmpty(password1) && password1.equals(password2);
    }

    private static boolean passwordLength(CharSequence password) {
        return password.length() > 5;
    }

    private static boolean passwordNotEmpty(CharSequence password1, CharSequence password2) {
        return !(TextUtils.isEmpty(password1) && TextUtils.isEmpty(password2));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        Objects.requireNonNull(getActivity()).setTitle("Registrieren");
        Button registerButton = view.findViewById(R.id.register_register_button);
        final CheckBox registercheckbox = view.findViewById(R.id.register_checkbox);

        final EditText mailfield = view.findViewById(R.id.register_mail_textfield);

        final EditText passwordfield = view.findViewById(R.id.register_password_textfield);

        final EditText confirmpasswordfield = view.findViewById(R.id.register_confirmpassword_textfield);

        ((DrawerLocker) getActivity()).setDrawerLocked(true);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).hide();

        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!isValidEmail(mailfield.getText().toString())) {
                    mailfield.setError("Geben Sie eine valide E-Mail-Adresse an");
                }
                if (!passwordLength(passwordfield.getText().toString())) {
                    passwordfield.setError("Das Passwort muss mindestens 6 Zeichen lang sein");
                    confirmpasswordfield.setError("Das Passwort muss mindestens 6 Zeichen lang sein");
                }
                if (!passwordsMatch(passwordfield.getText().toString(), confirmpasswordfield.getText().toString())) {
                    passwordfield.setError("Die Passwörter stimmen nicht überein");
                    confirmpasswordfield.setError("Die Passwörter stimmen nicht überein");
                    confirmpasswordfield.setText("");
                }
                if (!passwordNotEmpty(passwordfield.getText().toString(), confirmpasswordfield.getText().toString())) {
                    passwordfield.setError("Das Passwort muss mindestens 6 Zeichen lang sein");
                    confirmpasswordfield.setError("Das Passwort muss mindestens 6 Zeichen lang sein");
                }
                if (!registercheckbox.isChecked()) {
                    registercheckbox.setTextColor(Color.RED);
                }
                if (registercheckbox.isChecked() && isValidEmail(mailfield.getText().toString()) && passwordsMatch(passwordfield.getText().toString(), confirmpasswordfield.getText().toString()) &&
                        passwordLength(passwordfield.getText().toString()) && passwordNotEmpty(passwordfield.getText().toString(), confirmpasswordfield.getText().toString())) {
                    int result = Usermanagement.getInstance().register(mailfield.getText().toString(), passwordfield.getText().toString(),getContext());

                    if(result == Usermanagement.OPERATION_SUCCESSFUL) {
                        Toast.makeText(getContext(),"Registrierung erfolgreich!", Toast.LENGTH_LONG).show();
                        mListener.changeFragment(4);
                    }
                    else if(result == Usermanagement.OPERATION_FAILED){
                        Toast.makeText(getContext(),"Account mit dieser E-Mail Adresse existiert bereits!", Toast.LENGTH_LONG).show();
                        mailfield.setText("");
                        passwordfield.setText("");
                    }
                    else if(result == Usermanagement.NO_INTERNET_CONNECTION){
                        Toast.makeText(getContext(),"Keine Verbindung zum Internet möglich!", Toast.LENGTH_LONG).show();
                        mailfield.setText("");
                        passwordfield.setText("");
                    }
                    else if(result == Usermanagement.COULDNT_REACH_SERVER){
                        Toast.makeText(getContext(),"Server konnte nicht erreicht werden!", Toast.LENGTH_LONG).show();
                        mailfield.setText("");
                        passwordfield.setText("");
                    }


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
