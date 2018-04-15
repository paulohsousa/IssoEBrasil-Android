package ieb.coffee.com.br.issoebrasil.activity;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.MenuInflater;
import android.support.design.widget.TabLayout;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import ieb.coffee.com.br.issoebrasil.R;
import ieb.coffee.com.br.issoebrasil.adpter.PagerAdapter;
import ieb.coffee.com.br.issoebrasil.config.ConfiguracaoFirebase;
import ieb.coffee.com.br.issoebrasil.fragment.ScreenEconomicoFragment;
import ieb.coffee.com.br.issoebrasil.fragment.ScreenPoliticaFragment;
import ieb.coffee.com.br.issoebrasil.fragment.ScreenSocialFragment;
import ieb.coffee.com.br.issoebrasil.helper.SlidingTabLayout;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth autenticao;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    private String identificadorContato;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        autenticao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_pagina);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.stl_tabs);

        //Add tabs icon with setIcon() or simple text with .setText()
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.nationalcongressofbrazil));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.accountgroup));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.money));

        //Add fragments
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ScreenPoliticaFragment());
        adapter.addFragment(new ScreenSocialFragment());
        adapter.addFragment(new ScreenEconomicoFragment());

        //Setting adapter
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_sair:
                deslogarUsuario();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void deslogarUsuario(){
        autenticao.signOut();
        Intent intent = new Intent(DrawerActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
