package com.example.cdu.medstracker;

/**
 * Created by cdu on 2017-02-14.
 */

public class Drug {

    private int id;
    private String title;
    private String description;
    private String url;

    public Drug(){

    }

    public Drug(int id, String title, String description, String url){
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
    }

    public Drug(String title, String description, String url){
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString(){
        return this.getTitle();
    }
}
