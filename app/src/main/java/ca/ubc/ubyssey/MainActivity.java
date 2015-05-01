package ca.ubc.ubyssey;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import ca.ubc.ubyssey.main.FeedFragment;

/**
 * Main Controller for the application data. Determines which fragment to show
 * <p/>
 * Created by Chris Li on 3/16/2015.
 */
public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final int HOME_ITEM = 0;
    public static final int CULTURE_ITEM = 1;
    public static final int OPINION_ITEM = 2;
    public static final int FEATURES_ITEM = 3;
    public static final int DATA_ITEM = 4;
    public static final int SPORTS_ITEM = 5;
    public static final int VIDEO_ITEM = 6;
    public static final int BLOG_ITEM = 7;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;


    private Toolbar mToolbar;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                mToolbar,
                mainLayout);
    }

    @Override
    public void onNavigationDrawerItemSelected(int value) {
        // update the main content by replacing fragments

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (value) {
            case HOME_ITEM:
                fragmentTransaction.replace(R.id.container, FeedFragment.newInstance(HOME_ITEM)).commit();
                break;

            case CULTURE_ITEM:
                fragmentTransaction.replace(R.id.container, FeedFragment.newInstance(CULTURE_ITEM)).commit();
                break;

            case OPINION_ITEM:

                break;

            case FEATURES_ITEM:

                break;

            case DATA_ITEM:

                break;

            case SPORTS_ITEM:

                break;

            case VIDEO_ITEM:

                break;

            case BLOG_ITEM:

                break;

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

}
