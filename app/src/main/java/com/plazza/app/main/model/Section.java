package com.plazza.app.main.model;

import com.j256.ormlite.field.DatabaseField;

public class Section {
    @DatabaseField(id = true)
    public int id;
    @DatabaseField
    public String name = "";
    @DatabaseField
    public String descr = "";
    @DatabaseField
    public String descr2 = "";
    @DatabaseField
    public String descr3 = "";
    @DatabaseField
    public String img = "";
    @DatabaseField
    public String icon = "";
    @DatabaseField
    public int parent;
    @DatabaseField
    public int type;
    @DatabaseField
    public int or;
    @DatabaseField
    public int place;
    @DatabaseField
    public int op1;
    @DatabaseField
    public int op2;
    @DatabaseField
    public int op3;
    @DatabaseField
    public int op4;
    @DatabaseField
    public int op5;

}
