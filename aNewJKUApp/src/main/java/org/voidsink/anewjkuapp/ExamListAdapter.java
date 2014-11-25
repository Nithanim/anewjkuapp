package org.voidsink.anewjkuapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.voidsink.anewjkuapp.base.ListWithHeaderAdapter;
import org.voidsink.anewjkuapp.calendar.CalendarUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ExamListAdapter extends ListWithHeaderAdapter<ExamListItem> {

	private static final DateFormat df = SimpleDateFormat.getDateInstance();

	private LayoutInflater inflater;

	public ExamListAdapter(Context context) {
		super(context, R.layout.exam_list_item);

		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ExamListItem item = this.getItem(position);
		if (item.isExam()) {
			view = getExamView(convertView, parent, item);
		} else {
			view = null;
		}
		return view;
	}

	private View getExamView(View convertView, ViewGroup parent,
			ExamListItem item) {
		ExamListExam eventItem = (ExamListExam) item;
		ExamListExamHolder eventItemHolder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.exam_list_item, parent,
					false);

			eventItemHolder = new ExamListExamHolder();

			eventItemHolder.title = (TextView) convertView
					.findViewById(R.id.exam_list_item_title);
			eventItemHolder.description = (TextView) convertView
					.findViewById(R.id.exam_list_item_description);
			eventItemHolder.info = (TextView) convertView
					.findViewById(R.id.exam_list_item_info);
			eventItemHolder.lvaNr = (TextView) convertView
					.findViewById(R.id.exam_list_item_lvanr);
			eventItemHolder.term = (TextView) convertView
					.findViewById(R.id.exam_list_item_term);
			eventItemHolder.skz = (TextView) convertView
					.findViewById(R.id.exam_list_item_skz);
			eventItemHolder.time = (TextView) convertView
					.findViewById(R.id.exam_list_item_time);
			eventItemHolder.location = (TextView) convertView
					.findViewById(R.id.exam_list_item_location);
			eventItemHolder.chip = (View) convertView
					.findViewById(R.id.empty_chip_background);

			convertView.setTag(eventItemHolder);
		}

		if (eventItemHolder == null) {
			eventItemHolder = (ExamListExamHolder) convertView.getTag();
		}

		if (eventItem.mark()) {
			eventItemHolder.chip
					.setBackgroundColor(CalendarUtils.COLOR_DEFAULT_EXAM);
		} else {
			eventItemHolder.chip
					.setBackgroundColor(CalendarUtils.COLOR_DEFAULT_LVA);
		}

		eventItemHolder.title.setText(eventItem.getTitle());
		if (!eventItem.getDescription().isEmpty()) {
			eventItemHolder.description.setText(eventItem.getDescription());
			eventItemHolder.description.setVisibility(View.VISIBLE);
		} else {
			eventItemHolder.description.setVisibility(View.GONE);
		}
		if (!eventItem.getInfo().isEmpty()) {
			eventItemHolder.info.setText(eventItem.getInfo());
			eventItemHolder.info.setVisibility(View.VISIBLE);
		} else {
			eventItemHolder.info.setVisibility(View.GONE);
		}
		eventItemHolder.lvaNr.setText(eventItem.getLvaNr());
		eventItemHolder.term.setText(eventItem.getTerm());
		eventItemHolder.skz.setText(String.format("[%s]", eventItem.getSkz()));
		eventItemHolder.time.setText(eventItem.getTime());
		eventItemHolder.location.setText(eventItem.getLocation());

		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public int getItemViewType(int position) {
		return this.getItem(position).getType();
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	private static class ExamListExamHolder {
		public TextView term;
		public View chip;
		public TextView location;
		public TextView info;
		public TextView description;
		public TextView time;
		private TextView title;
		private TextView lvaNr;
		private TextView skz;
	}

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup viewGroup) {
        // Build your custom HeaderView
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        final View headerView = mInflater.inflate(R.layout.list_header, null);
        final TextView tvHeaderTitle = (TextView) headerView.findViewById(R.id.list_header_text);

        ExamListItem card = getItem(position);
        if (card instanceof ExamListExam) {
            tvHeaderTitle.setText(DateFormat.getDateInstance().format(((ExamListExam) card).getDate()));
        }

        return headerView;
    }

    @Override
    public long getHeaderId(int position) {
        ExamListItem card = getItem(position);
        if (card instanceof ExamListExam) {

            Calendar cal = Calendar.getInstance(); // locale-specific
            cal.setTimeInMillis(((ExamListExam) card).getDate().getTime());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTimeInMillis();
        }
        return 0;
    }
}
