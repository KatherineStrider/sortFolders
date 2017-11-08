package com.example.kate.sort_folders.models;

/**
 * Created by Kate on 08.11.2017.
 */

public class Settings {

    String pathWhence;
    String pathWhere;

    boolean needToDelete;

    Settings (String pathWhence,String pathWhere){
        this.pathWhence = pathWhence;
        this.pathWhere = pathWhere;
    }

    public boolean isNeedToDelete() {
        return needToDelete;
    }

    public void setNeedToDelete(boolean needToDelete) {
        this.needToDelete = needToDelete;
    }
}
