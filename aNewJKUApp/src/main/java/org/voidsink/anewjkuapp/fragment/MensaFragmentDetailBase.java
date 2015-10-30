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

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.joda.time.LocalDate;
import org.voidsink.anewjkuapp.R;
import org.voidsink.anewjkuapp.base.BaseFragment;
import org.voidsink.anewjkuapp.mensa.MenuRecyclerListAdapter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import me.nithanim.mensaapi.Menu;
import me.nithanim.mensaapi.Type;

public abstract class MensaFragmentDetailBase extends BaseFragment {

    public static final String TAG = MensaFragmentDetailBase.class.getSimpleName();
    protected MenuRecyclerListAdapter mAdapter;
    protected RecyclerView mRecyclerView;

    private MensaFragment.CompleteMensaLoadTask task;

    public MensaFragmentDetailBase() {
    }

    public MensaFragmentDetailBase(MensaFragment.CompleteMensaLoadTask task) {
        this.task = task;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MenuRecyclerListAdapter(getContext(), true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(mAdapter));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new MenuLoadTask().execute();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    protected abstract void onMensasLoaded(Map<Type, List<Menu>> mensas);

    private class MenuLoadTask extends AsyncTask<Void, Void, Map<Type, List<Menu>>> {
        private Context mContext;
        private int mSelectPosition;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mContext = MensaFragmentDetailBase.this.getContext();
            if (mContext == null) {
                Log.e(TAG, "context is null");
            }
            mSelectPosition = -1;
        }

        private Map<Type, List<Menu>> getApiMenus() {
            try {
                return task.get();
            } catch (InterruptedException | ExecutionException e) {
                Log.e(TAG, "Unable to get mensa info!");
                return null;
            }
        }

        @Override
        protected Map<Type, List<Menu>> doInBackground(Void... voids) {
            Map<Type, List<Menu>> mensas = getApiMenus();

            LocalDate startOfWeek = getStartOfTheWeek();
            LocalDate today = new LocalDate();
            if (mensas != null) {
                for (Map.Entry<Type, List<Menu>> entry : mensas.entrySet()) {
                    List<Menu> menus = entry.getValue();
                    for (Menu menu : menus) {
                        // allow only menus >= start of this week
                        LocalDate date = menu.getDate();
                        if ((menu.getDate() != null) && (date.isAfter(startOfWeek))) {
                            // remember position of menu for today for scrolling to item after update
                            if (mSelectPosition == -1 && date.isEqual(today)) {
                                mSelectPosition = menus.size() - 1;
                            }
                        }
                    }
                }
            }

            return mensas;
        }

        private LocalDate getStartOfTheWeek() {
            LocalDate day = new LocalDate().dayOfWeek().withMinimumValue().minusDays(1);
            return day;
        }

        @Override
        protected void onPostExecute(Map<Type, List<Menu>> result) {
            onMensasLoaded(result);
            // scroll to today's menu
            if (mSelectPosition >= 0 && mRecyclerView != null) {
                mRecyclerView.smoothScrollToPosition(mSelectPosition);
            }
        }
    }

    public void setTask(MensaFragment.CompleteMensaLoadTask task) {
        this.task = task;
    }
}
