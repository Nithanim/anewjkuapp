package me.nithanim.mensaapi;

import android.content.Context;

import java.io.IOException;

public interface MensaService {
    /**
     * Loads the menu either from server or from a local store.
     *
     * @param context the context to save the result in
     * @return the LoaderResult either loaded from the local store or directly from the server.
     */
    MensaApiResult load(Context context) throws IOException;

    /**
     * Invalidates the cache so that everything is fetched from server on next request.
     *
     * @param context
     */
    void invalidateCache(Context context);
}
