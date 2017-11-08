package com.example.kate.sort_folders.models;

/**
 * Created by Kate on 08.11.2017.
 */

public class CopyFile {

    String path;
    String name;
    Long date;

    public CopyFile(String path, String name, Long date) {
        this.path = path;
        this.name = name;
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }
}
