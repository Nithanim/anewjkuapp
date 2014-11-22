package org.voidsink.anewjkuapp.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import org.voidsink.anewjkuapp.utils.Analytics;
import org.voidsink.anewjkuapp.utils.UIUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ThemedActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIUtils.applyTheme(this);

        super.onCreate(savedInstanceState);

        initActionBar();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    protected void initActionBar() {
    }

    @Override
    protected void onStart() {
        super.onStart();

        final String screenName = getScreenName();
        if (screenName != null && !screenName.isEmpty()) {
            Analytics.sendScreen(this, screenName);
        }
    }

    /*
     * returns screen name for logging activity
     */
    protected String getScreenName() {
        return null;
    }
}
