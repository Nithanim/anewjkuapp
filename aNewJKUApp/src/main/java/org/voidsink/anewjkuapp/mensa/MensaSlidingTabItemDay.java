package org.voidsink.anewjkuapp.mensa;

import android.support.v4.app.Fragment;

import org.joda.time.LocalDate;
import org.voidsink.anewjkuapp.base.SlidingTabItem;
import org.voidsink.anewjkuapp.fragment.MensaFragment;
import org.voidsink.anewjkuapp.fragment.MensaFragmentDetailDay;

public class MensaSlidingTabItemDay extends SlidingTabItem {
    private final LocalDate day;
    private final MensaFragment.CompleteMensaLoadTask task;

    public MensaSlidingTabItemDay(CharSequence title, LocalDate day, MensaFragment.CompleteMensaLoadTask task) {
        super(title, null);
        this.day = day;
        this.task = task;
    }

    @Override
    public Fragment createFragment() {
        return new MensaFragmentDetailDay(day, task);
    }
}
