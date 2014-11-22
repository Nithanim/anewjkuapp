package org.voidsink.anewjkuapp.base;

import android.content.Context;
import org.voidsink.anewjkuapp.view.StickyListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public abstract class StickyArrayAdapter<T> extends BaseArrayAdapter<T> implements StickyListHeadersAdapter {

    protected StickyListView mCardListView;

    public StickyArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public void setCardListView(StickyListView cardListView) {
        this.mCardListView = cardListView;
    }
}