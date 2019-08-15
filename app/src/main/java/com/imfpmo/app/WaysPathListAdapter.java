package com.imfpmo.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class WaysPathListAdapter extends BaseAdapter {
    ArrayList<Path> paths;

    public WaysPathListAdapter(ArrayList<Path> paths){
        this.paths = paths;
    }


    public int getCount() {
        return paths.size();
    }

    @Override
    public Object getItem(int i) {
        return paths.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.way_path_item_layout, viewGroup,false);
        }
        final Path ergebnis = (Path) getItem(i);
        TextView startTime = view.findViewById(R.id.startZeitTextView);
        TextView endTime = view.findViewById(R.id.endZeitTextView);
        TextView startAddress = view.findViewById(R.id.startAdresseTextView);
        TextView endAdress = view.findViewById(R.id.endAdresseTextView);
        TextView date = view.findViewById(R.id.date);
        date.setText(ergebnis.start.timestamp.substring(8,10)+"."+ergebnis.start.timestamp.substring(5,7)+"." + ergebnis.start.timestamp.substring(0,4));
        startTime.setText(ergebnis.start.getTimeAsString());
        endTime.setText(ergebnis.end.getTimeAsString()  );
        startAddress.setText(ergebnis.getStartAdress());
        endAdress.setText(ergebnis.getEndAdress());
        return view;
    }
}
