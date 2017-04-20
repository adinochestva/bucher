package com.nothio.bucher.model;

import com.j256.ormlite.field.DatabaseField;

public class Section {
    @DatabaseField(id = true)
    public int id;
    @DatabaseField
    public String name = "";
    @DatabaseField
    public String descr = "";
    @DatabaseField
    public String img = "";
    @DatabaseField
    public int parent;

}
