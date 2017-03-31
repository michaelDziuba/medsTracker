package com.example.cdu.medstracker;

/**
 * Created by web on 2017-02-07.
 */

public class Image {
    private int id;
    private int drug_id = -1;
    private String resource;

    public Image(int id, int drug_id, String resource){
        this.id = id;
        this.drug_id = drug_id;
        this.resource = resource;
    }

    public Image(int drug_id, String resource){
        this.id = 0;
        this.drug_id = drug_id;
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

    public int getDrug_id() {
        return drug_id;
    }

    public void setDrug_id(int drug_id) {
        this.drug_id = drug_id;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
