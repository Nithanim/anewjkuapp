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

package org.voidsink.anewjkuapp.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import org.voidsink.anewjkuapp.PreferenceWrapper;
import org.voidsink.anewjkuapp.R;
import org.voidsink.anewjkuapp.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Collections;

public class AssessmentChangedNotification {
    private static int NOTIFICATION_GRADES_CHANGED = R.string.notification_grades_changed;

    private Context mContext;

    private List<String> mInserts;
    private List<String> mUpdates;

    public AssessmentChangedNotification(Context mContext) {
        this.mContext = mContext;
        this.mInserts = new ArrayList<String>();
        this.mUpdates = new ArrayList<String>();
    }

    public void addInsert(String text) {
        mInserts.add(text);
    }

    public void addUpdate(String text) {
        mUpdates.add(text);
    }

    public void show() {
        if (PreferenceWrapper.getNotifyGrade(mContext)
                && (mInserts.size() > 0 || mUpdates.size() > 0)) {
            PendingIntent pendingIntent = PendingIntent
                    .getActivity(mContext, NOTIFICATION_GRADES_CHANGED, new Intent(mContext,
                            MainActivity.class).putExtra(MainActivity.ARG_SHOW_FRAGMENT_ID, R.id.nav_grades)
                            .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT), 0);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    mContext)
                    .setSmallIcon(R.drawable.ic_stat_notify_kusss)
//					.setLargeIcon(
//							BitmapFactory.decodeResource(
//									mContext.getResources(),
//									R.drawable.ic_launcher_grey))
                    .setContentIntent(pendingIntent)
                    .setContentTitle(
                            mContext.getText(R.string.notification_grades_changed_title))
                    .setContentText(
                            String.format(
                                    mContext.getString(R.string.notification_grades_changed),
                                    (mInserts.size() + mUpdates.size())))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setNumber(
                            mInserts.size() + mUpdates.size());

            // creates big view with all grades in inbox style
            NotificationCompat.InboxStyle inBoxStyle = new NotificationCompat.InboxStyle();

            inBoxStyle.setBigContentTitle(String.format(
                    mContext.getString(R.string.notification_grades_changed),
                    (mInserts.size() + mUpdates.size())));

            Collections.sort(mInserts);
            for (String text : mInserts) {
                inBoxStyle.addLine(text);
            }
            Collections.sort(mUpdates);
            for (String text : mUpdates) {
                inBoxStyle.addLine(text);
            }
            // Moves the big view style object into the notification object.
            mBuilder.setStyle(inBoxStyle);

            ((NotificationManager) mContext
                    .getSystemService(Context.NOTIFICATION_SERVICE)).notify(
                    NOTIFICATION_GRADES_CHANGED, mBuilder.build());
        }
    }
}
