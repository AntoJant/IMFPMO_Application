package com.imfpmo.app;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener, DrawerLocker {

    public static String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        ((Switch) navigationView.getMenu().findItem(R.id.nav_tracking).getActionView()).setChecked(true);
        ((Switch) navigationView.getMenu().findItem(R.id.nav_tracking).getActionView()).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton button, boolean state) {
                //If Tracking Switch has state "On"
                if (state) {
                    Log.w(TAG, "TRACKING STARTED FROM SWITCH");
                    startService(intent);
                    //If Tracking Switch has state "Off"
                } else {
                    Log.w(TAG, "TRACKING STOPPED FROM SWITCH");
                    stopService(intent);
                }
            }
        });

        if (!checkPermissions()) {
            requestPermissions();
        }

        AnalysisLoader.createInstance(this);


        //should be on LoggedInActivity or onPhoneBoot
        //startService(new Intent(this, LocationUpdatesService.class));
        //Sachen für die AnalyseDarstellung

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
            NavigationView navigationView = findViewById(R.id.nav_view);
            ((Switch) navigationView.getMenu().findItem(R.id.nav_tracking).getActionView()).setChecked(false);

            Usermanagement.getInstance().logout(getApplicationContext());
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


    //implement onRequestPermissionsResult for clean handling of denied permissions
    //app works only with all the time permissions granted.
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
        /*
        boolean permissionAccessFineLocationApproved =
                ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;

        boolean backgroundLocationPermissionApproved =
                ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;

        boolean shouldProvideRationale =
                permissionAccessFineLocationApproved && backgroundLocationPermissionApproved;

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    findViewById(R.id.drawer_layout),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
        */
    }


    @Override
    protected void onDestroy() {
        Log.w(TAG, "onDestroy");
        //stopping here only for testing. in reality only on changeslider or location permissions revoked
        //stopService(new Intent(this, LocationUpdatesService.class));
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.w(TAG, "onStop");
        //stopService(new Intent(this, LocationUpdatesService.class));
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.w(TAG, "onRestart");
        super.onRestart();
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
                i = j;break;
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
                        r = l; break;
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
}
