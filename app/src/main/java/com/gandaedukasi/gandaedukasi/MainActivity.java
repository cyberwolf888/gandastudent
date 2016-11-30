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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gandaedukasi.gandaedukasi.fragments.ArticlesFragment;
import com.gandaedukasi.gandaedukasi.fragments.HomeFragment;
import com.gandaedukasi.gandaedukasi.fragments.MenuFragment;
import com.gandaedukasi.gandaedukasi.utility.RequestServer;
import com.gandaedukasi.gandaedukasi.utility.Session;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    TextView studentName;
    ImageView studentPhoto;
    NavigationView navigationView;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(MainActivity.this);
        if(session.isLoggedIn()){
            /*Intent i = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(i);
            finish();*/
        }
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        studentName = (TextView) headerView.findViewById(R.id.studentName);
        studentPhoto = (ImageView) headerView.findViewById(R.id.studentPhoto);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragment = new HomeFragment();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Ganda Edukasi");
    }

    public void onResume (){
        super.onResume();
        if(session.isLoggedIn()){
            String fullname = session.getFullname();
            studentName.setText(fullname);
            navigationView.getMenu().findItem(R.id.nav_home).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);

            String url_photo = new RequestServer().getPhotoUrl()+"/siswa/"+session.getUserPhoto();
            Ion.with(MainActivity.this)
                    .load(url_photo)
                    .withBitmap()
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .intoImageView(studentPhoto);
        }else {
            studentName.setText("Guest");
            navigationView.getMenu().findItem(R.id.nav_home).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
        }
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
            /*fragment = new MenuFragment();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Main Menu");*/
            Intent i = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_contact){
            Intent i = new Intent(MainActivity.this, ContactActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_login){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_logout){
            String url = new RequestServer().getServer_url()+"deleteNotif";
            Ion.with(MainActivity.this)
                    .load(url)
                    .setMultipartParameter("user_id", session.getUserId())
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                        }
                    });
            session.logoutUser();
            //Intent i = new Intent(MainActivity.this, LoginActivity.class);
            //startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
