package org.voidsink.anewjkuapp.fragment;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import org.voidsink.anewjkuapp.ExamListAdapter;
import org.voidsink.anewjkuapp.ExamListExam;
import org.voidsink.anewjkuapp.ExamListItem;
import org.voidsink.anewjkuapp.ImportExamTask;
import org.voidsink.anewjkuapp.KusssContentContract;
import org.voidsink.anewjkuapp.LvaMap;
import org.voidsink.anewjkuapp.R;
import org.voidsink.anewjkuapp.base.BaseFragment;
import org.voidsink.anewjkuapp.utils.AppUtils;
import org.voidsink.anewjkuapp.utils.Consts;
import org.voidsink.anewjkuapp.view.StickyListView;

import java.util.ArrayList;
import java.util.List;

public class ExamFragment extends BaseFragment {

    private static final String TAG = ExamFragment.class.getSimpleName();

    private ExamListAdapter mAdapter;

    private ContentObserver mNewExamObserver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_exam, container, false);

        final StickyListView mListView = (StickyListView) view.findViewById(R.id.exam_list);
        mAdapter = new ExamListAdapter(getContext());
        mListView.setAdapter(mAdapter);

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
    protected String getScreenName() {
        return Consts.SCREEN_EXAMS;
    }

    private class ExamLoadTask extends AsyncTask<String, Void, Void> {
        private ProgressDialog progressDialog;
        private List<ExamListItem> mExams;
        private Context mContext;

        @Override
        protected Void doInBackground(String... urls) {
            Account mAccount = AppUtils.getAccount(mContext);
            if (mAccount != null) {
                LvaMap map = new LvaMap(mContext);

                ContentResolver cr = mContext.getContentResolver();
                Cursor c = cr.query(KusssContentContract.Exam.CONTENT_URI,
                        ImportExamTask.EXAM_PROJECTION, null, null,
                        KusssContentContract.Exam.EXAM_COL_DATE + " ASC");

                if (c != null) {
                    while (c.moveToNext()) {
                        mExams.add(new ExamListExam(c, map));
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
            mAdapter.clear();
            mAdapter.addAll(mExams);
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
