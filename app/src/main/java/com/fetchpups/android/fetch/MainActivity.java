package com.fetchpups.android.fetch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

//import static com.fetchpups.android.fetch.R.styleable.NavigationView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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


//        checkIfFirstRun();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, new EventFinder()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStackImmediate();
        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            return true;
            startActivity(new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displayView(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /***
     * Displays respected fragment that user selected from navigation menu
     */
    private void displayView(int viewId) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch(viewId){
            case R.id.news:
                fragment = new News();  //Modify this to correct fragment after respective fragment files created
                title = "News";
                break;
            case R.id.national_news:
                fragment = new NationalNews();
                title = "National News";
                break;
            case R.id.pet_products:
                fragment = new PetProducts();
                title = "Pet Products";
                break;
            case R.id.event_finder:
                fragment = new EventFinder();
                title = "Event finder";
                break;
            case R.id.dog_sales:
                fragment = new DogListView();
                title = "Dog Adoptions";
                break;
            case R.id.cat_sales:
                fragment = new CatListView();  //
                title = "Cat Adoptions";
                break;
            case R.id.dog_park_finder:
                fragment = new MapsFragment();
                title = "Dog Park Finder";
                break;
        }

        //Process the fragment replacement as long as there's a valid fragment
        if(fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment).addToBackStack(null);
            ft.commit();
        }

        //Setup the toolbar title correctly
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

//    protected void checkIfFirstRun(){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//        //If the flag does not exist, true is returned from getBoolean()
//        if(prefs.getBoolean("is_first_run", true)){
//            prefs.edit().putBoolean("is_first_run", false).apply();
//            showFirstRunOptions();
//        }
//
//
//    }
//
//    protected void showFirstRunOptions(){
//        Fragment fragment = new IntroFragment();
//
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.frame, fragment);
//        ft.commit();
//    }
}
