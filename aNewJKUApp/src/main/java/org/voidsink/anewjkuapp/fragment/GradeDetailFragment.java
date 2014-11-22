package org.voidsink.anewjkuapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import org.voidsink.anewjkuapp.GradeListAdapter;
import org.voidsink.anewjkuapp.R;
import org.voidsink.anewjkuapp.base.BaseFragment;
import org.voidsink.anewjkuapp.kusss.ExamGrade;
import org.voidsink.anewjkuapp.utils.AppUtils;
import org.voidsink.anewjkuapp.view.StickyListView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class GradeDetailFragment extends BaseFragment {

    public static final String TAG = GradeDetailFragment.class.getSimpleName();

    private GradeListAdapter mAdapter;

    private List<ExamGrade> mGrades;

    public GradeDetailFragment() {
        this (new ArrayList<String>(), new ArrayList<ExamGrade>());
    }

    public GradeDetailFragment(List<String> terms, List<ExamGrade> grades) {
        super();

        this.mGrades = AppUtils.filterGrades(terms, grades);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_grade_detail, container,
                false);

        final StickyListView mListView = (StickyListView) view.findViewById(R.id.grade_card_list);

        mAdapter = new GradeListAdapter(getContext());
        mAdapter.addAll(this.mGrades);
        mListView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.grade, menu);
    }
}
