package ca.ubc.ubyssey;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ca.ubc.ubyssey.main.FeedFragment;
import ca.ubc.ubyssey.main.GalleriesFragment;
import ca.ubc.ubyssey.main.SearchFragment;
import ca.ubc.ubyssey.main.TrendingFragment;

/**
 * Main Controller for the application data. Determines which fragment to show
 * <p/>
 * Created by Chris Li on 3/16/2015.
 */
public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, FeedFragment.ErrorCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int HOME_ITEM = 0;
    public static final int CULTURE_ITEM = 1;
    public static final int OPINION_ITEM = 2;
    public static final int FEATURES_ITEM = 3;
    public static final int DATA_ITEM = 4;
    public static final int SPORTS_ITEM = 5;
    public static final int VIDEO_ITEM = 6;
    public static final int BLOG_ITEM = 7;
    public static final int TRENDING_ITEM = 8;
    public static final int GALLERY_ITEM = 9;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private SearchFragment mSearchFragment;
    private RelativeLayout mErrorLayout;

    private Toolbar mToolbar;
    private boolean mIsSearchSelected = false;
    private boolean mIsTrendingSelected = false;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                mToolbar,
                mainLayout);

        mErrorLayout = (RelativeLayout) findViewById(R.id.error_indicator);
    }

    @Override
    public void onNavigationDrawerItemSelected(int value) {
        // update the main content by replacing fragments

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (value) {
            case HOME_ITEM:
                handleToolbarTransitions();
                fragmentTransaction.replace(R.id.container, FeedFragment.newInstance(HOME_ITEM, false)).commit();
                break;

            case CULTURE_ITEM:
                handleToolbarTransitions();
                fragmentTransaction.replace(R.id.container, FeedFragment.newInstance(CULTURE_ITEM, false)).commit();
                break;

            case OPINION_ITEM:
                handleToolbarTransitions();
                fragmentTransaction.replace(R.id.container, FeedFragment.newInstance(OPINION_ITEM, false)).commit();
                break;

            case FEATURES_ITEM:
                handleToolbarTransitions();
                fragmentTransaction.replace(R.id.container, FeedFragment.newInstance(FEATURES_ITEM, false)).commit();
                break;

            case DATA_ITEM:
                handleToolbarTransitions();
                fragmentTransaction.replace(R.id.container, FeedFragment.newInstance(DATA_ITEM, false)).commit();
                break;

            case SPORTS_ITEM:
                handleToolbarTransitions();
                fragmentTransaction.replace(R.id.container, FeedFragment.newInstance(SPORTS_ITEM, false)).commit();
                break;

            case VIDEO_ITEM:
                handleToolbarTransitions();
                fragmentTransaction.replace(R.id.container, FeedFragment.newInstance(VIDEO_ITEM, false)).commit();
                break;

            case BLOG_ITEM:
                handleToolbarTransitions();
                fragmentTransaction.replace(R.id.container, FeedFragment.newInstance(BLOG_ITEM, false)).commit();
                break;

            case TRENDING_ITEM:
                fragmentTransaction.replace(R.id.container, new TrendingFragment()).commit();
                TransitionDrawable transitionDrawable = (TransitionDrawable) mToolbar.getBackground();
                ImageView toolbarImage = (ImageView) mToolbar.findViewById(R.id.toolbar_title);
                LinearLayout trendingLayout = (LinearLayout) mToolbar.findViewById(R.id.trending_title);
                toolbarImage.setVisibility(View.GONE);
                trendingLayout.setVisibility(View.VISIBLE);
                transitionDrawable.startTransition(500);
                mIsTrendingSelected = true;
                break;

            case GALLERY_ITEM:
                handleToolbarTransitions();
                fragmentTransaction.replace(R.id.container, new GalleriesFragment()).commit();
                break;
        }
    }

    @Override
    public void onTopicItemSelected(String topic, int id) {
        handleToolbarTransitions();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, FeedFragment.newInstance(id, true)).commit();

    }

    private void handleToolbarTransitions() {
        if (mIsTrendingSelected) {
            mToolbar.findViewById(R.id.toolbar_title).setVisibility(View.VISIBLE);
            ((TransitionDrawable) mToolbar.getBackground()).reverseTransition(500);
            mToolbar.findViewById(R.id.trending_title).setVisibility(View.GONE);
            mIsTrendingSelected = false;
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
        int id = item.getItemId();

        switch (id) {

            case R.id.action_search:

                final TransitionDrawable transitionDrawable = (TransitionDrawable) mToolbar.getBackground();
                final ImageView toolbarImage = (ImageView) mToolbar.findViewById(R.id.toolbar_title);
                final LinearLayout trendingLayout = (LinearLayout) mToolbar.findViewById(R.id.trending_title);
                final EditText toolbarSearch = (EditText) mToolbar.findViewById(R.id.search_edittext);
                toolbarSearch.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {

                            if (mSearchFragment != null) {
                                if (!toolbarSearch.getText().toString().trim().isEmpty()) {
                                    mSearchFragment.makeSearchRequest(toolbarSearch.getText().toString().trim());
                                } else {
                                    mSearchFragment.showEmptyText();
                                }
                            }

                            return true;
                        }
                        return false;
                    }
                });

                if (!mIsSearchSelected) {
                    toolbarImage.setVisibility(View.GONE);
                    trendingLayout.setVisibility(View.GONE);
                    transitionDrawable.startTransition(500);
                    toolbarSearch.setVisibility(View.VISIBLE);
                    mNavigationDrawerFragment.setDrawerIndicatorEnabled(mIsSearchSelected, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // performs back button transition for search fragment
                            if (mIsTrendingSelected) {
                                trendingLayout.setVisibility(View.VISIBLE);
                            } else {
                                toolbarImage.setVisibility(View.VISIBLE);
                                transitionDrawable.reverseTransition(500);
                            }

                            toolbarSearch.setVisibility(View.GONE);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(!mIsSearchSelected);
                            mNavigationDrawerFragment.setDrawerIndicatorEnabled(mIsSearchSelected, null);
                            mNavigationDrawerFragment.toggleDrawerLock(DrawerLayout.LOCK_MODE_UNLOCKED);
                            mIsSearchSelected = false;

                            getSupportFragmentManager().popBackStackImmediate();

                            // hide keyboard
                            InputMethodManager inputManager = (InputMethodManager) getSystemService(
                                    INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    });
                    getSupportActionBar().setDisplayHomeAsUpEnabled(!mIsSearchSelected);
                    mNavigationDrawerFragment.toggleDrawerLock(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    mIsSearchSelected = true;

                    mSearchFragment = new SearchFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.container, mSearchFragment).addToBackStack(null).commit();
                } else {
                    // search fragment is visible, start search queries
                    if (mSearchFragment != null) {
                        if (!toolbarSearch.getText().toString().trim().isEmpty()) {
                            mSearchFragment.makeSearchRequest(toolbarSearch.getText().toString().trim());
                        } else {
                            mSearchFragment.showEmptyText();
                        }
                    }

                }

                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        // performs back button transition for search fragment
        if (mIsSearchSelected) {
            if (mIsTrendingSelected) {
                mToolbar.findViewById(R.id.trending_title).setVisibility(View.VISIBLE);
            } else {
                mToolbar.findViewById(R.id.toolbar_title).setVisibility(View.VISIBLE);
                ((TransitionDrawable) mToolbar.getBackground()).reverseTransition(500);
            }
            mToolbar.findViewById(R.id.search_edittext).setVisibility(View.GONE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(!mIsSearchSelected);
            mNavigationDrawerFragment.setDrawerIndicatorEnabled(mIsSearchSelected, null);
            mNavigationDrawerFragment.toggleDrawerLock(DrawerLayout.LOCK_MODE_UNLOCKED);
            mIsSearchSelected = false;
        }

        super.onBackPressed();
    }

    @Override
    public void OnInternetNotAvailable(String message) {

        mErrorLayout.setVisibility(View.VISIBLE);
        TextView errorText = (TextView) mErrorLayout.findViewById(R.id.error_text);
        errorText.setText(message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void OnUnableToLoad(String message) {

        mErrorLayout.setVisibility(View.VISIBLE);
        TextView errorText = (TextView) mErrorLayout.findViewById(R.id.error_text);
        errorText.setText(message);
    }

    @Override
    public void HideErrorMessage() {
        mErrorLayout.setVisibility(View.GONE);
    }
}
