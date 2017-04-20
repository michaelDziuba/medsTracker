package com.example.cdu.medstracker;

/**
 * Created by cdu on 2017-02-14.
 */

public class Drug {

    /**
     * All properties of the drug class
     */
    private int id;
    private String drugName;
    private String drugDose;
    private String whenToTake;
    private String notes;

    /**
     * Default drug object constructor
     */
    public Drug(){

    }

    /**
     * Drug object constructor initializes all object properties, with values passed as parameters
     *
     * @param id
     * @param drugName
     * @param drugDose
     * @param whenToTake
     * @param notes
     */
    public Drug(int id, String drugName, String drugDose, String whenToTake, String notes){
        this.id = id;
        this.drugName = drugName;
        this.drugDose = drugDose;
        this.whenToTake = whenToTake;
        this.notes = notes;
    }

    /**
     * Drub object constructor initializes all properties based on parameter values,
     * except the id property, which is initialized to 0 value
     *
     * @param drugName
     * @param drugDose
     * @param whenToTake
     * @param notes
     */
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

    /**
     * Method enables the drug object to be treated as a string for printing and other purposes
     *
     * @return the name of the drug object
     */
    @Override
    public String toString(){
        return this.getDrugName();
    }
}
