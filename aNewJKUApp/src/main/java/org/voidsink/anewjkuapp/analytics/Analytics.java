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

package org.voidsink.anewjkuapp.analytics;

import android.app.Application;
import android.content.Context;

public class Analytics {
    private static Object mutex = new Object();
    private static IAnalytics sAnalytics;

    private static synchronized IAnalytics getAnalytics() {
        if (sAnalytics == null) {
            synchronized (mutex) {
                if (sAnalytics == null) sAnalytics = new AnalyticsFlavor();
            }
        }
        return sAnalytics;
    }

    public static void init(Application app) {
        getAnalytics().init(app);
    }

    public static void sendException(Context c, Exception e, boolean fatal) {
        sendException(c, e, fatal, null);
    }

    public static void sendException(Context c, Exception e, boolean fatal, String additionalData) {
        getAnalytics().sendException(c, e, fatal, additionalData);
    }

    public static void sendScreen(Context c, String screenName) {
        getAnalytics().sendScreen(c, screenName);
    }

    public static void clearScreen(Context c) {
        sendScreen(c, null);
    }

    public static void eventReloadCourses(Context c) {
        getAnalytics().sendButtonEvent("reload_courses");
    }

    public static void eventReloadEventsCourse(Context c) {
        getAnalytics().sendButtonEvent("reload_events_exam");
    }

    public static void eventReloadEventsExam(Context c) {
        getAnalytics().sendButtonEvent("reload_events_course");
    }

    public static void eventLoadMoreEvents(Context c, long value) {
        getAnalytics().sendButtonEvent("more_events");
    }

    public static void eventReloadExams(Context c) {
        getAnalytics().sendButtonEvent("reload_exams");
    }

    public static void eventReloadAssessments(Context c) {
        getAnalytics().sendButtonEvent("reload_assessments");
    }

    public static void eventReloadCurricula(Context c) {
        getAnalytics().sendButtonEvent("reload_curricula");
    }

    public static void sendPreferenceChanged(String key, String value) {
        getAnalytics().sendPreferenceChanged(key, value);
    }

    public static void setEnabled(boolean enabled) {
        getAnalytics().setEnabled(enabled);
    }
}
