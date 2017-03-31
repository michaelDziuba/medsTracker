package com.example.cdu.medstracker;

/**
 * Created by cdu on 2017-02-14.
 */

public class Drug {

    private int id;
    private String drugName;
    private String drugDose;
    private String whenToTake;
    private String notes;

    public Drug(){

    }

    public Drug(int id, String drugName, String drugDose, String whenToTake, String notes){
        this.id = id;
        this.drugName = drugName;
        this.drugDose = drugDose;
        this.whenToTake = whenToTake;
        this.notes = notes;
    }

    public Drug(String drugName, String drugDose, String whenToTake, String notes){
        this.id = 0;
        this.drugName = drugName;
        this.drugDose = drugDose;
        this.whenToTake = whenToTake;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugDose() {
        return drugDose;
    }

    public void setDrugDose(String drugDose) {
        this.drugDose = drugDose;
    }

    public String getWhenToTake() {
        return whenToTake;
    }

    public void setWhenToTake(String whenToTake) {
        this.whenToTake = whenToTake;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString(){
        return this.getDrugName();
    }
}
