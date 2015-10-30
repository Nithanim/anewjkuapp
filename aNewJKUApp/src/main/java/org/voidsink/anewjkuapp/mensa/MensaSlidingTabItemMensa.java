package org.voidsink.anewjkuapp.mensa;

import android.support.v4.app.Fragment;

import org.voidsink.anewjkuapp.base.SlidingTabItem;
import org.voidsink.anewjkuapp.fragment.MensaFragment;
import org.voidsink.anewjkuapp.fragment.MensaFragmentDetailMensa;

import me.nithanim.mensaapi.Type;

public class MensaSlidingTabItemMensa extends SlidingTabItem {
    private final Type type;
    private final MensaFragment.CompleteMensaLoadTask task;

    public MensaSlidingTabItemMensa(CharSequence title, Type type, MensaFragment.CompleteMensaLoadTask task) {
        super(title, null);
        this.type = type;
        this.task = task;
    }

    @Override
    public Fragment createFragment() {
        return new MensaFragmentDetailMensa(type, task);
    }
}
