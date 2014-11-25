package org.voidsink.anewjkuapp.rss;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.voidsink.anewjkuapp.R;
import org.voidsink.anewjkuapp.activity.RssFeedEntryActivity;
import org.voidsink.anewjkuapp.base.BaseFragment;
import org.voidsink.anewjkuapp.rss.lib.FeedEntry;
import org.voidsink.anewjkuapp.rss.lib.FeedPullParser;
import org.voidsink.anewjkuapp.utils.Consts;
import org.voidsink.anewjkuapp.view.ListViewWithHeader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by paul on 16.11.2014.
 */
public class RssFeedFragment extends BaseFragment {

    private URL mUrl = null;
    private RssListAdapter mCardArrayAdapter;
    private DisplayImageOptions mOptions;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        try {
            mUrl = new URL(args.getString(Consts.ARG_FEED_URL));
        } catch (MalformedURLException e) {
            mUrl = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageForEmptyUri(getResources().getDrawable(R.drawable.ic_launcher))
                .showImageOnFail(getResources().getDrawable(R.drawable.ic_launcher))
                .build();
    }

    private void startFeedDetailView(FeedEntry entry) {
        Intent i = new Intent(getContext(), RssFeedEntryActivity.class);

        i.putExtra(Consts.ARG_FEED_ENTRY, entry);

        getContext().startActivity(i);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rss_feed, container, false);

        final ListViewWithHeader mCardListView = (ListViewWithHeader) v.findViewById(R.id.rssfeed_list);

        mCardArrayAdapter = new RssListAdapter(getContext(), mOptions);
        mCardListView.setAdapter(mCardArrayAdapter);

        mCardListView.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = mCardListView.getItemAtPosition(i);
                if (o instanceof FeedEntry) {
                    startFeedDetailView((FeedEntry) o);
                }
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateData();
    }

    private void updateData() {
        new LoadFeedTask().execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.rss_feed, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh_feed:
                updateData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class LoadFeedTask extends AsyncTask<Void, Void, Void> {

        private List<FeedEntry> mFeed = null;
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(getContext(),
                    getContext().getString(R.string.progress_title),
                    getContext().getString(R.string.progress_load_feed), true);

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mFeed = new FeedPullParser().parse(mUrl);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mCardArrayAdapter != null) {
                mCardArrayAdapter.clear();
                if (mFeed != null) {
                    mCardArrayAdapter.addAll(mFeed);
                } else {
                    Toast.makeText(getContext(), "TODO: Error loading feed.", Toast.LENGTH_SHORT).show();
                }
                mCardArrayAdapter.notifyDataSetChanged();
            }

            mProgressDialog.dismiss();

            super.onPostExecute(aVoid);
        }
    }

    @Override
    protected String getScreenName() {
        return Consts.SCREEN_RSS_FEED;
    }
}
