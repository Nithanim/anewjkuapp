package org.voidsink.anewjkuapp.mensa;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import org.voidsink.anewjkuapp.R;
import org.voidsink.anewjkuapp.base.RecyclerArrayAdapter;
import org.voidsink.anewjkuapp.utils.UIUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import me.nithanim.mensaapi.Meal;
import me.nithanim.mensaapi.Menu;

public class MenuRecyclerListAdapter
        extends RecyclerArrayAdapter<Menu, MenuRecyclerListAdapter.MenuInfoHolder>
        implements StickyRecyclerHeadersAdapter<MenuRecyclerListAdapter.MenuHeaderHolder> {
    private static final DateFormat df = SimpleDateFormat.getDateInstance();

    private Context context;
    protected boolean mUseDateHeader;

    public MenuRecyclerListAdapter(Context context, boolean useDateHeader) {
        super();
        this.context = context;
        this.mUseDateHeader = useDateHeader;
    }

    @Override
    public MenuInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mensa_menu_item, parent, false);
                return new MenuInfoHolder(v);
            }
            default:
                throw new IllegalStateException();
        }
    }

    private static int count = 0;

    @Override
    public void onBindViewHolder(MenuInfoHolder holder, int position) {
        Menu menu = getItem(position);

        UIUtils.setTextAndVisibility(holder.title, menu.getSubtype());

        String price = (menu.getPrice() > 0)
                ? (context.getString(R.string.mensa_price, (menu.getPrice() - menu.getOehBonus()) / 100f))
                : null;
        UIUtils.setTextAndVisibility(holder.price, price);

        String oehBonus = (menu.getOehBonus() > 0)
                ? (context.getString(R.string.mensa_oehBonus, menu.getOehBonus() / 100f))
                : null;
        UIUtils.setTextAndVisibility(holder.oehBonus, oehBonus);

        //this method is also called with an recycled holder, so reset it completely
        LinearLayout layout = holder.meals;
        layout.removeAllViews();
        for (Meal meal : menu.getMeals()) {
            View mealView = generateMealView(meal);
            layout.addView(mealView);
        }
    }

    private View generateMealView(Meal meal) {
        View mealView = LayoutInflater.from(context).inflate(R.layout.mensa_meal_item, null, false);
        TextView titleView = (TextView) mealView.findViewById(R.id.mensa_meal_item_desc);
        TextView priceView = (TextView) mealView.findViewById(R.id.mensa_meal_item_price);
        TextView oehBonusView = (TextView) mealView.findViewById(R.id.mensa_meal_item_oeh_bonus);

        UIUtils.setTextAndVisibility(titleView, meal.getDesc());

        String price = meal.getPrice() > 0 ? context.getString(R.string.mensa_price, meal.getPrice() / 100f) : null;
        UIUtils.setTextAndVisibility(priceView, price);
        return mealView;
    }

    @Override
    public MenuHeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_header, parent, false);
        return new MenuHeaderHolder(v);
    }

    @Override
    public void onBindHeaderViewHolder(MenuHeaderHolder menuHeaderHolder, int position) {
        Menu menu = getItem(position);
        if (menu != null) {
            /*if (mUseDateHeader) {
                final MensaDay day = item.getDay();
                if (day != null) {
                    menuHeaderHolder.mText.setText(df.format(day.getDate()));
                }
            } else {*/
            menuHeaderHolder.text.setText(df.format(menu.getDate().toDate()));
        }
    }

    @Override
    public long getHeaderId(int position) {
        Menu menu = getItem(position);
        if (menu != null) {
            if (mUseDateHeader) {
                return menu.getDate().hashCode();
            } else {
                //final Mensa mensa = item.getMensa();
                //if (mensa != null) {
                //    return (long) mensa.getName().hashCode() + (long) Integer.MAX_VALUE; // header id has to be > 0???
                //}
            }
        }
        return 0;
    }

    public static class MenuHeaderHolder extends RecyclerView.ViewHolder {
        public TextView text;

        public MenuHeaderHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.list_header_text);
        }
    }

    public static class MenuInfoHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final LinearLayout meals;
        public final TextView price;
        public final TextView oehBonus;

        public final View layout;

        public MenuInfoHolder(View itemView) {
            super(itemView);
            layout = itemView;

            title = (TextView) itemView.findViewById(R.id.mensa_menu_item_title);
            meals = (LinearLayout) itemView.findViewById(R.id.mensa_menu_item_meals);
            price = (TextView) itemView.findViewById(R.id.mensa_menu_item_price);
            oehBonus = (TextView) itemView.findViewById(R.id.mensa_menu_item_oeh_bonus);
        }
    }
}
