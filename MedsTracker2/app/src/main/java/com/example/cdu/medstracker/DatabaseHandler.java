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

    private static final String BOOKS = "books";  //table name
    private static final String TITLE = "title";  //column name for books table
    private static final String DESCRIPTION = "description"; //column name for books table
    private static final String URL = "url"; //column name for books table


    private static final String IMAGES = "images";  //table name
    private static final String BOOK_ID = "book_id";  //column name for images table
    private static final String IMAGE_PATH = "image_path";  //column name for images table


    private static final String CREATE_BOOKS_TABLE = "CREATE TABLE IF NOT EXISTS " +  BOOKS + " (" +
                                                    ID + " INTEGER PRIMARY KEY, " +
                                                    TITLE + " TEXT, " +
                                                    DESCRIPTION + " TEXT, " +
                                                     URL + " TEXT)";



    private static final String CREATE_IMAGES_TABLE = "CREATE TABLE IF NOT EXISTS " + IMAGES + " (" +
            ID + " INTEGER PRIMARY KEY, " +
            BOOK_ID + " INTEGER REFERENCES " + BOOKS + "("  + ID + "), " +
            IMAGE_PATH + " TEXT)";


    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_BOOKS_TABLE);
        db.execSQL(CREATE_IMAGES_TABLE);

    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion ){
        db.execSQL("DROP TABLE IF EXISTS " + BOOKS);
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

    public void addBook(Drug book){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(ID, 0);
        values.put(TITLE, book.getTitle());
        values.put(DESCRIPTION, book.getDescription());
        values.put(URL, book.getUrl());
        db.insert(BOOKS, null, values);
    }


    /**
     * READ OPERATIONS
     */

    public Drug getBook(int id){
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
        Cursor cursor = db.query(BOOKS,
                new String[] {ID, TITLE, DESCRIPTION, URL},
                "=?", new String[]{String.valueOf(id)}, null, null, null);
        if(cursor != null)
            cursor.moveToFirst();
        /**
         * We create a location object using the cursor record
         */
        Drug book = new Drug(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        return book;
    }

    public ArrayList<Drug> getAllBooks(){
        ArrayList<Drug> bookList = new ArrayList<Drug>();
        String selectQuery = "SELECT * FROM " + BOOKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                Drug book = new Drug();
                book.setId(cursor.getInt(0));
                book.setTitle(cursor.getString(1));
                book.setDescription(cursor.getString(2));
                book.setUrl(cursor.getString(3));
                bookList.add(book);

            } while(cursor.moveToNext());
        }
        return  bookList;
    }


    /**
     * UPDATE OPERATIONS
     */

    public int updateBook(Drug book){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, book.getTitle());
        values.put(DESCRIPTION, book.getDescription());
        values.put(URL, book.getUrl());
        return db.update(BOOKS, values, ID + " = ?",
                new String[]{String.valueOf(book.getId())});
    }
    /**
     * DELETE OPERATIONS
     */

    public void deleteBook(long book_id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(BOOKS, ID + " = ?", new String[]{String.valueOf(book_id)});
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
        contentValues.put(BOOK_ID, image.getBook_id());
        contentValues.put(IMAGE_PATH, image.getResource());
        Long lastInsertId = db.insert(IMAGES, null, contentValues);
        db.close();
        return lastInsertId;
    }

    public void deleteImage(long book_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(IMAGES, BOOK_ID + " = ?", new String[] { String.valueOf(book_id) });
    }

    public int updateImage(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOK_ID, image.getBook_id());
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
                image.setBook_id(cursor.getInt(1));
                image.setResource(cursor.getString(2));
                imageList.add(image);
            } while (cursor.moveToNext());
        }
        return imageList;
    }

    /**
     * The second getAllImages is used to grab all images associated with a book
     * @param book_id
     * @return
     */
    public ArrayList<Image> getAllImages(int book_id) {
        ArrayList<Image> imageList = new ArrayList<Image>();
        String selectQuery = "SELECT  * FROM " + IMAGES + " WHERE " + BOOK_ID + " = " + book_id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.getCount() > 0 && cursor.moveToFirst()){

            do{
                Image image = new Image();
                image.setId(cursor.getInt(0));
                image.setBook_id(cursor.getInt(1));
                image.setResource(cursor.getString(2));
                imageList.add(image);
            }while(cursor.moveToNext());
        }
        return imageList;
    }




}
