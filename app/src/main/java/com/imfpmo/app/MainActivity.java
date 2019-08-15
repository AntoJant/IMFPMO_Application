package com.imfpmo.app;

import android.Manifest;
import android.annotation.TargetApi;
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
import androidx.annotation.Nullable;
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

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener, DrawerLocker, SharedPreferences.OnSharedPreferenceChangeListener {

    public static String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

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
        AnalysisLoader.createInstance(this);
    }

    public void FragmentListener(BottomNavigationView bottomNav) {
        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }

    public void setDrawerLocked(boolean enabled) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (enabled) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
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
            fragment = new DeleteUserFragment();
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
        if (id == 9) {
            fragment = new LoginFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.framelayout, fragment).addToBackStack("my_fragment");
            Toast.makeText(getApplicationContext(), "Account erfolgreich gelöscht", Toast.LENGTH_SHORT).show();
            ft.commit();
        }
    }


    /**
     * Checks background locations that are required for core functionality for both Android Q and
     * pre Android Q OS versions.
     *
     * @return - true if permissions were already granted
     */
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


    /**
     * Requests permissions for Fine Location Tracking and for Background Location Tracking (Android Q)
     */
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

    /**
     * Callback from Request Permissions method. Displays message in case of core functionality permissions denied.
     * If denied then an app restart would ask for them again. If never ask again is checked then only app reinstallation
     * or changing app-phone setting will solve the issue.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.w(TAG, "in callback");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE)
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && (!(Build.VERSION.SDK_INT >= 29 && !(grantResults[1] == PackageManager.PERMISSION_GRANTED)))) {
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
    protected void onResume() {
        super.onResume();
        NavigationView navigationView = findViewById(R.id.nav_view);
        Log.w(TAG, "onResume");
        if (Helpers.requestingLocationUpdates(this))
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


    public void changeToAnalyseMonatFragment(Calendar monat) {
        int i = -1;
        for (int j = 0; j < AnalysisLoader.getInstance().getResults().size(); j++) {
            if (AnalysisLoader.getInstance().getResults().get(j).getDate().get(Calendar.MONTH) == monat.get(Calendar.MONTH) && AnalysisLoader.getInstance().getResults().get(j).getDate().get(Calendar.YEAR) == monat.get(Calendar.YEAR)) {
                i = j;
                break;
            }
        }
        if (i != -1) {
            AnalysisMonthFragment temp = new AnalysisMonthFragment(AnalysisLoader.getInstance().getResults().get(i));
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.framelayout, temp);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    public void changeToAnalyseTagFragment(Calendar monat) {
        int i = -1;
        int r = -1;
        for (int j = 0; j < AnalysisLoader.getInstance().getResults().size(); j++) {
            if (AnalysisLoader.getInstance().getResults().get(j).getDate().get(Calendar.MONTH) == monat.get(Calendar.MONTH) && AnalysisLoader.getInstance().getResults().get(j).getDate().get(Calendar.YEAR) == monat.get(Calendar.YEAR)) {
                i = j;
                for (int l = 0; l < AnalysisLoader.getInstance().getResults().get(i).getDays().size(); l++) {
                    int tag = AnalysisLoader.getInstance().getResults().get(i).getDays().get(l).getDay().get(Calendar.DAY_OF_MONTH);
                    if (tag == monat.get(Calendar.DAY_OF_MONTH)) {
                        r = l;
                        break;
                    }
                }
            }
        }
        if (i != -1) {
            AnalysisDayFragment temp = new AnalysisDayFragment(AnalysisLoader.getInstance().getResults().get(i).getDays().get(r));
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.framelayout, temp);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    public void changeToAnalyseFahrtFragment(AnalysisResultPath weg) {
        AnalysisPathFragment temp = new AnalysisPathFragment(weg);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout, temp);
        ft.addToBackStack(null);
        ft.commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x1)
            if (resultCode == RESULT_OK)
                Helpers.setServiceStartedSuccesfully(this, true);
            else
                LocationUpdatesService.stopLocationUpdates(this);

    }

    /**
     * Method changes tracking button state in case of stopping service from notification while activity is alive.
     * Same scenario if OS kills Activity and/or Service.
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        if (s.equals(Helpers.KEY_REQUESTING_LOCATION_UPDATES)) {
            NavigationView navigationView = findViewById(R.id.nav_view);

            if (Helpers.requestingLocationUpdates(this))
                ((Switch) navigationView.getMenu().findItem(R.id.nav_tracking).getActionView()).setChecked(true);
            else
                ((Switch) navigationView.getMenu().findItem(R.id.nav_tracking).getActionView()).setChecked(false);
        }

    }
}
