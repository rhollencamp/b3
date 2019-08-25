package com.cargurus.percolator.domain;

public class Breadcrumb {

    private final String title;
    private final String url;

    public Breadcrumb(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
