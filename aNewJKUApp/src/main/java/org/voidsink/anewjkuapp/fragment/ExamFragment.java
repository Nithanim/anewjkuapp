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

package org.voidsink.anewjkuapp.fragment;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.voidsink.anewjkuapp.CourseMap;
import org.voidsink.anewjkuapp.ExamListAdapter;
import org.voidsink.anewjkuapp.ExamListExam;
import org.voidsink.anewjkuapp.KusssContentContract;
import org.voidsink.anewjkuapp.R;
import org.voidsink.anewjkuapp.analytics.Analytics;
import org.voidsink.anewjkuapp.base.BaseFragment;
import org.voidsink.anewjkuapp.update.ImportExamTask;
import org.voidsink.anewjkuapp.update.UpdateService;
import org.voidsink.anewjkuapp.utils.AppUtils;
import org.voidsink.anewjkuapp.utils.Consts;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ExamFragment extends BaseFragment {

    private static final String TAG = ExamFragment.class.getSimpleName();

    private ExamListAdapter mAdapter;
    private ContentObserver mNewExamObserver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        final RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mAdapter = new ExamListAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(mAdapter));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new ExamLoadTask().execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.exam, menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNewExamObserver = new NewExamContentObserver(new Handler());
        getActivity().getContentResolver().registerContentObserver(
                KusssContentContract.Exam.CONTENT_CHANGED_URI, false, mNewExamObserver);
    }

    @Override
    public void onDestroy() {
        getActivity().getContentResolver().unregisterContentObserver(
                mNewExamObserver);

        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh_exams: {
                Intent mUpdateService = new Intent(getActivity(), UpdateService.class);
                mUpdateService.putExtra(Consts.ARG_UPDATE_KUSSS_EXAMS, true);
                getActivity().startService(mUpdateService);
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected String getScreenName() {
        return Consts.SCREEN_EXAMS;
    }

    private class ExamLoadTask extends AsyncTask<String, Void, Void> {
        private ProgressDialog progressDialog;
        private List<ExamListExam> mExams;
        private Context mContext;

        @Override
        protected Void doInBackground(String... urls) {
            Account mAccount = AppUtils.getAccount(mContext);
            if (mAccount != null) {
                CourseMap map = new CourseMap(mContext);

                ContentResolver cr = mContext.getContentResolver();
                Cursor c = cr.query(KusssContentContract.Exam.CONTENT_URI,
                        ImportExamTask.EXAM_PROJECTION, null, null,
                        KusssContentContract.Exam.COL_DTSTART + " ASC");

                if (c != null) {
                    while (c.moveToNext()) {
                        try {
                            mExams.add(new ExamListExam(c, map));
                        } catch (ParseException e) {
                            Analytics.sendException(mContext, e, false);
                        }
                    }
                    c.close();
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mContext = ExamFragment.this.getContext();
            if (mContext == null) {
                Log.e(TAG, "context is null");
            }
            mExams = new ArrayList<>();
            progressDialog = ProgressDialog.show(mContext,
                    mContext.getString(R.string.progress_title),
                    mContext.getString(R.string.progress_load_exam), true); //!!
        }

        @Override
        protected void onPostExecute(Void result) {
            if (mAdapter != null) {
                mAdapter.clear();
                mAdapter.addAll(mExams);
                mAdapter.notifyDataSetChanged();
            }

            progressDialog.dismiss();

            super.onPostExecute(result);
        }
    }

    private class NewExamContentObserver extends ContentObserver {

        public NewExamContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new ExamLoadTask().execute();
        }
    }
}
