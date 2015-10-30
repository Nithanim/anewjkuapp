/*******************************************************************************
 * ____.____  __.____ ___     _____
 * |    |    |/ _|    |   \   /  _  \ ______ ______
 * |    |      < |    |   /  /  /_\  \\____ \\____ \
 * /\__|    |    |  \|    |  /  /    |    \  |_> >  |_> >
 * \________|____|__ \______/   \____|__  /   __/|   __/
 * \/                  \/|__|   |__|
 * <p/>
 * Copyright (c) 2014-2015 Paul "Marunjar" Pretsch
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 ******************************************************************************/

package org.voidsink.anewjkuapp.fragment;

import android.os.AsyncTask;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.voidsink.anewjkuapp.PreferenceWrapper;
import org.voidsink.anewjkuapp.R;
import org.voidsink.anewjkuapp.base.SlidingTabItem;
import org.voidsink.anewjkuapp.base.SlidingTabsFragment;
import org.voidsink.anewjkuapp.mensa.MensaSlidingTabItemDay;
import org.voidsink.anewjkuapp.mensa.MensaSlidingTabItemMensa;
import org.voidsink.anewjkuapp.utils.Consts;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import me.nithanim.mensaapi.MensaApiResult;
import me.nithanim.mensaapi.MensaService;
import me.nithanim.mensaapi.MensaServiceImpl;
import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaapi.Type;

public class MensaFragment extends SlidingTabsFragment {
    private final MensaService mensaService = new MensaServiceImpl();
    public static final String TAG = MensaFragment.class.getSimpleName();

    @Override
    protected void fillTabs(List<SlidingTabItem> mTabs) {
        CompleteMensaLoadTask task = new CompleteMensaLoadTask();
        task.execute();

        if (PreferenceWrapper.getGroupMenuByDay(getContext())) {
            DateTime dateTime = new DateTime();
            // jump to next day if later than 4pm
            if (dateTime.hourOfDay().get() >= 16) {
                dateTime.plusDays(1);
            }
            LocalDate date = dateTime.toLocalDate();

            // add days until next friday
            do {
                // do not add weekend (no menu)
                int dow = date.getDayOfWeek();
                if (dow != DateTimeConstants.SATURDAY && dow != DateTimeConstants.SUNDAY) {
                    mTabs.add(new MensaSlidingTabItemDay(getTabTitle(date), date, task));
                }
                // increment day
                date = date.plusDays(1);
            } while (date.getDayOfWeek() != DateTimeConstants.SATURDAY);
        } else {
            mTabs.add(new MensaSlidingTabItemMensa(getString(R.string.mensa_title_classic), Type.CLASSIC, task));
            mTabs.add(new MensaSlidingTabItemMensa(getString(R.string.mensa_title_choice), Type.CHOICE, task));
            mTabs.add(new MensaSlidingTabItemMensa(getString(R.string.mensa_title_khg), Type.KHG, task));
            mTabs.add(new MensaSlidingTabItemMensa(getString(R.string.mensa_title_raab), Type.RAAB, task));
        }
    }

    private String getTabTitle(LocalDate date) {
        LocalDate now = new LocalDate();
        if (now.isEqual(date)) {
            return getResources().getString(R.string.mensa_menu_today);
        } else if (now.plusDays(1).isEqual(date)) {
            return getResources().getString(R.string.mensa_menu_tomorrow);
        }
        return new SimpleDateFormat("EEEE").format(date.toDate());
    }

    @Override
    protected String getScreenName() {
        return Consts.SCREEN_MENSA;
    }

    public class CompleteMensaLoadTask extends AsyncTask<Void, Double, Map<Type, List<Menu>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (getContext() == null) {
                Log.e(TAG, "context is null");
            }
        }

        @Override
        protected Map<Type, List<Menu>> doInBackground(Void... nothing) {
            MensaApiResult mensaApiResult = null;
            try {
                mensaApiResult = mensaService.load(getContext());
            } catch (IOException e) {
                Log.e(TAG, "Unable to load mensa data!");
                return null;
            }

            Map<Type, List<Menu>> rawMensas = mensaApiResult.getResult();
            return rawMensas;
        }

        @Override
        protected void onPostExecute(Map<Type, List<Menu>> result) {
            Comparator<Menu> comparator = new Comparator<Menu>() {
                @Override
                public int compare(Menu lhs, Menu rhs) {
                    return lhs.getDate().compareTo(rhs.getDate());
                }
            };

            if (result != null) {
                for (Map.Entry<Type, List<Menu>> entry : result.entrySet()) {
                    Collections.sort(entry.getValue(), comparator);
                }
            }
            super.onPostExecute(result);
        }

        private CompleteMensaLoadTask() {
        }
    }
}
