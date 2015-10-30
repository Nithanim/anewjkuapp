package me.nithanim.mensaapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonLoader {
    private final String urlString;
    private String etag;

    public JsonLoader(String urlString, String etag) {
        this.urlString = urlString;
        this.etag = etag;
    }


    public LoaderResult loadJson() throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            conn.setRequestProperty("If-None-Match", etag);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(15000);

            if (conn.getResponseCode() == 304) { //not modified
                return new LoaderResult(true, null, etag);
            } else if (conn.getResponseCode() == 200) {
                String json = readStringFromStream(conn.getInputStream());
                return new LoaderResult(true, json, conn.getHeaderField("Etag"));
            } else {
                throw new IOException("Server sent unknown status code " + conn.getResponseCode());
            }
        } finally {
            conn.disconnect();
        }
    }

    private static String readStringFromStream(InputStream in) throws IOException {
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        int n;
        while ((n = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, n);
        }

        return writer.toString();
    }
}
