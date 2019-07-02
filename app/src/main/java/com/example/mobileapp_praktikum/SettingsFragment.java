package com.example.mobileapp_praktikum;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    private OnFragmentInteractionListener mListener;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
        Objects.requireNonNull(getActivity()).setTitle("Einstellungen");
        Preference changeMail = findPreference("change_mail");
        Preference changePassword = findPreference("change_password");

        ((DrawerLocker) getActivity()).setDrawerLocked(false);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).show();

        changeMail.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                mListener.changeFragment(6);
                return true;
            }
        });
        changePassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                mListener.changeFragment(7);
                return true;
            }
        });
        mListener = new OnFragmentInteractionListener() {
            @Override
            public void changeFragment(int id) {
                ((MainActivity) Objects.requireNonNull(getActivity())).changeFragment(id);
            }
        };
    }
}
