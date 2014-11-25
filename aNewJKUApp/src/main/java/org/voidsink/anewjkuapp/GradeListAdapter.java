package org.voidsink.anewjkuapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.voidsink.anewjkuapp.base.ListWithHeaderAdapter;
import org.voidsink.anewjkuapp.kusss.ExamGrade;
import org.voidsink.anewjkuapp.kusss.GradeType;
import org.voidsink.anewjkuapp.utils.AppUtils;
import org.voidsink.anewjkuapp.utils.UIUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class GradeListAdapter extends ListWithHeaderAdapter<ExamGrade> {

    private static final DateFormat df = SimpleDateFormat.getDateInstance();
    private LayoutInflater inflater;

    public GradeListAdapter(Context context) {
        super(context, R.layout.grade_list_grade);

        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ExamGrade item = this.getItem(position);
        if (item !=null) {
            view = getGradeView(convertView, parent, item);
        } else {
            view = null;
        }
        return view;
    }

	private static class GradeListGradeHolder {
		public TextView grade;
		public TextView date;
		public TextView term;
		private TextView title;
		private TextView lvaNr;
		private TextView skz;
        public View chipBack;
        public TextView chipInfo;
        public TextView chipGrade;
	}

	private View getGradeView(View convertView, ViewGroup parent,
                             ExamGrade item) {
		ExamGrade mGrade = (ExamGrade) item;
		GradeListGradeHolder gradeItemHolder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.grade_list_grade, parent,
					false);
			gradeItemHolder = new GradeListGradeHolder();
			gradeItemHolder.title = (TextView) convertView
					.findViewById(R.id.grade_list_grade_title);
			gradeItemHolder.lvaNr = (TextView) convertView
					.findViewById(R.id.grade_list_grade_lvanr);
			gradeItemHolder.term = (TextView) convertView
					.findViewById(R.id.grade_list_grade_term);
			gradeItemHolder.skz = (TextView) convertView
                    .findViewById(R.id.grade_list_grade_skz);
			gradeItemHolder.date = (TextView) convertView
					.findViewById(R.id.grade_list_grade_date);
			gradeItemHolder.grade = (TextView) convertView
					.findViewById(R.id.grade_list_grade_grade);
			gradeItemHolder.chipBack = (View) convertView
					.findViewById(R.id.grade_chip);
            gradeItemHolder.chipInfo = (TextView) convertView.findViewById(R.id.grade_chip_info);
            gradeItemHolder.chipGrade = (TextView) convertView.findViewById(R.id.grade_chip_grade);

			convertView.setTag(gradeItemHolder);
		}

		if (gradeItemHolder == null) {
			gradeItemHolder = (GradeListGradeHolder) convertView.getTag();
		}

        gradeItemHolder.title.setText(mGrade.getTitle());

        UIUtils.setTextAndVisibility(gradeItemHolder.lvaNr, mGrade.getLvaNr());
        UIUtils.setTextAndVisibility(gradeItemHolder.term, mGrade.getTerm());

        if (mGrade.getSkz() > 0) {
            gradeItemHolder.skz.setText(String.format("[%d]", mGrade.getSkz()));
            gradeItemHolder.skz.setVisibility(View.VISIBLE);
        } else {
            gradeItemHolder.skz.setVisibility(View.GONE);
        }

        gradeItemHolder.chipBack.setBackgroundColor(mGrade.getGrade().getColor());
        gradeItemHolder.chipGrade.setText(String.format("%d", mGrade.getGrade().getValue()));
        gradeItemHolder.chipInfo.setText(String.format("%.2f ECTS", mGrade.getEcts()));

        final DateFormat df = DateFormat.getDateInstance();

        gradeItemHolder.date.setText(df.format(mGrade.getDate()));
        gradeItemHolder.grade.setText(getContext().getString(mGrade.getGrade()
                .getStringResID()));

		return convertView;
	}

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup viewGroup) {
        // Build your custom HeaderView
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        final View headerView = mInflater.inflate(R.layout.list_header, null);
        final TextView tvHeaderTitle = (TextView) headerView.findViewById(R.id.list_header_text);

        ExamGrade card = getItem(position);
        if (card instanceof ExamGrade) {
            tvHeaderTitle.setText(getContext().getString(((ExamGrade) card).getGradeType().getStringResID()));

        }
        return headerView;
    }

    @Override
    public long getHeaderId(int position) {
        ExamGrade card = getItem(position);
        if (card instanceof ExamGrade) {
            return ((ExamGrade) card).getGradeType().getStringResID();
        }
        return 0;
    }



}
