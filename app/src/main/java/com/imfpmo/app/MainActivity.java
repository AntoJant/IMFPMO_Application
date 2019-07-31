package com.imfpmo.app;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener, DrawerLocker, SharedPreferences.OnSharedPreferenceChangeListener {

    public static String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private ArrayList<AnalyseergebnisMonat> analyseErgebnisse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.framelayout, new LoginFragment()).commit();

        //create instance of Usermanagement and check if user is already logged in
        Usermanagement.createInstance(getApplicationContext());
        if (Usermanagement.getInstance().isLoggedIn()) {
            Log.w(TAG, "User is already logged in");
            changeFragment(3);
        }
        //Set Dark Theme Switch to "Off" by default:
        navigationView.getMenu().findItem(R.id.nav_dark_theme).setActionView(new Switch(this));

        ((Switch) navigationView.getMenu().findItem(R.id.nav_dark_theme).getActionView()).setChecked(false);
        ((Switch) navigationView.getMenu().findItem(R.id.nav_dark_theme).getActionView()).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton button, boolean state) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                if (state) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    getDelegate().applyDayNight();
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    getDelegate().applyDayNight();
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
        //Set Tracking Switch to "On" by default:
        navigationView.getMenu().findItem(R.id.nav_tracking).setActionView(new Switch(this));
        final Intent intent = new Intent(this, LocationUpdatesService.class);
        final Context context = this;
        ((Switch) navigationView.getMenu().findItem(R.id.nav_tracking).getActionView()).setChecked(false);
        ((Switch) navigationView.getMenu().findItem(R.id.nav_tracking).getActionView()).setOnCheckedChangeListener((button, state) -> {
                //If Tracking Switch has state "On"
                if (state) {

                    LocationUpdatesService.requestLocationUpdates(this);

                    //If Tracking Switch has state "Off"
                } else {

                    LocationUpdatesService.stopLocationUpdates(this);

                }
        });

        if (!checkPermissions()) {
            requestPermissions();
        }

        //Sachen für die AnalyseDarstellung
        setAnalyseErgebnisse(3);
    }

    public void FragmentListener(BottomNavigationView bottomNav) {
        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }

    public void setDrawerLocked(boolean enabled) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (enabled) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.nav_analysis:
                    fragment = new AnalysisFragment();
                    break;
                case R.id.nav_ways:
                    fragment = new WaysFragment();
                    break;
            }
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.framelayout, fragment).addToBackStack("my_fragment");
                ft.commit();
            }
            return true;
        }

    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            Fragment ways = getSupportFragmentManager().findFragmentById(R.id.nav_ways);
            Fragment analysis = getSupportFragmentManager().findFragmentById(R.id.nav_analysis);
            if (ways != null) {
                bottomNav.setSelectedItemId(R.id.nav_analysis);
            }
            if (analysis != null) {
                bottomNav.setSelectedItemId(R.id.nav_ways);
            }
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        if (id == R.id.nav_home) {
            fragment = new AnalysisFragment();
        } else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
        } else if (id == R.id.nav_logoff) {


            LocationUpdatesService.stopLocationUpdates(this);
            //failsafe if app crashes. service will be restarted at login
            Helpers.setRequestingLocationUpdates(this, false);
            LocationUpdatesService.sendLastDataAndCancelWorker(this);

            Usermanagement.getInstance().logout();

            Log.w("Token after logout", "Token = " + Usermanagement.getInstance().getSecurityToken());
            Toast.makeText(getApplicationContext(), "Erfolgreich abgemeldet", Toast.LENGTH_SHORT).show();
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.framelayout, fragment).addToBackStack("my_fragment");
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void changeFragment(int id) {
        Fragment fragment;
        findViewById(R.id.drawer_layout);
        if (id == 1) {
            fragment = new RegistrationFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.framelayout, fragment).addToBackStack("my_fragment");
            ft.commit();
        }
        if (id == 2) {
            fragment = new ResetPasswordFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.framelayout, fragment).addToBackStack("my_fragment");
            ft.commit();
        }
        if (id == 3) {
            fragment = new AnalysisFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.framelayout, fragment).addToBackStack("my_fragment");
            ft.commit();
        }
        if (id == 4) {
            fragment = new LoginFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.framelayout, fragment).addToBackStack("my_fragment");
            Toast.makeText(getApplicationContext(), "Registrierung erfolgreich", Toast.LENGTH_SHORT).show();
            ft.commit();
        }
        if (id == 5) {
            fragment = new LoginFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.framelayout, fragment).addToBackStack("my_fragment");
            Toast.makeText(getApplicationContext(), "E-Mail versendet", Toast.LENGTH_SHORT).show();
            ft.commit();
        }
        if (id == 6) {
            fragment = new ChangeMailFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.framelayout, fragment).addToBackStack("my_fragment");
            ft.commit();
        }
        if (id == 7) {
            fragment = new ChangePasswordFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            setDrawerLocked(false);
            Objects.requireNonNull(getSupportActionBar()).show();
            ft.replace(R.id.framelayout, fragment).addToBackStack("my_fragment");
            ft.commit();
        }
        if (id == 8) {
            fragment = new LoginFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.framelayout, fragment).addToBackStack("my_fragment");
            Toast.makeText(getApplicationContext(), "Bitte loggen Sie sich erneut ein", Toast.LENGTH_SHORT).show();
            ft.commit();
        }
    }


    private boolean checkPermissions() {
        boolean permissionStateFine = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 29) {
            boolean permissionStateBackground = ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    == PackageManager.PERMISSION_GRANTED;
            return permissionStateFine && permissionStateBackground;
        }
        return permissionStateFine;
    }


    //app works only with all-the-time permissions granted
    @TargetApi(29)
    private void requestPermissions() {

        if (Build.VERSION.SDK_INT >= 29)
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        else
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);

    }

    //method displays message in case of core functionality permissions denied
    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults) {
        Log.w(TAG, "in callback");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE)
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.w(TAG, "permissions granted ");
                // permission was granted
            } else {
                // permission denied
                Log.w(TAG, "permissions denied");
                Snackbar.make(
                        findViewById(R.id.drawer_layout),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE).show();
            }

    }

    @Override
    protected void onDestroy() {
        Log.w(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.w(TAG, "onStop");
        super.onStop();
    }

    //reloads the true state of the service in case of service being killed.
    @Override
    protected void onResume(){
        super.onResume();
        NavigationView navigationView = findViewById(R.id.nav_view);
        Log.w(TAG, "onResume");
        if(Helpers.requestingLocationUpdates(this))
            ((Switch) navigationView.getMenu().findItem(R.id.nav_tracking).getActionView()).setChecked(true);
        else
            ((Switch) navigationView.getMenu().findItem(R.id.nav_tracking).getActionView()).setChecked(false);

    }
    @Override
    protected void onRestart() {
        super.onRestart();

        Log.w(TAG, "onRestart");
    }

    @Override
    protected void onPause() {
        Log.w(TAG, "onPause");
        super.onPause();
    }

    //Methoden für die Darstellung von AnalyseErgebnissen
    //Diese methode laedt die AnalyseErgbinsse des Monats
    private void setAnalyseErgebnisse(int monat) {
        analyseErgebnisse = AnalyseRandomErgebnisMaker.getYear();
    }

    public void getMehrAnalyseErgebnisse(int i) {
        int groesse = analyseErgebnisse.size();
        Calendar lastMonth = analyseErgebnisse.get(groesse - 1).getDate();
        Calendar aktMonth = new GregorianCalendar(lastMonth.get(Calendar.YEAR), lastMonth.get(Calendar.MONTH), 1);
        for (int j = 1; j <= i; j++) {
            aktMonth.add(Calendar.MONTH, -1);
            analyseErgebnisse.add(AnalyseRandomErgebnisMaker.makeMonat(new GregorianCalendar(aktMonth.get(Calendar.YEAR), aktMonth.get(Calendar.MONTH), 1)));
        }
    }

    public ArrayList<AnalyseergebnisMonat> getAnalyseMonate(int lastMonat) {
        Usermanagement usermanagement = Usermanagement.getInstance();
        JsonObject ergebnisObject = usermanagement.getAnalyseErgebnisse(this);
        ArrayList<AnalyseergebnisMonat> monate = new ArrayList<>();
        JsonArray array = ergebnisObject.getAsJsonArray("results");
        for (int i = 0; i < lastMonat; i++) {
            int id = array.get(i).getAsJsonObject().get("id").getAsInt();
            Calendar monat = getCalendarDate(array.get(i).getAsJsonObject().get("timestamp").getAsString());
            monate.add(getAnalyseMonat(monat, id));
        }
        return monate;
    }

    public AnalyseergebnisMonat getAnalyseMonat(int month, int year) {
        Usermanagement usermanagement = Usermanagement.getInstance();
        JsonObject monatObject = usermanagement.getAnalyseWegeMonat(this, month, year);

        return null;
    }

    public AnalyseergebnisMonat getAnalyseMonat(Calendar monat, int analysisid) {
        Usermanagement usermanagement = Usermanagement.getInstance();
        JsonObject analyseObject = usermanagement.getAnalyseErgebnis(this, analysisid);
        ArrayList<AnalyseergebnisTag> analyseergebnisTags = new ArrayList<>();
        int cMonat = monat.get(Calendar.MONTH);
        int cYear = monat.get(Calendar.YEAR);
        for (int i = 1; i <= monat.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            analyseergebnisTags.add(getAnaylseTag(new GregorianCalendar(cYear, cMonat, i)));
        }
        int car = analyseObject.get("car").getAsInt();
        int bike = analyseObject.get("bike").getAsInt();
        int opnv = analyseObject.get("opnv").getAsInt();
        int fuss = analyseObject.get("foot").getAsInt();
        String besteAlternative = analyseObject.get("bestAlternative").getAsString();
        int emmisionen = analyseObject.get("emissions").getAsInt();
        String ampel = analyseObject.get("ampel").getAsString();
        int okoBewertung = 1;
        switch (ampel) {
            case "red":
                okoBewertung = 1;
            case "yellow":
                okoBewertung = 2;
            case "green":
                okoBewertung = 3;
        }

        return new AnalyseergebnisMonat(1, analysisid, monat, bike, opnv, car, fuss, emmisionen, okoBewertung, analyseergebnisTags);
    }

    public AnalyseergebnisTag getAnaylseTag(Calendar tag) {
        Usermanagement usermanagement = Usermanagement.getInstance();
        JsonObject tagObject = usermanagement.getAnalyseErgebnisseTag(this, tag);
        JsonArray wegArray = tagObject.get("paths").getAsJsonArray();
        ArrayList<AnalyseergebnisWeg> wege = new ArrayList<>();
        for (int i = 0; i < wegArray.size(); i++) {
            JsonObject wegObject = wegArray.get(i).getAsJsonObject();
            int wegID = wegObject.get("id").getAsInt();
            wege.add(getAnalyseWeg(wegID));
        }
        return new AnalyseergebnisTag(wege, tag);
    }

    public AnalyseergebnisWeg getAnalyseWeg(int wegId) {
        Usermanagement usermanagement = Usermanagement.getInstance();
        JsonObject weg = usermanagement.getAnalyseErgebnisWeg(this, Integer.toString(wegId));
        JsonArray fahten = weg.getAsJsonArray("rides");
        JsonObject wegStart = weg.get("start").getAsJsonObject();
        JsonObject wegEnd = weg.get("end").getAsJsonObject();
        ArrayList<AnalyseergebnisFahrt> fahrtenArray = new ArrayList<>();
        for (int i = 0; i < fahten.size(); i++) {

            JsonObject fahrt = fahten.get(i).getAsJsonObject();
            JsonObject start = fahrt.getAsJsonObject("start");
            JsonObject ende = fahrt.getAsJsonObject("end");

            //int distanz = fahrt.get("distance").getAsInt();
            int distanz = new Random().nextInt(100);
            String mode = fahrt.get("mode").getAsString();
            int emissions = fahrt.get("emissions").getAsInt();
            String ampel = fahrt.get("ampel").getAsString();
            String startName = start.get("name").getAsString();
            String endName = ende.get("name").getAsString();
            Calendar startDate = getCalendarDate(start.get("timestamp").getAsString());
            Calendar endDate = getCalendarDate(ende.get("timestamp").getAsString());
            FahrtModi modi;
            switch (mode) {
                case "car":
                    modi = FahrtModi.AUTO;
                    break;
                case "opnv":
                    modi = FahrtModi.OPNV;
                    break;
                case "train":
                    modi = FahrtModi.OPNV;
                    break;
                case "bus":
                    modi = FahrtModi.OPNV;
                    break;
                case "walk":
                    modi = FahrtModi.WALK;
                    break;
                case "bike":
                    modi = FahrtModi.FAHRRAD;
                    break;
                default:
                    modi = FahrtModi.FAHRRAD;
            }
            int okoBewertung = 1;
            switch (ampel) {
                case "red":
                    okoBewertung = 1;
                case "yellow":
                    okoBewertung = 2;
                case "green":
                    okoBewertung = 3;
            }
            int dauerStunde = startDate.get(Calendar.HOUR_OF_DAY) - endDate.get(Calendar.HOUR_OF_DAY);
            int dauerMinute = Math.abs(startDate.get(Calendar.MINUTE) - endDate.get(Calendar.MINUTE));
            fahrtenArray.add(new AnalyseergebnisFahrt(wegId, modi, FahrtModi.WALK, okoBewertung, emissions, dauerStunde * 60 + dauerMinute, startDate, endDate, startName, endName, distanz, 1));
        }
        return new AnalyseergebnisWeg(fahrtenArray, wegId, getCalendarDate(wegStart.get("timestamp").getAsString()), getCalendarDate(wegEnd.get("timestamp").getAsString()), wegStart.get("name").getAsString(), wegEnd.get("name").getAsString());
    }

    public void changeToAnalyseMonatFragment(Calendar monat) {
        int i = -1;
        for (int j = 0; j < analyseErgebnisse.size(); j++) {
            if (analyseErgebnisse.get(j).getDate().get(Calendar.MONTH) == monat.get(Calendar.MONTH) && analyseErgebnisse.get(j).getDate().get(Calendar.YEAR) == monat.get(Calendar.YEAR)) {
                i = j;
            }
        }
        if (i != -1) {
            AnalysisMonatFragment temp = new AnalysisMonatFragment(analyseErgebnisse.get(i));
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.framelayout, temp);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    public void changeToAnalyseTagFragment(Calendar monat) {
        int i = -1;
        int r = -1;
        for (int j = 0; j < analyseErgebnisse.size(); j++) {
            if (analyseErgebnisse.get(j).getDate().get(Calendar.MONTH) == monat.get(Calendar.MONTH) && analyseErgebnisse.get(j).getDate().get(Calendar.YEAR) == monat.get(Calendar.YEAR)) {
                i = j;
                for (int l = 0; l < analyseErgebnisse.get(i).getTage().size(); l++) {
                    int tag = analyseErgebnisse.get(i).getTage().get(l).getTag().get(Calendar.DAY_OF_MONTH);
                    if (tag == monat.get(Calendar.DAY_OF_MONTH)) {
                        r = l;
                    }
                }
            }
        }
        if (i != -1) {
            AnalysisTagFragment temp = new AnalysisTagFragment(analyseErgebnisse.get(i).getTage().get(r));
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.framelayout, temp);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    public void changeToAnalyseFahrtFragment(AnalyseergebnisWeg weg) {
        AnalysisFahrtFragment temp = new AnalysisFahrtFragment(weg);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout, temp);
        ft.addToBackStack(null);
        ft.commit();
    }

    public ArrayList<AnalyseergebnisMonat> getErgebnisse() {
        return analyseErgebnisse;
    }

    public Calendar getCalendarDate(String date) {
        return new GregorianCalendar(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5, 7)) - 1, Integer.parseInt(date.substring(8, 10)), Integer.parseInt(date.substring(11, 13)), Integer.parseInt(date.substring(14, 16)));
    }


    //method changes tracking button state in case of stopping service from notification while activity is alive
    //same scenario if OS kills Activity and Service
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        if(s.equals(Helpers.KEY_REQUESTING_LOCATION_UPDATES)) {
            NavigationView navigationView = findViewById(R.id.nav_view);

            if (Helpers.requestingLocationUpdates(this))
                ((Switch) navigationView.getMenu().findItem(R.id.nav_tracking).getActionView()).setChecked(true);
            else
                ((Switch) navigationView.getMenu().findItem(R.id.nav_tracking).getActionView()).setChecked(false);
        }

    }
}
