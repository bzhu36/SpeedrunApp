package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import edu.ucsb.cs.cs184.speedrun.speedrunapp2.SplitFrag;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.TimerFrag;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TimerFrag.OnFragmentInteractionListener, SplitFrag.OnFragmentInteractionListener {

    Fragment fragment = null;
    static boolean local = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragment = new HomepageFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
        else {
            getFragmentManager().popBackStack();
        }
        /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        */
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.timer) {
            //fragment = new TimerFrag();
            fragment = new Timer2Fragment();
        } else if (id == R.id.leaderboard) {
            local = false;
            fragment = new LeaderboardFragment();
        } else if (id == R.id.friends) {
            local = true;
            fragment = new LeaderboardFragment();
        } else if (id == R.id.profile) {
            fragment = ProfileFragment.newInstance(FirebaseAuth.getInstance().getUid(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            //fragment = new ProfileFragment();
        }
        else if (id == R.id.findFriends){
            fragment = new FriendsSearchFragment();
        }
        else if( id == R.id.friendsList){
            fragment = new FriendsListFragment();
        }
        else if (id == R.id.logout) {
            fragment = null;
            FirebaseAuth.getInstance().signOut();
            LoginActivity.mGoogleSignInClient.signOut();
            Intent intent=new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        if(fragment!=null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment).commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
