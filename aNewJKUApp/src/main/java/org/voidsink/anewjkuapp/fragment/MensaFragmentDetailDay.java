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

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaapi.Type;

public class MensaFragmentDetailDay extends MensaFragmentDetailBase {

    public static final String TAG = MensaFragmentDetailDay.class.getSimpleName();

    private LocalDate day;

    public MensaFragmentDetailDay() {
    }

    public MensaFragmentDetailDay(LocalDate day, MensaFragment.CompleteMensaLoadTask task) {
        super(task);
        this.day = day;
    }

    @Override
    protected void onMensasLoaded(Map<Type, List<Menu>> mensas) {
        List<Menu> menus = new ArrayList<>();
        mAdapter.clear();
        for (Map.Entry<Type, List<Menu>> entry : mensas.entrySet()) {
            List<Menu> menusByType = entry.getValue();
            for (Menu menu : menusByType) {
                if (menu.getDate().equals(day)) {
                    menus.add(menu);
                }
            }

        }
        mAdapter.addAll(menus);
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }
}
