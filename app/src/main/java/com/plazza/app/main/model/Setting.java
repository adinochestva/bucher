package com.plazza.app.main.model;

import com.j256.ormlite.field.DatabaseField;

public class Setting {
    @DatabaseField(id = true)
    public int id;
    @DatabaseField
    public String name = "";
    @DatabaseField
    public String val = "";

}
