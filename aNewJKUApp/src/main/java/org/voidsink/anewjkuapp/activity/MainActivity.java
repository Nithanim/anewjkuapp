/*******************************************************************************
 *      ____.____  __.____ ___     _____
 *     |    |    |/ _|    |   \   /  _  \ ______ ______
 *     |    |      < |    |   /  /  /_\  \\____ \\____ \
 * /\__|    |    |  \|    |  /  /    |    \  |_> >  |_> >
 * \________|____|__ \______/   \____|__  /   __/|   __/
 *                  \/                  \/|__|   |__|
 *
 * Copyright (c) 2014-2015 Paul "Marunjar" Pretsch
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 ******************************************************************************/

package org.voidsink.anewjkuapp.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.voidsink.anewjkuapp.DrawerItem;
import org.voidsink.anewjkuapp.KusssAuthenticator;
import org.voidsink.anewjkuapp.PreferenceWrapper;
import org.voidsink.anewjkuapp.R;
import org.voidsink.anewjkuapp.analytics.Analytics;
import org.voidsink.anewjkuapp.base.BaseFragment;
import org.voidsink.anewjkuapp.base.ThemedActivity;
import org.voidsink.anewjkuapp.calendar.CalendarContractWrapper;
import org.voidsink.anewjkuapp.fragment.CalendarFragment;
import org.voidsink.anewjkuapp.fragment.CurriculaFragment;
import org.voidsink.anewjkuapp.fragment.NavigationDrawerFragment;
import org.voidsink.anewjkuapp.utils.AppUtils;

import de.cketti.library.changelog.ChangeLog;

