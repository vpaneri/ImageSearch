package org.wiki.assignment.imagesearch.model;

/**
 * Created by me on 6/18/2017.
 */

public class Image {
    private  String url;
    private  String pageId;


    public Image(String imageurl, String pageId) {
        this.pageId = pageId;
        this.url = imageurl;
    }


    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
