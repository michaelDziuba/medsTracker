package com.example.install.medstracker;

/**
 * Created by web on 2017-02-07.
 */

public class Image {
    private int id;
    private int book_id = -1;
    private String resource;

    public Image(int id, int book_id, String resource){
        this.id = id;
        this.book_id = book_id;
        this.resource = resource;
    }

    public Image(int book_id, String resource){
        this.id = 0;
        this.book_id = book_id;
        this.resource = resource;
    }

    public Image(){

    }
    public Image(String resource){
        this.resource = resource;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
