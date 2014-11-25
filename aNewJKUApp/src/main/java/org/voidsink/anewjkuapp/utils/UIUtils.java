package org.voidsink.anewjkuapp.utils;

import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;

import org.voidsink.anewjkuapp.PreferenceWrapper;
import org.voidsink.anewjkuapp.R;
import org.voidsink.anewjkuapp.kusss.ExamGrade;

public class UIUtils {

    private UIUtils() {
    }

    public static boolean handleUpNavigation(Activity activity, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (activity instanceof ActionBarActivity) {
                    ActionBar actionBar = ((ActionBarActivity)activity).getSupportActionBar();
                    if (actionBar != null && (actionBar.getDisplayOptions() & ActionBar.DISPLAY_HOME_AS_UP) != 0) {
                        // app icon in action bar clicked; goto parent activity.
                        NavUtils.navigateUpFromSameTask(activity);
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }

    public static void setTextAndVisibility(TextView v, String text) {
        if (text != null && !text.isEmpty()) {
            v.setText(text);
            v.setVisibility(TextView.VISIBLE);
        } else {
            v.setVisibility(TextView.GONE);
        }
    }

    public static void applyTheme(Activity activity) {
        if (PreferenceWrapper.getUseLightDesign(activity)) {
            activity.setTheme(R.style.AppTheme_Light);
        } else {
            activity.setTheme(R.style.AppTheme);
        }
    }

    public static String getChipGradeText(ExamGrade grade) {
        if (grade != null) {
            if (grade.getGrade().isNumber()) {
                return String.format("%d", grade.getGrade().getValue());
            }
            if (grade.getGrade().isPositive()) {
                return "\u2713";
            } else {
                return "\u2717";
            }
        }
        return "?";
    }

    public static int getChipGradeColor(ExamGrade grade) {
        if (grade != null) {
            return grade.getGrade().getColor();
        }
        return Color.GRAY;
    }

    public static String getChipGradeEcts(double ects) {
        return String.format("%.2f ECTS", ects);
    }
}
