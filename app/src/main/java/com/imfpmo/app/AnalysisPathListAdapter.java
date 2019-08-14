package com.imfpmo.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

public class AnalysisPathListAdapter extends BaseAdapter {
    private Day day;
    private boolean[] isUnfolded;
    public AnalysisPathListAdapter(Day day){
        this.day = day;
        isUnfolded = new boolean[day.getRides().size()];
        for (int i = 0; i< isUnfolded.length; i++)
            isUnfolded[i] = false;
    }


    @Override
    public int getCount() {
        return day.getRides().size();
    }

    @Override
    public Object getItem(int i) {
        return day.getRides().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.analyse_weg_item_layout, viewGroup,false);
        }
        final Path ergebnis = (Path) getItem(i);
        TextView startTime = view.findViewById(R.id.startZeitTextView);
        TextView endTime = view.findViewById(R.id.endZeitTextView);
        TextView startAddress = view.findViewById(R.id.startAdresseTextView);
        TextView endAdress = view.findViewById(R.id.endAdresseTextView);
        Button b = view.findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity =(MainActivity) view.getContext();
                activity.changeToAnalyseFahrtFragment(ergebnis);
            }
        });
        startTime.setText(ergebnis.start.getTimeAsString());
        endTime.setText(ergebnis.end.getTimeAsString()  );
        startAddress.setText(ergebnis.getStartAdress());
        endAdress.setText(ergebnis.getEndAdress());
        ImageView okoGrade = view.findViewById(R.id.okoImageView);
        switch ((int) ergebnis.getOkoGrade()){
            case 1: okoGrade.setImageResource(R.drawable.red_dot_24dp);break;
            case 2: okoGrade.setImageResource(R.drawable.yellow_dot_24dp);break;
            case 3: okoGrade.setImageResource(R.drawable.ic_lens_black_24dp);break;
        }

        ViewPager vp = view.findViewById(R.id.viewPager);
        AnalysisPathDiagramPagerAdapter mp = new AnalysisPathDiagramPagerAdapter(viewGroup.getContext(), ergebnis);
        vp.setAdapter(mp);


        return view;

    }

}
