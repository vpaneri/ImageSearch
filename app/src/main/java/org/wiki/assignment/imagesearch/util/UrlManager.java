package org.wiki.assignment.imagesearch.util;

import android.net.Uri;



/**
 * Created by me on 6/18/2017.
 */

public class UrlManager {


    private static final  String ENDPOINT = "https://en.wikipedia.org/w/api.php";
    public static final String SEARCH_QUERY = "searchQuery";
    private static volatile UrlManager instance = null;
    private UrlManager() {

    }

    public static UrlManager getInstance() {
        if (instance == null) {
            synchronized (UrlManager.class) {
                if (instance == null) {
                    instance = new UrlManager();
                }
            }
        }
        return instance;
    }

    public static String getItemUrl(String query) {
        String url = null;

        url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter("action", "query")
                    .appendQueryParameter("prop", "pageimages")
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("piprop", "thumbnail")
                    .appendQueryParameter("pithumbsize", "96")
                    .appendQueryParameter("pilimit", "50")
                    .appendQueryParameter("generator", "prefixsearch")
                    .appendQueryParameter("gpssearch", query)
                    .appendQueryParameter("gpslimit", "50")
                    .build().toString();

        return url;
    }
}
