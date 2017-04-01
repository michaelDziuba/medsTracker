package com.example.cdu.medstracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by web on 2017-02-07.
 */

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


    private static final String CREATE_DRUGS_TABLE = "CREATE TABLE IF NOT EXISTS " +  DRUGS + " (" +
                                                    ID + " INTEGER PRIMARY KEY, " +
                                                    DRUG_NAME + " TEXT, " +
                                                    DRUG_DOSE + " TEXT, " +
                                                    WHEN_TO_TAKE + " TEXT, " +
                                                    NOTES + " TEXT)";



    private static final String CREATE_IMAGES_TABLE = "CREATE TABLE IF NOT EXISTS " + IMAGES + " (" +
            ID + " INTEGER PRIMARY KEY, " +
            DRUG_ID + " INTEGER REFERENCES " + DRUGS + "("  + ID + "), " +
            IMAGE_PATH + " TEXT)";


    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_DRUGS_TABLE);
        db.execSQL(CREATE_IMAGES_TABLE);

    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion ){
        db.execSQL("DROP TABLE IF EXISTS " + DRUGS);
        db.execSQL("DROP TABLE IF EXISTS " + IMAGES);
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
     * CREATE OPERATIONS
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
     * READ OPERATIONS
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
     * UPDATE OPERATIONS
     */

    public int updateDrug(Drug drug){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DRUG_NAME, drug.getDrugName());
        values.put(DRUG_DOSE, drug.getDrugDose());
        values.put(WHEN_TO_TAKE, drug.getWhenToTake());
        values.put(NOTES, drug.getNotes());
        return db.update(DRUGS, values, ID + " = ?",
                new String[]{String.valueOf(drug.getId())});
    }
    /**
     * DELETE OPERATIONS
     */

    public void deleteDrug(long drug_id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DRUGS, ID + " = ?", new String[]{String.valueOf(drug_id)});
    }

    /**
     * Closing the database connection
     */
    public void closeDB(){
        SQLiteDatabase db =this.getReadableDatabase();
        if(db != null && db.isOpen()){
            db.close();
        }
    }


    public boolean isTable(String tableName){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SHOW TABLES LIKE " + tableName;
        Cursor cursor = db.rawQuery(sql, null);

        int numberOfTables = cursor.getCount();

        return numberOfTables == 0 ? false : true;
    }



    //
    public long addImage(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
       // contentValues.put(ID, 0);
        contentValues.put(DRUG_ID, image.getDrug_id());
        contentValues.put(IMAGE_PATH, image.getResource());
        Long lastInsertId = db.insert(IMAGES, null, contentValues);
        db.close();
        return lastInsertId;
    }

    public void deleteImage(long drug_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(IMAGES, DRUG_ID + " = ?", new String[] { String.valueOf(drug_id) });
    }

    public int updateImage(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DRUG_ID, image.getDrug_id());
        contentValues.put(IMAGE_PATH, image.getResource());
        return db.update(IMAGES, contentValues, ID + " = ?", new String[] { String.valueOf(image.getId()) });
    }


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
                imageList.add(image);
            } while (cursor.moveToNext());
        }
        return imageList;
    }

    /**
     * The second getAllImages is used to grab all images associated with a drug
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
                imageList.add(image);
            }while(cursor.moveToNext());
        }
        return imageList;
    }




}
