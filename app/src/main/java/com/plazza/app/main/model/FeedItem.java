package com.plazza.app.main.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "item", strict = false)
public class FeedItem implements Serializable {
    @Element(name = "pubDate")
    private String mpubDate;
    @Element(name = "title")
    private String mtitle;
    @Element(name = "link")
    private String mlink;
    @Element(name = "description")
    private String mdescription;

    public FeedItem() {
    }

    public FeedItem(String mdescription, String mlink, String mtitle, String mpubDate) {
        this.mdescription = mdescription;
        this.mlink = mlink;
        this.mtitle = mtitle;
        this.mpubDate = mpubDate;
    }

    public String getPubDate() {
        return mpubDate;
    }

    public void setMpubDate(String mpubDate) {
        this.mpubDate = mpubDate;
    }

    public String getTitle() {
        return mtitle;
    }

    public void setMtitle(String mtitle) {
        this.mtitle = mtitle;
    }

    public String getLink() {
        return mlink;
    }

    public void setMlink(String mlink) {
        this.mlink = mlink;
    }

    public String getDescription() {
        return mdescription;
    }

    public void setMdescription(String mdescription) {
        this.mdescription = mdescription;
    }
}