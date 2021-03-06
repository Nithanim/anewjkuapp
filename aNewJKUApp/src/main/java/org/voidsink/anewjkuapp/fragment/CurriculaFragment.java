/*******************************************************************************
 *      ____.____  __.____ ___     _____
 *     |    |    |/ _|    |   \   /  _  \ ______ ______
 *     |    |      < |    |   /  /  /_\  \\____ \\____ \
 * /\__|    |    |  \|    |  /  /    |    \  |_> >  |_> >
 * \________|____|__ \______/   \____|__  /   __/|   __/
 *                  \/                  \/|__|   |__|
 *
 * Copyright (c) 2014-2015 Paul "Marunjar" Pretsch
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 ******************************************************************************/

package org.voidsink.anewjkuapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.voidsink.anewjkuapp.KusssContentContract;
import org.voidsink.anewjkuapp.R;
import org.voidsink.anewjkuapp.base.BaseContentObserver;
import org.voidsink.anewjkuapp.base.BaseFragment;
import org.voidsink.anewjkuapp.base.ContentObserverListener;
import org.voidsink.anewjkuapp.base.RecyclerArrayAdapter;
import org.voidsink.anewjkuapp.kusss.Curriculum;
import org.voidsink.anewjkuapp.provider.KusssContentProvider;
import org.voidsink.anewjkuapp.update.UpdateService;
import org.voidsink.anewjkuapp.utils.Consts;

import java.text.DateFormat;
import java.util.List;

public class CurriculaFragment extends BaseFragment implements
        ContentObserverListener {

    private static final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
    private CurriculaAdapter mAdapter;
    private BaseContentObserver mObserver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(KusssContentContract.AUTHORITY,
                KusssContentContract.Curricula.PATH_CONTENT_CHANGED, 0);

        mObserver = new BaseContentObserver(uriMatcher, this);
        getActivity().getContentResolver().registerContentObserver(
                KusssContentContract.Curricula.CONTENT_CHANGED_URI, false,
                mObserver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getActivity().getContentResolver().unregisterContentObserver(
                mObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        final RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mAdapter = new CurriculaAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(mAdapter));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.curricula, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh_curricula: {
                Intent mUpdateService = new Intent(getActivity(), UpdateService.class);
                mUpdateService.putExtra(Consts.ARG_UPDATE_KUSSS_CURRICULA, true);
                getActivity().startService(mUpdateService);

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadData() {

        new AsyncTask<Void, Void, Void>() {

            public List<Curriculum> mCurricula;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                mCurricula = KusssContentProvider.getCurricula(getContext());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                mAdapter.clear();
                mAdapter.addAll(mCurricula);
                mAdapter.notifyDataSetChanged();

                super.onPostExecute(aVoid);
            }
        }.execute();

    }

    @Override
    public void onContentChanged(boolean selfChange) {
        loadData();
    }

    private static class CurriculaAdapter extends RecyclerArrayAdapter<Curriculum, CurriculumViewHolder> implements StickyRecyclerHeadersAdapter<CurriculumHeaderHolder> {

        private final Context mContext;

        public CurriculaAdapter(Context context) {
            super();
            mContext = context;
        }

        @Override
        public CurriculumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.curricula_list_item, parent, false);
            return new CurriculumViewHolder(v);
        }

        @Override
        public void onBindViewHolder(CurriculumViewHolder holder, int position) {
            Curriculum item = getItem(position);

            holder.isStandard.setText(item.isStandard() ? mContext.getString(R.string.curriculum_is_standard_yes) : mContext.getString(R.string.curriculum_is_standard_no));
            holder.cid.setText(item.getCid());
            holder.title.setText(item.getTitle());
            holder.steopDone.setText(item.isSteopDone() ? mContext.getString(R.string.curriculum_steop_done_yes) : mContext.getString(R.string.curriculum_steop_done_no));
            holder.activeStatus.setText(item.isActive() ? mContext.getString(R.string.curriculum_active_status_yes) : mContext.getString(R.string.curriculum_active_status_no));
            if (item.getDtStart() != null) {
                holder.dtStart.setText(dateFormat.format(item.getDtStart()));
            }
            if (item.getDtEnd() != null) {
                holder.dtEnd.setText(dateFormat.format(item.getDtEnd()));
            }
        }

        @Override
        public long getHeaderId(int i) {
            Curriculum curriculum = getItem(i);
            if (curriculum != null) {
                return (long) curriculum.getUni().hashCode() + (long) Integer.MAX_VALUE; // header id has to be > 0???
            }
            return 0;
        }

        @Override
        public CurriculumHeaderHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_header, viewGroup, false);
            return new CurriculumHeaderHolder(v);
        }

        @Override
        public void onBindHeaderViewHolder(CurriculumHeaderHolder curriculumHeaderHolder, int position) {
            Curriculum curriculum = getItem(position);
            curriculumHeaderHolder.mText.setText(curriculum.getUni());
        }
    }

    public static class CurriculumViewHolder extends RecyclerView.ViewHolder {
        public TextView isStandard;
        public TextView cid;
        public TextView title;
        public TextView steopDone;
        public TextView activeStatus;
        public TextView dtStart;
        public TextView dtEnd;

        public CurriculumViewHolder(View itemView) {
            super(itemView);

            isStandard = (TextView) itemView.findViewById(R.id.curriculum_is_standard);
            cid = (TextView) itemView.findViewById(R.id.curriculum_id);
            title = (TextView) itemView.findViewById(R.id.curriculum_title);
            steopDone = (TextView) itemView.findViewById(R.id.curriculum_steop_done);
            activeStatus = (TextView) itemView.findViewById(R.id.curriculum_active_status);
            dtStart = (TextView) itemView.findViewById(R.id.curriculum_dt_start);
            dtEnd = (TextView) itemView.findViewById(R.id.curriculum_dt_end);
        }
    }

    public static class CurriculumHeaderHolder extends RecyclerView.ViewHolder {
        public TextView mText;

        public CurriculumHeaderHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.list_header_text);
        }
    }
}
