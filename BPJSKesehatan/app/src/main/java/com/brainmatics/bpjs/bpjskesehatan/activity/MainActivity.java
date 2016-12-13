package com.brainmatics.bpjs.bpjskesehatan.activity;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import com.brainmatics.bpjs.bpjskesehatan.R;
import com.brainmatics.bpjs.bpjskesehatan.fragment.NavigationDrawerFragment;
import com.brainmatics.bpjs.bpjskesehatan.fragment.PesertaFragment;
import com.brainmatics.bpjs.bpjskesehatan.fragment.SettingsFragment;
import com.brainmatics.bpjs.bpjskesehatan.fragment.TagihanFragment;
import com.brainmatics.bpjs.bpjskesehatan.fragment.WelcomeFragment;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = "MainActivity";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new WelcomeFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(!getString(R.string.action_settings).equals(item.getTitle())) {
            Log.d(TAG, "Menu Item : "+item.getTitle());
            return false;
        }

        // tambahkan action handler untuk button settings
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new SettingsFragment())
                .commit();
        return true;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fr = null;
        switch (position) {
            case 0:
                fr = new TagihanFragment();
                break;
            case 1:
                fr = new PesertaFragment();
                break;
            default:
                fr = new WelcomeFragment();
                break;
        }

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fr)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.lblMenuTagihan);
                break;
            case 2:
                mTitle = getString(R.string.lblMenuPeserta);
                break;
            case 3:
                mTitle = getString(R.string.lblMenuLogout);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

}
