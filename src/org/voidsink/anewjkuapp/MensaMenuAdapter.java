package org.voidsink.anewjkuapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.voidsink.anewjkuapp.base.BaseArrayAdapter;
import org.voidsink.anewjkuapp.calendar.CalendarUtils;
import org.voidsink.anewjkuapp.mensa.Mensa;
import org.voidsink.anewjkuapp.mensa.MensaDay;
import org.voidsink.anewjkuapp.mensa.MensaMenu;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MensaMenuAdapter extends BaseArrayAdapter<MensaItem> {

	private static final DateFormat df = SimpleDateFormat.getDateInstance();

	private LayoutInflater inflater;

	public MensaMenuAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);

		this.inflater = LayoutInflater.from(context);
	}

	public MensaMenuAdapter(Context context) {
		this(context, 0);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;

		MensaItem item = this.getItem(position);
		switch (item.getType()) {
		case MensaItem.TYPE_DAY:
			view = getDayView(convertView, parent, item);
			break;
		case MensaItem.TYPE_MENSA:
			view = getMensaView(convertView, parent, item);
			break;
		case MensaItem.TYPE_MENU:
			view = getMenuView(convertView, parent, item);
			break;
		case MensaItem.TYPE_INFO:
			view = getInfoView(convertView, parent, item);
		default:
			break;
		}
		return view;
	}

	private View getInfoView(View convertView, ViewGroup parent, MensaItem item) {
		MensaInfoItem mensaInfoItem = (MensaInfoItem) item;
		MensaInfoHolder mensaInfoItemHolder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.mensa_info_item, parent,
					false);
			mensaInfoItemHolder = new MensaInfoHolder();
			mensaInfoItemHolder.title = (TextView) convertView
					.findViewById(R.id.mensa_info_item_title);
			mensaInfoItemHolder.descr = (TextView) convertView
					.findViewById(R.id.mensa_info_item_descr);
			mensaInfoItemHolder.chip = (View) convertView
					.findViewById(R.id.mensa_info_chip);

			convertView.setTag(mensaInfoItemHolder);
		}

		if (mensaInfoItemHolder == null) {
			mensaInfoItemHolder = (MensaInfoHolder) convertView.getTag();
		}

		mensaInfoItemHolder.title.setText(mensaInfoItem.getTitle());
		String descr = mensaInfoItem.getDescr();
		if (!descr.isEmpty()) {
			mensaInfoItemHolder.descr.setVisibility(View.VISIBLE);
			mensaInfoItemHolder.descr.setText(descr);
		} else {
			mensaInfoItemHolder.descr.setVisibility(View.GONE);
		}
		mensaInfoItemHolder.chip.setBackgroundColor(CalendarUtils.COLOR_DEFAULT_LVA);

		return convertView;
	}

	private View getMenuView(View convertView, ViewGroup parent, MensaItem item) {
		MensaMenu mensaMenuItem = (MensaMenu) item;
		MensaMenuHolder mensaMenuItemHolder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.mensa_menu_item, parent,
					false);
			mensaMenuItemHolder = new MensaMenuHolder();
			mensaMenuItemHolder.name = (TextView) convertView
					.findViewById(R.id.mensa_menu_item_name);
			mensaMenuItemHolder.soup = (TextView) convertView
					.findViewById(R.id.mensa_menu_item_soup);
			mensaMenuItemHolder.meal = (TextView) convertView
					.findViewById(R.id.mensa_menu_item_meal);
			mensaMenuItemHolder.price = (TextView) convertView
					.findViewById(R.id.mensa_menu_item_price);
			mensaMenuItemHolder.oehBonus = (TextView) convertView
					.findViewById(R.id.mensa_menu_item_oeh_bonus);
			mensaMenuItemHolder.chip = convertView
					.findViewById(R.id.mensa_menu_chip);

			convertView.setTag(mensaMenuItemHolder);
		}

		if (mensaMenuItemHolder == null) {
			mensaMenuItemHolder = (MensaMenuHolder) convertView.getTag();
		}

		String name = mensaMenuItem.getName();
		if (name != null && !name.isEmpty()) {
			mensaMenuItemHolder.name.setText(name);
			mensaMenuItemHolder.name.setVisibility(View.VISIBLE);
		} else {
			mensaMenuItemHolder.name.setVisibility(View.GONE);
		}
		String soup = mensaMenuItem.getSoup();
		if (soup != null && !soup.isEmpty()) {
			mensaMenuItemHolder.soup.setText(soup);
			mensaMenuItemHolder.soup.setVisibility(View.VISIBLE);
		} else {
			mensaMenuItemHolder.soup.setVisibility(View.GONE);
		}
		mensaMenuItemHolder.meal.setText(mensaMenuItem.getMeal());
		if (mensaMenuItem.getPrice() > 0) {
			mensaMenuItemHolder.price.setText(String.format("%.2f �",
					mensaMenuItem.getPrice()));
			mensaMenuItemHolder.price.setVisibility(View.VISIBLE);

			if (mensaMenuItem.getOehBonus() > 0) {
				mensaMenuItemHolder.oehBonus.setText(String.format(
						"inkl %.2f � �H Bonus", mensaMenuItem.getOehBonus()));
				mensaMenuItemHolder.oehBonus.setVisibility(View.VISIBLE);
			} else {
				mensaMenuItemHolder.oehBonus.setVisibility(View.GONE);
			}
		} else {
			mensaMenuItemHolder.price.setVisibility(View.GONE);
			mensaMenuItemHolder.oehBonus.setVisibility(View.GONE);
		}
		mensaMenuItemHolder.chip
				.setBackgroundColor(CalendarUtils.COLOR_DEFAULT_LVA);

		return convertView;
	}

	private View getMensaView(View convertView, ViewGroup parent, MensaItem item) {
		Mensa mensaItem = (Mensa) item;
		MensaHolder mensaItemHolder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.mensa_item, parent, false);
			mensaItemHolder = new MensaHolder();
			mensaItemHolder.title = (TextView) convertView
					.findViewById(R.id.mensa_item_title);

			convertView.setTag(mensaItemHolder);
		}

		if (mensaItemHolder == null) {
			mensaItemHolder = (MensaHolder) convertView.getTag();
		}

		mensaItemHolder.title.setText(mensaItem.getName());

		return convertView;
	}

	private View getDayView(View convertView, ViewGroup parent, MensaItem item) {
		MensaDay dayItem = (MensaDay) item;
		MensaDayHolder dayItemHolder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.mensa_day_item, parent,
					false);
			dayItemHolder = new MensaDayHolder();
			dayItemHolder.date = (TextView) convertView
					.findViewById(R.id.mensa_day_item_date);

			convertView.setTag(dayItemHolder);
		}

		if (dayItemHolder == null) {
			dayItemHolder = (MensaDayHolder) convertView.getTag();
		}

		if (dayItem.isModified()) {
			dayItemHolder.date.setText(df.format(dayItem.getDate())
					+ "\n\nEs k�nnte aber auch was anderes geben.");
		} else {
			dayItemHolder.date.setText(df.format(dayItem.getDate()));
		}

		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		return 4;
	}

	@Override
	public int getItemViewType(int position) {
		return this.getItem(position).getType();
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	private class MensaHolder {
		public TextView title;
	}

	private class MensaMenuHolder {
		public TextView name;
		public TextView soup;
		public TextView meal;
		public TextView price;
		public TextView oehBonus;
		public View chip;
	}

	private class MensaDayHolder {
		private TextView date;
	}

	private class MensaInfoHolder {
		public TextView title;
		public TextView descr;
		public View chip;
	}

	public void addMensa(Mensa mensa) {
		if (mensa != null && !mensa.isEmpty()) {
			long now = System.currentTimeMillis();

			for (MensaDay day : mensa.getDays()) {
				if (!day.isEmpty()) {
					if ((DateUtils.isToday(day.getDate().getTime()))
							|| (day.getDate().getTime() >= now)) {
						add(day);
						for (MensaMenu menu : day.getMenus()) {
							add(menu);
						}
					}
				}
			}
		}

		if (getCount() == 0) {
			add(new MensaDay(new Date()));
			add(new MensaInfoItem("kein Speiseplan verf�gbar", ""));
		}
	}

	public void addInfo(String title, String descr) {
		add(new MensaInfoItem(title, descr));
	}

	private class MensaInfoItem implements MensaItem {

		private String title;
		private String descr;

		public MensaInfoItem(String title, String descr) {
			this.title = title;
			this.descr = descr;
		}

		@Override
		public int getType() {
			return MensaItem.TYPE_INFO;
		}

		public String getTitle() {
			return title;
		}

		public String getDescr() {
			return descr;
		}

	}
}