public class MainActivity extends ThemedActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String ARG_SHOW_FRAGMENT = "show_fragment";
    public static final String ARG_EXACT_LOCATION = "exact_location";
    public static final String ARG_SAVE_LAST_FRAGMENT = "save_last_fragment";

    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Fragment managing the behaviors, interactions and presentation of the
     * navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in
     * {@link #initActionBar()}.
     */
    private CharSequence mTitle;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void StartCreateAccount(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            context.startActivity(new Intent(Settings.ACTION_ADD_ACCOUNT)
                    .putExtra(Settings.EXTRA_ACCOUNT_TYPES,
                            new String[]{KusssAuthenticator.ACCOUNT_TYPE}));
        } else {
            context.startActivity(new Intent(Settings.ACTION_ADD_ACCOUNT)
                    .putExtra(
                            Settings.EXTRA_AUTHORITIES,
                            new String[]{CalendarContractWrapper.AUTHORITY()}));
        }
    }

    public static void StartMyCurricula(Context context) {
        //
        Intent i = new Intent(context, MainActivity.class)
                .putExtra(MainActivity.ARG_SHOW_FRAGMENT, CurriculaFragment.class.getName())
                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(i);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                ARG_SHOW_FRAGMENT);
        if (fragment != null) {
            outState.putString(ARG_SHOW_FRAGMENT, fragment.getClass().getName());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // do things if new version was installed
        AppUtils.doOnNewVersion(this);

        // initialize graphic factory for mapsforge
        AndroidGraphicFactory.createInstance(this.getApplication());

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                mTitle = getTitleFromFragment(null);
                initActionBar();
            }
        });

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navigation_drawer);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        //mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color));


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, mDrawerLayout);

        Intent intent = getIntent();

        Fragment f = attachFragment(intent, savedInstanceState, true);
        // attach calendar fragment as default
        if (f == null) {
            f = attachFragmentByClassName(CalendarFragment.class.getName());
        }
        handleIntent(f, intent);

        mTitle = getTitleFromFragment(f);

        if (AppUtils.getAccount(this) == null) {
            StartCreateAccount(this);
        } else {
            ChangeLog cl = new ChangeLog(this);
            if (cl.isFirstRun()) {
                cl.getLogDialog().show();
            }
        }
    }

    private Fragment attachFragment(Intent intent, Bundle savedInstanceState,
                                    boolean attachStored) {
        if (intent != null && intent.hasExtra(ARG_SHOW_FRAGMENT)) {
            // show fragment from intent
            return attachFragmentByClassName(
                    intent.getStringExtra(ARG_SHOW_FRAGMENT),
                    intent.getBooleanExtra(ARG_SAVE_LAST_FRAGMENT, true));
        } else if (savedInstanceState != null) {
            // restore saved fragment
            return attachFragmentByClassName(savedInstanceState
                    .getString(ARG_SHOW_FRAGMENT));
        } else if (attachStored) {
            return attachFragmentByClassName(PreferenceWrapper
                    .getLastFragment(this));
        } else {
            return getSupportFragmentManager().findFragmentByTag(
                    ARG_SHOW_FRAGMENT);
        }
    }

    private void handleIntent(Fragment f, Intent intent) {
        if (f == null) {
            f = getSupportFragmentManager()
                    .findFragmentByTag(ARG_SHOW_FRAGMENT);
        }

        if (f != null) {
            // Log.i(TAG, "fragment: " + f.getClass().getSimpleName());
            if (BaseFragment.class.isInstance(f)) {
                ((BaseFragment) f).handleIntent(intent);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Fragment f = attachFragment(intent, null, false);
        handleIntent(f, intent);
    }

    private Fragment attachFragmentByClassName(final String clazzname) {
        return attachFragmentByClassName(clazzname, true);
    }

    private Fragment attachFragmentByClassName(final String clazzname, final boolean saveLastFragment) {
        if (clazzname != null && !clazzname.isEmpty()) {
            try {
                Class<?> clazz = getClassLoader().loadClass(clazzname);
                if (Fragment.class.isAssignableFrom(clazz)) {
                    return attachFragment((Class<? extends Fragment>) clazz, saveLastFragment);
                }
            } catch (ClassNotFoundException e) {
                Log.w(TAG, "fragment instantiation failed", e);
                Analytics.sendException(this, e, false);
                PreferenceWrapper.setLastFragment(this, PreferenceWrapper.PREF_LAST_FRAGMENT_DEFAULT);
            }
        }
        return null;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position, DrawerItem item) {
        if (item.isEnabled()) {
            // update the main content by replacing fragments
            if ((item.getStartFragment() != null)) {
                attachFragment(item.getStartFragment());
            }
        }
    }

    public String getTitleFromFragment(Fragment f) {
        if (f == null) {
            f = getSupportFragmentManager()
                    .findFragmentByTag(ARG_SHOW_FRAGMENT);
        }
        if (f != null) {
            return NavigationDrawerFragment.getLabel(this, f.getClass());
        }
        return getString(R.string.app_name);
    }

    private Fragment attachFragment(Class<? extends Fragment> startFragment) {
        return attachFragment(startFragment, true);
    }

    private Fragment attachFragment(Class<? extends Fragment> startFragment, boolean saveLastFragment) {
        if (startFragment != null) {
            try {
                Fragment f = startFragment.newInstance();

                final Fragment oldFragment = getSupportFragmentManager().findFragmentByTag(ARG_SHOW_FRAGMENT);
                final boolean addToBackstack = (oldFragment != null) && (!oldFragment.getClass().equals(f.getClass()));

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, f, ARG_SHOW_FRAGMENT);
                if (addToBackstack) {
                    ft.addToBackStack(f.getClass().getCanonicalName());
                }
                ft.commit();

                mTitle = getTitleFromFragment(f);
                if (saveLastFragment) {
                    PreferenceWrapper.setLastFragment(this,
                            f.getClass().getCanonicalName());
                }

                initActionBar();

                return f;
            } catch (Exception e) {
                Log.w(TAG, "fragment instantiation failed", e);
                Analytics.sendException(this, e, false);
                if (saveLastFragment) {
                    PreferenceWrapper.setLastFragment(this, PreferenceWrapper.PREF_LAST_FRAGMENT_DEFAULT);
                }
                return null;
            }
        }
        return null;
    }


    @Override
    protected void onInitActionBar(ActionBar actionBar) {
        super.onInitActionBar(actionBar);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        if (mNavigationDrawerFragment != null &&
                !mNavigationDrawerFragment.isDrawerOpen()) {
            actionBar.setTitle(mTitle);
        } else {
            actionBar.setTitle(getString(R.string.app_name));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        initActionBar();

        menu.clear();
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Analytics.clearScreen(this);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container,
                    false);
            TextView textView = (TextView) rootView
                    .findViewById(R.id.section_label);
            textView.setText(Integer.toString(getArguments().getInt(
                    ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
}
