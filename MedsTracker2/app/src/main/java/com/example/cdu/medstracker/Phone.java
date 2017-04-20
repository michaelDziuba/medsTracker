package com.example.cdu.medstracker;

/**
 * Created by cdu on 2017-04-01.
 */

public class Phone {

    /**
     * All properties of the object
     */
    private int id;
    private String phoneName;
    private String phoneNumber;
    private String phoneNote;

    /**
     * Default constructor
     */
    public Phone(){

    }

    public Phone(int id, String phoneName, String phoneNumber, String phoneNote){
        this.id = id;
        this.phoneName = phoneName;
        this.phoneNumber = phoneNumber;
        this.phoneNote = phoneNote;
    }

    public Phone(String phoneName, String phoneNumber, String phoneNote){
        this.id = 0;
        this.phoneName = phoneName;
        this.phoneNumber = phoneNumber;
        this.phoneNote = phoneNote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNote() {
        return phoneNote;
    }

    public void setPhoneNote(String phoneNote) {
        this.phoneNote = phoneNote;
    }

    @Override
    public String toString(){
        return this.getPhoneName();
    }
}
