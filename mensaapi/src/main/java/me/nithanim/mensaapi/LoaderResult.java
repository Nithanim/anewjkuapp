package me.nithanim.mensaapi;

public class LoaderResult {
    private boolean successful;
    private final String json;
    private final String etag;

    public LoaderResult(boolean successful, String json, String etag) {
        this.successful = successful;
        this.json = json;
        this.etag = etag;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getEtag() {
        return etag;
    }

    public String getJson() {
        return json;
    }
}
