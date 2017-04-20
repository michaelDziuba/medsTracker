package com.example.cdu.medstracker;

/**
 * Created by web on 2017-02-07.
 */

public class Image {
    /**
     * All Class properties
     */
    private int id;
    private int drug_id = -1; //-1 value indicates that this image object isn't yet associated with a drug object
    private String resource;
    private int pictureWidth;
    private int pictureHeight;

    public Image(int id, int drug_id, String resource){
        this.id = id;
        this.drug_id = drug_id;
        this.resource = resource;
    }


    public Image(int drug_id, String resource, int pictureWidth, int pictureHeight){
        this.id = 0;
        this.drug_id = drug_id;
        this.resource = resource;
        this.pictureWidth = pictureWidth;
        this.pictureHeight = pictureHeight;
    }

    /**
     * Default constructor
     */
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

    public int getDrug_id() {
        return drug_id;
    }

    public void setDrug_id(int drug_id) {
        this.drug_id = drug_id;
    }

    public String getResource() {
        return resource;
    }

    public int getPictureWidth() {
        return pictureWidth;
    }

    public void setPictureWidth(int pictureWidth) {
        this.pictureWidth = pictureWidth;
    }

    public int getPictureHeight() {
        return pictureHeight;
    }

    public void setPictureHeight(int pictureHeight) {
        this.pictureHeight = pictureHeight;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
