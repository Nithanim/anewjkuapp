package org.voidsink.anewjkuapp.calendar;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.voidsink.anewjkuapp.R;
import org.voidsink.anewjkuapp.base.BaseArrayAdapter;
import org.voidsink.anewjkuapp.base.StickyArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CalendarEventAdapter extends StickyArrayAdapter<CalendarListItem> {

	private LayoutInflater inflater;

	public CalendarEventAdapter(Context context) {
		super(context, R.layout.calendar_list_item);

		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		CalendarListItem item = this.getItem(position);
		if (item.isEvent()) {
			view = getEventView(convertView, parent, item);
		} else {
			view = getSectionView(convertView, parent, item);
		}
		return view;
	}

	private View getEventView(View convertView, ViewGroup parent,
			CalendarListItem item) {
		CalendarListEvent eventItem = (CalendarListEvent) item;
		CalendarListEventHolder eventItemHolder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.calendar_list_item, parent,
					false);
			eventItemHolder = new CalendarListEventHolder();
			eventItemHolder.chip = convertView
					.findViewById(R.id.calendar_list_item_chip);
			eventItemHolder.title = (TextView) convertView
					.findViewById(R.id.calendar_list_item_title);
			eventItemHolder.descr = (TextView) convertView
					.findViewById(R.id.calendar_list_item_descr);
			eventItemHolder.time = (TextView) convertView
					.findViewById(R.id.calendar_list_item_time);
			eventItemHolder.location = (TextView) convertView
					.findViewById(R.id.calendar_list_item_location);

			convertView.setTag(eventItemHolder);
		}

		if (eventItemHolder == null) {
			eventItemHolder = (CalendarListEventHolder) convertView.getTag();
		}

		eventItemHolder.chip.setBackgroundColor(eventItem.getColor());
		eventItemHolder.title.setText(eventItem.getTitle());

		if (eventItem.getDescr().isEmpty()) {
			eventItemHolder.descr.setVisibility(View.GONE);
		} else {
			eventItemHolder.descr.setVisibility(View.VISIBLE);
			eventItemHolder.descr.setText(eventItem.getDescr());
		}

		if (eventItem.getTime().isEmpty()) {
			eventItemHolder.time.setVisibility(View.GONE);
		} else {
			eventItemHolder.time.setVisibility(View.VISIBLE);
			eventItemHolder.time.setText(eventItem.getTime());
		}

		if (eventItem.getLocation().isEmpty()) {
			eventItemHolder.location.setVisibility(View.GONE);
		} else {
			eventItemHolder.location.setVisibility(View.VISIBLE);
			eventItemHolder.location.setText(eventItem.getLocation());
		}

		return convertView;
	}

	private View getSectionView(View convertView, ViewGroup parent,
			CalendarListItem item) {
		CalendarListSection section = (CalendarListSection) item;
		CalendarListSectionHolder sectionHolder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.calendar_list_section,
					parent, false);
			sectionHolder = new CalendarListSectionHolder();
			sectionHolder.text = (TextView) convertView
					.findViewById(R.id.calendar_list_section_text);
			convertView.setTag(sectionHolder);
		}

		if (sectionHolder == null) {
			sectionHolder = (CalendarListSectionHolder) convertView.getTag();
		}

		sectionHolder.text.setText(section.getText());

		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return this.getItem(position).getType();
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
        CalendarListItem card = getItem(position);
        if (card instanceof CalendarListEvent) {
            tvHeaderTitle.setText(DateFormat.getDateInstance().format(new Date(((CalendarListEvent) card).getDtStart())));
        }
        return headerView;
    }

    @Override
    public long getHeaderId(int position) {
        CalendarListItem card = getItem(position);
        if (card instanceof CalendarListEvent) {

            Calendar cal = Calendar.getInstance(); // locale-specific
            cal.setTimeInMillis(((CalendarListEvent) card).getDtStart());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTimeInMillis();
        }
        return 0;
    }

    private static class CalendarListEventHolder {
		private TextView title;
		private TextView descr;
		private View chip;
		private TextView time;
		private TextView location;
	}

	private class CalendarListSectionHolder {
		private TextView text;
	}

}
