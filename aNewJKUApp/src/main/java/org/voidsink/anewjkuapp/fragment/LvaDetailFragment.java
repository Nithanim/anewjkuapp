package org.voidsink.anewjkuapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import org.voidsink.anewjkuapp.LvaListAdapter;
import org.voidsink.anewjkuapp.R;
import org.voidsink.anewjkuapp.base.BaseFragment;
import org.voidsink.anewjkuapp.kusss.ExamGrade;
import org.voidsink.anewjkuapp.kusss.Lva;
import org.voidsink.anewjkuapp.kusss.LvaWithGrade;
import org.voidsink.anewjkuapp.utils.AppUtils;
import org.voidsink.anewjkuapp.view.StickyListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressLint("ValidFragment")
public class LvaDetailFragment extends BaseFragment {

    private static final String TAG = LvaDetailFragment.class.getSimpleName();

    private List<String> mTerms;
    private LvaListAdapter mAdapter;

    private List<LvaWithGrade> mLvas = new ArrayList<>();

    public LvaDetailFragment() {
        this("", new ArrayList<Lva>(), new ArrayList<ExamGrade>());
    }

    public LvaDetailFragment(List<String> terms, List<Lva> lvas,
                             List<ExamGrade> grades) {
        super();

        this.mTerms = terms;
        this.mLvas = AppUtils.getLvasWithGrades(terms, lvas, grades);
    }

    public LvaDetailFragment(String term, List<Lva> lvas, List<ExamGrade> grades) {
        this(Arrays.asList(new String[]{term}), lvas, grades);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_lva_detail, container,
                false);

        final StickyListView mListView = (StickyListView) view.findViewById(R.id.lva_card_list);

        mAdapter = new LvaListAdapter(getContext());
        AppUtils.sortLVAsWithGrade(mLvas);
        mAdapter.addAll(mLvas);
        mListView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.lva, menu);
    }
}
