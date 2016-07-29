package com.gandaedukasi.gandaedukasi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.gandaedukasi.gandaedukasi.fragments.ArticlesFragment;
import com.gandaedukasi.gandaedukasi.fragments.HomeFragment;
import com.gandaedukasi.gandaedukasi.fragments.MenuFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragment = new HomeFragment();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Ganda Edukasi");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (id == R.id.nav_artikel) {
            // Handle the camera action
            fragment = new ArticlesFragment();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Articles");
        } else if (id == R.id.nav_home) {
            fragment = new MenuFragment();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Main Menu");
        }else if (id == R.id.nav_login){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
