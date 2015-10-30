package me.nithanim.mensaapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;

import java.io.IOException;

import me.nithanim.mensaapi.converter.JsonToPojoConverter;

public class MensaServiceImpl implements MensaService {
    private static final String MENSA_URL = "http://mensaapi.nithanim.me/v1/mensa.json";

    @Override
    public void invalidateCache(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putLong("MENSA_DATE", 0);
    }

    @Override
    public MensaApiResult load(Context context) throws IOException {
        SharedPreferences sp = getSharedPreferences(context);

        if (shouldUpdateFromServer(context)) {
            JsonLoader loader = new JsonLoader(MENSA_URL, sp.getString("MENSA_ETAG", null));
            LoaderResult loaderResult;
            loaderResult = loader.loadJson();

            if (loaderResult.isSuccessful() && loaderResult.getJson() == null) { //not modified
                SharedPreferences.Editor editor = sp.edit();
                editor.putLong("MENSA_DATE", System.currentTimeMillis());
                editor.apply();
                return loadResultFromStore(context);
            } else if (loaderResult.isSuccessful()) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("MENSA_DATA", loaderResult.getJson());
                editor.putString("MENSA_ETAG", loaderResult.getEtag());
                editor.putLong("MENSA_DATE", System.currentTimeMillis());
                editor.apply();
                return getMensaApiResultFromJson(loaderResult.getJson());
            } else {
                throw new IOException("Unable to update form server response");
            }
        } else {
            return loadResultFromStore(context);
        }
    }

    private boolean shouldUpdateFromServer(Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        long lastUpdate = sp.getLong("MENSA_DATE", 0);
        return lastUpdate + 6 * DateUtils.HOUR_IN_MILLIS <= System.currentTimeMillis();
    }

    private MensaApiResult loadResultFromStore(Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return getMensaApiResultFromJson(sp.getString("MENSA_DATA", null));
    }

    private static MensaApiResult getMensaApiResultFromJson(String json) {
        JsonToPojoConverter converter = new JsonToPojoConverter();
        return converter.convert(json);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
