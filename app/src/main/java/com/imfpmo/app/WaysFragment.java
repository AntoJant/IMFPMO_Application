package com.imfpmo.app;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class WaysFragment extends Fragment {
    private WaysPathListAdapter adapter;
    private final static String NO_DATA_ALERT_TEXT = "Es liegen zurzeit keine Analyseergebnisse vor.";
    private final static String CONNECTION_ERROR_ALERT_TEXT = "Es trafen Probleme beim Laden der Analyseergebnisse vor.";
    public WaysFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle("Fahrten");
        View view = inflater.inflate(R.layout.fragment_ways, container, false);
        BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_ways);
        int proofBit = AnalysisLoader.getInstance().refresh();
        if(proofBit == 1){
            showInformationDialog(NO_DATA_ALERT_TEXT);
        }else if(proofBit == 2){
            showInformationDialog(CONNECTION_ERROR_ALERT_TEXT);
        }
        adapter = new WaysPathListAdapter(AnalysisLoader.getInstance().getPaths());
        ListView pathListView  = view.findViewById(R.id.listView);
        pathListView.setAdapter(adapter);
        pathListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainActivity)getActivity()).changeToWayPathFragment(AnalysisLoader.getInstance().getPaths().get(i));
            }
        });


        ((DrawerLocker) getActivity()).setDrawerLocked(false);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).show();


        ((MainActivity) getActivity()).FragmentListener(bottomNav);

        return view;
    }

    public void showInformationDialog(String text){
        AlertDialog.Builder a_builder = new AlertDialog.Builder(this.getActivity());
        a_builder.setMessage(text)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }) ;
        AlertDialog alert = a_builder.create();
        alert.setTitle("Information");
        alert.show();
    }
}
