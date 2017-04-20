package com.example.cdu.medstracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "medstracker.db";  //database name
    private static final int DATABASE_VERSION = 1;  //database version

    private static final String ID = "_id";  //common column name for all tables

    private static final String DRUGS = "drugs";  //table name
    private static final String  DRUG_NAME = "drugName";  //column name for drugs table
    private static final String DRUG_DOSE = "drugDose"; //column name for drugs table
    private static final String WHEN_TO_TAKE = "whenToTake"; //column name for drugs table
    private static final String NOTES = "notes"; //column name for drugs table


    private static final String IMAGES = "images";  //table name
    private static final String DRUG_ID = "drug_id";  //column name for images table
    private static final String IMAGE_PATH = "image_path";  //column name for images table
    private static final String IMAGE_WIDTH = "image_width"; //column name for images table
    private static final String IMAGE_HEIGHT = "image_height"; //column name for images table


    private static final String PHONES = "phones";  //table name
    private static final String  PHONE_NAME = "phoneName";  //column name for phones table
    private static final String PHONE_NUMBER = "phoneNumber"; //column name for phones table
    private static final String PHONE_NOTE = "phoneNote"; //column name for phones table

    //SQLITE statement for creating a drugs table
    private static final String CREATE_DRUGS_TABLE = "CREATE TABLE IF NOT EXISTS " +  DRUGS + " (" +
                                                    ID + " INTEGER PRIMARY KEY, " +
                                                    DRUG_NAME + " TEXT, " +
                                                    DRUG_DOSE + " TEXT, " +
                                                    WHEN_TO_TAKE + " TEXT, " +
                                                    NOTES + " TEXT)";

    //SQLITE statement for creating an images table
    private static final String CREATE_IMAGES_TABLE = "CREATE TABLE IF NOT EXISTS " + IMAGES + " (" +
            ID + " INTEGER PRIMARY KEY, " +
            DRUG_ID + " INTEGER REFERENCES " + DRUGS + "("  + ID + "), " +
            IMAGE_PATH + " TEXT, " +
            IMAGE_WIDTH + " INTEGER, " +
            IMAGE_HEIGHT + " INTEGER)";

    //SQLITE statement for creating a phones table
    private static final String CREATE_PHONES_TABLE = "CREATE TABLE IF NOT EXISTS " +  PHONES + " (" +
            ID + " INTEGER PRIMARY KEY, " +
            PHONE_NAME + " TEXT, " +
            PHONE_NUMBER + " TEXT, " +
            PHONE_NOTE + " TEXT)";

    /**
     * Creates a databaseHandler object
     *
     * @param context The context for the SQLITE database and the DatabaseHandler object
     */
    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * Creates 3 SQLITE tables: drugs, images, and phones (if they don't exist in the databse)
     *
     * @param db  The SQLITE database for this app
     */
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_DRUGS_TABLE);
        db.execSQL(CREATE_IMAGES_TABLE);
        db.execSQL(CREATE_PHONES_TABLE);
    }


    /**
     * Mthod deletes all database tables and calls on the onCreate method to create new tables, when the database is upgraded to a new version
     *
     * @param db  This app's SQLITE database
     * @param oldVersion  previous version number for this app's SQLITE database
     * @param newVersion new version number for this app's SQLITE database
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion ){
        db.execSQL("DROP TABLE IF EXISTS " + DRUGS);
        db.execSQL("DROP TABLE IF EXISTS " + IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + PHONES);
        onCreate(db);
    }

    /**
     * CRUD OPERATIONS FOR THE DATABASE AND TABLES
     * Create
     * Read
     * Update
     * Delete
     */


    /**
     * Method adds a drug entry into the drugs table
     *
     * @param drug object containing information for a row in the drugs table
     */
    public void addDrug(Drug drug){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(ID, 0);
        values.put(DRUG_NAME, drug.getDrugName());
        values.put(DRUG_DOSE, drug.getDrugDose());
        values.put(WHEN_TO_TAKE, drug.getWhenToTake());
        values.put(NOTES, drug.getNotes());
        db.insert(DRUGS, null, values);
    }


    /**
     * Method created and returns a drug object, identified by its id number, based on its information in the drugs table
     *
     * @param id the id of the drug object
     * @return drug object identified by the id paramenter
     */
    public Drug getDrug(int id){
        /**
         * Create a readable database
         */
        SQLiteDatabase db = this.getReadableDatabase();
        /**
         * Create a cursor
         * (which is able to move through and access database records)
         * Have it store all the records retrieved from the db.query()
         * cursor starts by pointing at record 0
         * Databases do not have a record 0
         * we use cursor.moveToFirst() to have it at the first record returned
         */
        Cursor cursor = db.query(DRUGS,
                new String[] {ID, DRUG_NAME, DRUG_DOSE, WHEN_TO_TAKE, NOTES},
                "=?", new String[]{String.valueOf(id)}, null, null, null);
        if(cursor != null)
            cursor.moveToFirst();
        /**
         * We create a location object using the cursor record
         */
        Drug drug = new Drug(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        return drug;
    }

    /**
     * Method creates and returns an arrayList of drugs objects,
     * with each object based on the information in a row of the drugs table
     *
     * @return returns an arrayList of drug objects that correspond to the entries in the drugs database table
     */
    public ArrayList<Drug> getAllDrugs(){
        ArrayList<Drug> drugList = new ArrayList<Drug>();
        String selectQuery = "SELECT * FROM " + DRUGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                Drug drug = new Drug();
                drug.setId(cursor.getInt(0));
                drug.setDrugName(cursor.getString(1));
                drug.setDrugDose(cursor.getString(2));
                drug.setWhenToTake(cursor.getString(3));
                drug.setNotes(cursor.getString(4));
                drugList.add(drug);

            } while(cursor.moveToNext());
        }
        return  drugList;
    }


    /**
     * Method updates information in the drugs table, based on the information in the parameter's drug object
     *
     * @param drug object containing updated information for a row in the drugs table
     * @return returns the id of the updated row in the drugs table
     */
    public int updateDrug(Drug drug){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DRUG_NAME, drug.getDrugName());
        values.put(DRUG_DOSE, drug.getDrugDose());
        values.put(WHEN_TO_TAKE, drug.getWhenToTake());
        values.put(NOTES, drug.getNotes());
        return db.update(DRUGS, values, ID + " = ?", new String[]{String.valueOf(drug.getId())});
    }

    /**
     * Method deletes a row from the drugs table identified by the drub_id parameter
     *
     * @param drug_id  the drugs table row id, that identifies which row to delete from the table
     */
    public void deleteDrug(long drug_id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DRUGS, ID + " = ?", new String[]{String.valueOf(drug_id)});
    }

    /**
     * Method closes the database connection, if it's open
     */
    public void closeDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        if(db != null && db.isOpen()){
            db.close();
        }
    }


    /**
     * Method checks if a table exists in the database
     *
     * @param tableName The name of the table to check for existence in the database
     * @return returns true or false, depending on whether the specified table exists in the databse or not
     */
    public boolean isTable(String tableName){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SHOW TABLES LIKE " + tableName;
        Cursor cursor = db.rawQuery(sql, null);

        int numberOfTables = cursor.getCount();

        return numberOfTables == 0 ? false : true;
    }


    /**
     * Method adds an image row to the images table based on the information in the parameter's image object
     *
     * @param image The image object containing information for adding a row in the images table
     * @return the id of the newly added table row in the images table
     */
    public long addImage(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DRUG_ID, image.getDrug_id());
        contentValues.put(IMAGE_PATH, image.getResource());
        contentValues.put(IMAGE_WIDTH, image.getPictureWidth());
        contentValues.put(IMAGE_HEIGHT, image.getPictureHeight());
        Long lastInsertId = db.insert(IMAGES, null, contentValues);
        db.close();
        return lastInsertId;
    }

    /**
     * Method deletes a row in the images table based on the parameter's drug_id value
     *
     * @param drug_id the id for the table row where to delete in the images table
     */
    public void deleteImage(long drug_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(IMAGES, DRUG_ID + " = ?", new String[] { String.valueOf(drug_id) });
    }

    /**
     * Method updates information in the images table, for the row corresponding to the parameter's image object
     *
     * @param image The image object for which to update a corresponding row in the images table
     * @return returns the id of the updated table row
     */
    public int updateImage(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DRUG_ID, image.getDrug_id());
        contentValues.put(IMAGE_PATH, image.getResource());
        contentValues.put(IMAGE_WIDTH, image.getPictureWidth());
        contentValues.put(IMAGE_HEIGHT, image.getPictureHeight());
        return db.update(IMAGES, contentValues, ID + " = ?", new String[] { String.valueOf(image.getId()) });
    }


    /**
     * Method creates and returns an arrayList of image objects,
     * with each object based on the information in a row of the images table
     *
     * @return returns an arrayList of image objects that correspond to the entries in the images database table
     */
    public ArrayList<Image> getAllImages() {
        ArrayList<Image> imageList = new ArrayList<Image>();
        String selectQuery = "SELECT * FROM " + IMAGES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Image image = new Image();
                image.setId(cursor.getInt(0));
                image.setDrug_id(cursor.getInt(1));
                image.setResource(cursor.getString(2));
                image.setPictureWidth(cursor.getInt(3));
                image.setPictureHeight(cursor.getInt(4));
                imageList.add(image);
            } while (cursor.moveToNext());
        }
        return imageList;
    }

    /**
     * Method gets and returns all images associated with a drug identified by the drug_id parameter
     *
     * @param drug_id
     * @return
     */
    public ArrayList<Image> getAllImages(int drug_id) {
        ArrayList<Image> imageList = new ArrayList<Image>();
        String selectQuery = "SELECT  * FROM " + IMAGES + " WHERE " + DRUG_ID + " = " + drug_id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.getCount() > 0 && cursor.moveToFirst()){

            do{
                Image image = new Image();
                image.setId(cursor.getInt(0));
                image.setDrug_id(cursor.getInt(1));
                image.setResource(cursor.getString(2));
                image.setPictureWidth(cursor.getInt(3));
                image.setPictureHeight(cursor.getInt(4));
                imageList.add(image);
            }while(cursor.moveToNext());
        }
        return imageList;
    }


    /**
     *  Method adds a row to the phone's table based on the information in the parameter's phone object
     *
     * @param phone the object that contains information for adding a row to the phones table
     * @return returns the id of the newly created table row in the phones table
     */
    public long addPhone(Phone phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PHONE_NAME, phone.getPhoneName());
        contentValues.put(PHONE_NUMBER, phone.getPhoneNumber());
        contentValues.put(PHONE_NOTE, phone.getPhoneNote());
        Long lastInsertId = db.insert(PHONES, null, contentValues);
        db.close();
        return lastInsertId;
    }

    /**
     * Method deletes a row in the phones table, identified by the phone_id parameter
     *
     * @param phone_id the row id where to delete the row in the phones table
     */
    public void deletePhone(long phone_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PHONES, ID + " = ?", new String[] { String.valueOf(phone_id) });
    }


    /**
     * Method updates a row in the phones table for a row that corresponds to the parameter's phone object
     *
     * @param phone the object for which to update a corresponding row in the phones table
     * @return
     */
    public int updatePhone(Phone phone) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PHONE_NAME, phone.getPhoneName());
        values.put(PHONE_NUMBER, phone.getPhoneNumber());
        values.put(PHONE_NOTE, phone.getPhoneNote());
        return db.update(PHONES, values, ID + " = ?", new String[]{String.valueOf(phone.getId())});
    }

    /**
     * Method creates and returns an arrayList of phone objects,
     * with each object based on the information in a row of the phones table
     *
     * @return returns an arrayList of image objects that correspond to the entries in the phones database table
     */
    public ArrayList<Phone> getAllPhones(){
        ArrayList<Phone> phoneList = new ArrayList<Phone>();
        String selectQuery = "SELECT * FROM " + PHONES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                Phone phone = new Phone();
                phone.setId(cursor.getInt(0));
                phone.setPhoneName(cursor.getString(1));
                phone.setPhoneNumber(cursor.getString(2));
                phone.setPhoneNote(cursor.getString(3));
                phoneList.add(phone);

            } while(cursor.moveToNext());
        }
        return  phoneList;
    }
}
