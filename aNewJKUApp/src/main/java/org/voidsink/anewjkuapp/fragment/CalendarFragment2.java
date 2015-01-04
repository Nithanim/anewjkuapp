package org.voidsink.anewjkuapp.fragment;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import net.fortuna.ical4j.data.CalendarBuilder;

import org.voidsink.anewjkuapp.ImportCalendarTask;
import org.voidsink.anewjkuapp.R;
import org.voidsink.anewjkuapp.activity.MainActivity;
import org.voidsink.anewjkuapp.base.BaseFragment;
import org.voidsink.anewjkuapp.calendar.CalendarContractWrapper;
import org.voidsink.anewjkuapp.calendar.CalendarEventAdapter;
import org.voidsink.anewjkuapp.calendar.CalendarListEvent;
import org.voidsink.anewjkuapp.calendar.CalendarListItem;
import org.voidsink.anewjkuapp.calendar.CalendarUtils;
import org.voidsink.anewjkuapp.utils.Analytics;
import org.voidsink.anewjkuapp.utils.AppUtils;
import org.voidsink.anewjkuapp.utils.Consts;
import org.voidsink.anewjkuapp.view.ListViewWithHeader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarFragment2 extends BaseFragment implements WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EventLongPressListener {

    private static final String TAG = CalendarFragment2.class.getSimpleName();
    private ContentObserver mCalendarObserver;
    private WeekView mWeekView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_calendar_2, container,
                false);

        mWeekView = (WeekView) view.findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);
        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);
        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        mWeekView.goToToday();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.calendar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh_calendar:
                final Account account = AppUtils.getAccount(getContext());

                if (account != null) {
                    Log.d(TAG, "importing calendars");
                    Analytics.eventReloadEvents(getContext());
                    new ImportCalendarTask(account, getContext(),
                            CalendarUtils.ARG_CALENDAR_EXAM, new CalendarBuilder())
                            .execute();
                    new ImportCalendarTask(account, getContext(),
                            CalendarUtils.ARG_CALENDAR_LVA, new CalendarBuilder())
                            .execute();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        mCalendarObserver = new CalendarContentObserver(new Handler());

        Account account = AppUtils.getAccount(getContext());

        String lvaCalId = CalendarUtils.getCalIDByName(getContext(), account, CalendarUtils.ARG_CALENDAR_LVA, true);
        String examCalId = CalendarUtils.getCalIDByName(getContext(), account, CalendarUtils.ARG_CALENDAR_EXAM, true);

        if (lvaCalId == null || examCalId == null) {
            // listen to all changes
            getActivity().getContentResolver().registerContentObserver(
                    CalendarContractWrapper.Events.CONTENT_URI().buildUpon()
                            .appendPath("#").build(), false, mCalendarObserver);
        } else {
            getActivity().getContentResolver().registerContentObserver(
                    CalendarContractWrapper.Events.CONTENT_URI().buildUpon()
                            .appendPath(lvaCalId).build(), false, mCalendarObserver);
            getActivity().getContentResolver().registerContentObserver(
                    CalendarContractWrapper.Events.CONTENT_URI().buildUpon()
                            .appendPath(examCalId).build(), false, mCalendarObserver);
        }

    }

    @Override
    public void onStop() {
        getActivity().getContentResolver().unregisterContentObserver(
                mCalendarObserver);

        super.onStop();
    }

    @Override
    protected String getScreenName() {
        return Consts.SCREEN_CALENDAR;
    }

    @Override
    public void onEventClick(WeekViewEvent weekViewEvent, RectF rectF) {

    }

    @Override
    public void onEventLongPress(WeekViewEvent weekViewEvent, RectF rectF) {
        // show context menu

    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        Account mAccount = AppUtils.getAccount(getContext());
        if (mAccount == null) {
            return null;
        }

        final String calIDLva = CalendarUtils.getCalIDByName(getContext(),
                mAccount, CalendarUtils.ARG_CALENDAR_LVA, true);
        final String calIDExam = CalendarUtils.getCalIDByName(getContext(),
                mAccount, CalendarUtils.ARG_CALENDAR_EXAM, true);

        if (calIDLva == null || calIDExam == null) {
            Log.w(TAG, "no events loaded, calendars not found");
            return null;
        }

        // fetch calendar colors
        final Map<String, Integer> mColors = new HashMap<>();
        final ContentResolver cr = getContext().getContentResolver();

        Cursor c = cr.query(CalendarContractWrapper.Calendars.CONTENT_URI(),
                new String[]{
                        CalendarContractWrapper.Calendars._ID(),
                        CalendarContractWrapper.Calendars.CALENDAR_COLOR()},
                null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                mColors.put(c.getString(0), c.getInt(1));
            }
            c.close();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(Calendar.YEAR, newYear);
        cal.set(Calendar.MONTH, newMonth - 1); // month index starts at 0 for jan
        cal.set(Calendar.DAY_OF_MONTH, 1);

        long now = cal.getTimeInMillis();

        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.MILLISECOND, -1);

        long then = cal.getTimeInMillis();

        c = cr.query(
                CalendarContractWrapper.Events.CONTENT_URI(),
                ImportCalendarTask.EVENT_PROJECTION,
                "("
                        + CalendarContractWrapper.Events
                        .CALENDAR_ID()
                        + " = ? or "
                        + CalendarContractWrapper.Events
                        .CALENDAR_ID() + " = ? ) and "
                        + CalendarContractWrapper.Events.DTEND()
                        + " >= ? and "
                        + CalendarContractWrapper.Events.DTSTART()
                        + " <= ? and "
                        + CalendarContractWrapper.Events.DELETED()
                        + " != 1",
                new String[]{calIDExam, calIDLva,
                        Long.toString(now), Long.toString(then)},
                CalendarContractWrapper.Events.DTSTART() + " ASC");

        if (c == null) {
            return null;
        }

        List<WeekViewEvent> events = new ArrayList<>();

        while (c.moveToNext()) {
            Calendar startTime = Calendar.getInstance();
            startTime.setTimeInMillis(c.getLong(ImportCalendarTask.COLUMN_EVENT_DTSTART));
            Calendar endTime = Calendar.getInstance();
            endTime.setTimeInMillis(c.getLong(ImportCalendarTask.COLUMN_EVENT_DTEND));

            WeekViewEvent event = new WeekViewEvent(c.getLong(ImportCalendarTask.COLUMN_EVENT_ID), c.getString(ImportCalendarTask.COLUMN_EVENT_TITLE), startTime, endTime);

            int color = getContext().getResources().getColor(R.color.accentTransparent);
            final String key = c.getString(ImportCalendarTask.COLUMN_EVENT_CAL_ID);
            if (mColors.containsKey(key)) {
                color = mColors.get(key);
            }
            event.setColor(color);

            events.add(event);
        }
        c.close();

        return events;
    }

    private class CalendarContentObserver extends ContentObserver {

        public CalendarContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            mWeekView.notifyDatasetChanged();
        }
    }
}