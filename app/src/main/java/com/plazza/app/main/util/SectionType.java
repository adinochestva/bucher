package com.plazza.app.main.util;

public enum SectionType {
    HTML("HTML", 1),
    List("List", 2),
    Call("Call", 3),
    Rss("Rss", 4),
    Link("Link", 5),
    Contact("Contact", 6),
    Gallery("Gallery", 7),
    Image("Image", 8),
    Video("Video", 9),
    Search("Search", 10);


    private String stringValue;
    private int intValue;

    private SectionType(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    public int value() {
        return intValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}