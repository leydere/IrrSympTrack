package com.leydere.irrsymptrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DatabaseHelper class contains the various functions that interact directly with the database.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //irritant table
    public static final String TABLE_IRRITANTS = "IRRITANTS";
    public static final String COLUMN_IRR_ID = "IRR_ID";
    public static final String COLUMN_IRR_TITLE = "IRR_TITLE";
    public static final String COLUMN_IRR_TIMEDATE = "IRR_TIMEDATE";
    public static final String COLUMN_IRR_SEVERITY = "IRR_SEVERITY";

    //symptoms table
    public static final String TABLE_SYMPTOMS = "SYMPTOMS";
    public static final String COLUMN_SYM_ID = "SYM_ID";
    public static final String COLUMN_SYM_TITLE = "SYM_TITLE";
    public static final String COLUMN_SYM_TIMEDATE = "SYM_TIMEDATE";
    public static final String COLUMN_SYM_SEVERITY = "SYM_SEVERITY";
    public static final String COLUMN_SYM_IMAGE_PATH = "IMAGE_PATH";

    //irritant tags table
    public static final String TABLE_IRR_TAGS = "IRRITANT_TAGS";
    public static final String COLUMN_IRR_TAG_ID = "IRR_TAG_ID";
    public static final String COLUMN_IRR_TAG_TITLE = "IRR_TAG_TITLE";

    //symptom tags table
    public static final String TABLE_SYM_TAGS = "SYMPTOM_TAGS";
    public static final String COLUMN_SYM_TAG_ID = "SYM_TAG_ID";
    public static final String COLUMN_SYM_TAG_TITLE = "SYM_TAG_TITLE";

    //irritant-tags associative table
    public static final String TABLE_IRR_TAG_ASSOC = "IRRITANT_TAG_ASSOCIATIVE";
    public static final String COLUMN_A_IRR_ID = "A_IRR_ID";
    public static final String COLUMN_A_IRR_TAG_ID = "A_IRR_TAG_ID";
    public static final String PK_A_IRR_TAG = "PK_A_IRR_TAG";

    //symptoms-tags associative table
    public static final String TABLE_SYM_TAG_ASSOC = "SYMPTOM_TAG_ASSOCIATIVE";
    public static final String COLUMN_A_SYM_ID = "A_SYM_ID";
    public static final String COLUMN_A_SYM_TAG_ID = "A_SYM_TAG_ID";
    public static final String PK_A_SYM_TAG = "PK_A_SYM_TAG";




    public DatabaseHelper(@Nullable Context context) {
        super(context, "IrritantsSymptomsTracker.db", null, 1);
    }

    /**
     * OnCreate the database and its tables are created.  This only occurs if the DB has not been previously established.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement1 = "CREATE TABLE " + TABLE_IRRITANTS +
                " (" + COLUMN_IRR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IRR_TITLE + " TEXT, " +
                COLUMN_IRR_TIMEDATE + " TEXT, " +
                COLUMN_IRR_SEVERITY + " INTEGER)";

        String createTableStatement2 = "CREATE TABLE " + TABLE_SYMPTOMS +
                " (" + COLUMN_SYM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SYM_TITLE + " TEXT, " +
                COLUMN_SYM_TIMEDATE + " TEXT, " +
                COLUMN_SYM_SEVERITY + " INTEGER, " +
                COLUMN_SYM_IMAGE_PATH + " TEXT)";

        String createTableStatement3 = "CREATE TABLE " + TABLE_IRR_TAGS +
                " (" + COLUMN_IRR_TAG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IRR_TAG_TITLE + " TEXT)";

        String createTableStatement4 = "CREATE TABLE " + TABLE_SYM_TAGS +
                " (" + COLUMN_SYM_TAG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SYM_TAG_TITLE + " TEXT)";

        String createTableStatement5 = "CREATE TABLE " + TABLE_IRR_TAG_ASSOC +
                " (" + COLUMN_A_IRR_ID + " INTEGER, " +
                COLUMN_A_IRR_TAG_ID + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_A_IRR_ID + ") REFERENCES " + TABLE_IRRITANTS + "(" + COLUMN_IRR_ID + "), " +
                "FOREIGN KEY (" + COLUMN_A_IRR_TAG_ID + ") REFERENCES " + TABLE_IRR_TAGS + "(" + COLUMN_IRR_TAG_ID + "), " +
                "CONSTRAINT " + PK_A_IRR_TAG + " PRIMARY KEY (" + COLUMN_A_IRR_ID + ", " + COLUMN_A_IRR_TAG_ID + "))";

        String createTableStatement6 = "CREATE TABLE " + TABLE_SYM_TAG_ASSOC +
                " (" + COLUMN_A_SYM_ID + " INTEGER, " +
                COLUMN_A_SYM_TAG_ID + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_A_SYM_ID + ") REFERENCES " + TABLE_SYMPTOMS + "(" + COLUMN_SYM_ID + "), " +
                "FOREIGN KEY (" + COLUMN_A_SYM_TAG_ID + ") REFERENCES " + TABLE_SYM_TAGS + "(" + COLUMN_SYM_TAG_ID + "), " +
                "CONSTRAINT " + PK_A_SYM_TAG + " PRIMARY KEY (" + COLUMN_A_SYM_ID + ", " + COLUMN_A_SYM_TAG_ID + "))";

        //create default symptom tag data
        String createDefaultSymptomTagDataQuery =  "INSERT INTO " + TABLE_SYM_TAGS + "(" + COLUMN_SYM_TAG_TITLE + ") " +
                        "VALUES ('Itchy'), ('Swelling'), ('Dry'), ('Nausea'), ('Rash'), ('Sneezing'), ('Congested');";
        //create default irritant tag data
        String createDefaultIrritantTagDataQuery =  "INSERT INTO " + TABLE_IRR_TAGS + "(" + COLUMN_IRR_TAG_TITLE + ") " +
                        "VALUES ('Shellfish'), ('Peanuts'), ('Dairy'), ('Tree Nuts'), ('Eggs'), ('Wheat'), ('Fish'), ('Soy');";

        db.execSQL(createTableStatement1);
        db.execSQL(createTableStatement2);
        db.execSQL(createTableStatement3);
        db.execSQL(createTableStatement4);
        db.execSQL(createTableStatement5);
        db.execSQL(createTableStatement6);
        db.execSQL(createDefaultSymptomTagDataQuery);
        db.execSQL(createDefaultIrritantTagDataQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //region ADD RECORDS

    /**
     * Inserts new irritant record to DB.  Returns -1 if record was not successfully added, otherwise returns ID of newly added record.
     * Returned ID necessary to create associative tag data for new records. Utilized in ActivityAddIrritant.java.
     * @param modelIrritant
     * @return
     */
    public int addIrritantRecord(ModelIrritant modelIrritant){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_IRR_TITLE, modelIrritant.getIrrTitle());
        contentValues.put(COLUMN_IRR_TIMEDATE, modelIrritant.getIrrTimeDate());
        contentValues.put(COLUMN_IRR_SEVERITY, modelIrritant.getIrrSeverity());

        long insert = db.insert(TABLE_IRRITANTS, null, contentValues);
        // get primary key of newly added record - return instead of original boolean value
        Cursor returnedCursor = db.rawQuery("SELECT LAST_INSERT_ROWID();", null);
        int returnedID = -1;
        if (returnedCursor != null){
            try {
                if (returnedCursor.moveToFirst()) {
                    returnedID = returnedCursor.getInt(0);
                }
            } finally {
                returnedCursor.close();
            }
        }
        //end attempt
        db.close();
        return returnedID;
    }

    /**
     * Inserts new symptom record to DB.  Returns -1 if record was not successfully added, otherwise returns ID of newly added record.
     * Returned ID necessary to create associative tag data for new records. Utilized in ActivityAddSymptom.java.
     * @param modelSymptom
     * @return
     */
    public int addSymptomRecord(ModelSymptom modelSymptom){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_SYM_TITLE, modelSymptom.getSymTitle());
        contentValues.put(COLUMN_SYM_TIMEDATE, modelSymptom.getSymTimeDate());
        contentValues.put(COLUMN_SYM_SEVERITY, modelSymptom.getSymSeverity());
        contentValues.put(COLUMN_SYM_IMAGE_PATH, modelSymptom.getSymImagePath());

        long insert = db.insert(TABLE_SYMPTOMS, null, contentValues);
        // get primary key of newly added record - return instead of original boolean value
        Cursor returnedCursor = db.rawQuery("SELECT LAST_INSERT_ROWID();", null);
        int returnedID = -1;
        if (returnedCursor != null){
            try {
                if (returnedCursor.moveToFirst()) {
                    returnedID = returnedCursor.getInt(0);
                }
            } finally {
                returnedCursor.close();
            }
        }
        //end attempt
        db.close();
        return returnedID;
    }

    /**
     * Inserts new irritant tag record to DB.  Returns false if record was not successfully added, otherwise returns true.
     * Utilized in ActivityNewIrritantTags.java.
     * @param modelIrritantTag
     * @return
     */
    public boolean addIrritantTagRecord(ModelIrritantTag modelIrritantTag){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_IRR_TAG_TITLE, modelIrritantTag.getIrrTagTitle());

        long insert = db.insert(TABLE_IRR_TAGS, null, contentValues);
        db.close();
        if (insert == -1) { return false; } else { return true; }
    }

    /**
     * Inserts new symptom tag record to DB.  Returns false if record was not successfully added, otherwise returns true.
     * Utilized in ActivityNewSymptomTags.java.
     * @param modelSymptomTag
     * @return
     */
    public boolean addSymptomTagRecord(ModelSymptomTag modelSymptomTag){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_SYM_TAG_TITLE, modelSymptomTag.getSymTagTitle());

        long insert = db.insert(TABLE_SYM_TAGS, null, contentValues);
        db.close();
        if (insert == -1) { return false; } else { return true; }
    }
    //endregion

    //region GET ALL RECORDS
    /**
     * Gets all irritant records from DB and returns in the form of a list of ModelIrritants.
     * Utilized in ActivityRecordsListIrritants.java to populate the recyclerview.
     * @return
     */
    public List<ModelIrritant> getAllIrritants() {
        List<ModelIrritant> compiledResults = new ArrayList();
        String queryString = "SELECT * FROM " + TABLE_IRRITANTS + " ORDER BY " + COLUMN_IRR_TIMEDATE + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do {
                int irrId = cursor.getInt(0);
                String irrTitle = cursor.getString(1);
                String irrDateTime = cursor.getString(2);
                int irrSeverity = cursor.getInt(3);

                ModelIrritant newIrritant = new ModelIrritant(irrId, irrTitle, irrDateTime, irrSeverity);
                compiledResults.add(newIrritant);
            } while (cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();

        return compiledResults;
    }

    /**
     * Gets all symptom records from DB and returns in the form of a list of ModelSymptoms.
     * Utilized in ActivityRecordsListSymptoms.java to populate the recyclerview.
     * @return
     */
    public List<ModelSymptom> getAllSymptoms() {
        List<ModelSymptom> compiledResults = new ArrayList();
        String queryString = "SELECT * FROM " + TABLE_SYMPTOMS + " ORDER BY " + COLUMN_SYM_TIMEDATE + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do {
                int symId = cursor.getInt(0);
                String symTitle = cursor.getString(1);
                String symDateTime = cursor.getString(2);
                int symSeverity = cursor.getInt(3);

                ModelSymptom newSymptom = new ModelSymptom(symId, symTitle, symDateTime, symSeverity, null);
                compiledResults.add(newSymptom);
            } while (cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();

        return compiledResults;
    }
    //endregion

    /**
     * Gets a single symptom record from the DB based on a requested symptom record ID. Returns in the form of ModelSymptom.
     * Utilized in ActivityAddSymptom.java edit record variant to populate the various fields with record data of the record to edit.
     * @param id
     * @return
     */
    public ModelSymptom getSingleSymptomRecord(int id){
        ModelSymptom newSymptom = new ModelSymptom(-1, null, null, -1, null);
        String queryString = "SELECT * FROM " + TABLE_SYMPTOMS + " WHERE " + COLUMN_SYM_ID + "=" + id + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            String symTitle = cursor.getString(1);
            String symDateTime = cursor.getString(2);
            int symSeverity = cursor.getInt(3);

            newSymptom = new ModelSymptom(id, symTitle, symDateTime, symSeverity, null);

        } else  {
            //
        }
        cursor.close();
        db.close();
        return newSymptom;
    }

    /**
     * Updates DB existing symptom record based on the known record ID. Returns false if record was not successfully added, otherwise returns true.
     * Utilized in ActivityAddSymptom.java edit record variant.
     * @param modelSymptom
     * @return
     */
    public boolean updateExistingSymptomRecord(ModelSymptom modelSymptom){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_SYM_ID, modelSymptom.getId());
        contentValues.put(COLUMN_SYM_TITLE, modelSymptom.getSymTitle());
        contentValues.put(COLUMN_SYM_TIMEDATE, modelSymptom.getSymTimeDate());
        contentValues.put(COLUMN_SYM_SEVERITY, modelSymptom.getSymSeverity());
        contentValues.put(COLUMN_SYM_IMAGE_PATH, modelSymptom.getSymImagePath());

        long insert = db.update(TABLE_SYMPTOMS, contentValues, COLUMN_SYM_ID + " = ?", new String[] {String.valueOf(modelSymptom.getId())});
        db.close();
        if (insert == -1) { return false; } else { return true; }
    }

    /**
     * Gets a single irritant record from the DB based on a requested irritant record ID. Returns in the form of ModelIrritant.
     * Utilized in ActivityAddIrritant.java edit record variant to populate the various fields with record data of the record to edit.
     * @param id
     * @return
     */
    public ModelIrritant getSingleIrritantRecord(int id){
        ModelIrritant newIrritant = new ModelIrritant(-1, null, null, -1);
        String queryString = "SELECT * FROM " + TABLE_IRRITANTS + " WHERE " + COLUMN_IRR_ID + "=" + id + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            String irrTitle = cursor.getString(1);
            String irrDateTime = cursor.getString(2);
            int irrSeverity = cursor.getInt(3);

            newIrritant = new ModelIrritant(id, irrTitle, irrDateTime, irrSeverity);

        } else  {
            //
        }
        cursor.close();
        db.close();
        return newIrritant;
    }

    /**
     * Updates DB existing irritant record based on the known record ID. Returns false if record was not successfully added, otherwise returns true.
     * Utilized in ActivityAddIrritant.java edit record variant.
     * @param modelIrritant
     * @return
     */
    public boolean updateExistingIrritantRecord(ModelIrritant modelIrritant){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_IRR_ID, modelIrritant.getId());
        contentValues.put(COLUMN_IRR_TITLE, modelIrritant.getIrrTitle());
        contentValues.put(COLUMN_IRR_TIMEDATE, modelIrritant.getIrrTimeDate());
        contentValues.put(COLUMN_IRR_SEVERITY, modelIrritant.getIrrSeverity());

        long insert = db.update(TABLE_IRRITANTS, contentValues, COLUMN_IRR_ID + " = ?", new String[] {String.valueOf(modelIrritant.getId())});
        db.close();
        if (insert == -1) { return false; } else { return true; }
    }

    /**
     * Gets all symptom records that meet the criteria of associated tag ID and date range. Returns in the form of list of ModelDataPoints which are severity sums grouped by date.
     * Utilized in ActivityGraphView.java to populate the graph.
     * @param tagId
     * @param startDate
     * @param endDate
     * @return
     */
    public ArrayList<ModelDataPoint> getSelectedSymptomsByDateRangeAndTagId(int tagId, String startDate, String endDate) {
        ArrayList<ModelDataPoint> compiledResults = new ArrayList();
        String queryString = "SELECT SUM(" + COLUMN_SYM_SEVERITY + ") AS \"SEVERITYSUM\", strftime('%Y-%m-%d', " + COLUMN_SYM_TIMEDATE + ") AS \"SHORTTIME\" FROM " + TABLE_SYMPTOMS +
                " INNER JOIN " + TABLE_SYM_TAG_ASSOC +
                " ON " + TABLE_SYMPTOMS + "." + COLUMN_SYM_ID + " = " + TABLE_SYM_TAG_ASSOC + "." + COLUMN_A_SYM_ID +
                " INNER JOIN " + TABLE_SYM_TAGS +
                " ON " + TABLE_SYM_TAG_ASSOC + "." + COLUMN_A_SYM_TAG_ID + " = " + TABLE_SYM_TAGS + "." + COLUMN_SYM_TAG_ID +
                " WHERE " + COLUMN_SYM_TAG_ID + " = " + tagId +
                " AND " + COLUMN_SYM_TIMEDATE + " BETWEEN \'" + startDate + "\' AND \'" + endDate +
                "\' GROUP BY SHORTTIME" +
                " ORDER BY SHORTTIME;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do {
                int severity = cursor.getInt(0);
                String date = cursor.getString(1) + " 12:00:00.000";

                ModelDataPoint newDataPoint = new ModelDataPoint(severity, date);
                compiledResults.add(newDataPoint);
            } while (cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();

        return compiledResults;
    }

    /**
     * Gets all irritants records that meet the criteria of associated tag ID and date range. Returns in the form of list of ModelDataPoints which are severity sums grouped by date.
     * Utilized in ActivityGraphView.java to populate the graph.
     * @param tagId
     * @param startDate
     * @param endDate
     * @return
     */
    public ArrayList<ModelDataPoint> getSelectedIrritantsByDateRangeAndTagId(int tagId, String startDate, String endDate) {
        ArrayList<ModelDataPoint> compiledResults = new ArrayList();
        String queryString = "SELECT SUM(" + COLUMN_IRR_SEVERITY + ") AS \"SEVERITYSUM\", strftime('%Y-%m-%d', " + COLUMN_IRR_TIMEDATE + ") AS \"SHORTTIME\" FROM " + TABLE_IRRITANTS +
                " INNER JOIN " + TABLE_IRR_TAG_ASSOC +
                " ON " + TABLE_IRRITANTS + "." + COLUMN_IRR_ID + " = " + TABLE_IRR_TAG_ASSOC + "." + COLUMN_A_IRR_ID +
                " INNER JOIN " + TABLE_IRR_TAGS +
                " ON " + TABLE_IRR_TAG_ASSOC + "." + COLUMN_A_IRR_TAG_ID + " = " + TABLE_IRR_TAGS + "." + COLUMN_IRR_TAG_ID +
                " WHERE " + COLUMN_IRR_TAG_ID + " = " + tagId +
                " AND " + COLUMN_IRR_TIMEDATE + " BETWEEN \'" + startDate + "\' AND \'" + endDate +
                "\' GROUP BY SHORTTIME" +
                " ORDER BY SHORTTIME;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do {
                int severity = cursor.getInt(0);
                String date = cursor.getString(1) + " 12:00:00.000";

                ModelDataPoint newDataPoint = new ModelDataPoint(severity, date);
                compiledResults.add(newDataPoint);
            } while (cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();

        return compiledResults;
    }

    /**
     * Creates irritant tag associative data records.  Previous records are deleted from DB to avoid duplicate records.
     * Returns true if record is successfully created, otherwise returns false.
     * Utilized in ActivityAddIrritant.java immediately following the adding or editing of an irritant record.
     * @param recordID
     * @param selectedIrritantTagIDsList
     * @return
     */
    public boolean createIrritantTagAssociativeRecord(int recordID, ArrayList<Integer> selectedIrritantTagIDsList){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            //remove existing db associative records; probably redundant for new records but possible fragments could remain from database manipulation
            String deleteQuery = "DELETE FROM " + TABLE_IRR_TAG_ASSOC +
                    " WHERE " + COLUMN_A_IRR_ID + " = " + recordID + ";";
            db.execSQL(deleteQuery);
        } catch (Exception e) {
            //nothing?
        }
        try{
            for (int irrID : selectedIrritantTagIDsList){
                    String insertQuery =  "INSERT INTO " + TABLE_IRR_TAG_ASSOC + "(" + COLUMN_A_IRR_ID + ", " + COLUMN_A_IRR_TAG_ID + ") " +
                            "VALUES (" + recordID + ", " + irrID + ");";
                    db.execSQL(insertQuery);
            }
        } catch (Exception e) {
            db.close();
            return false;
        }
        db.close();
        return true;
    }

    /**
     * Creates symptom tag associative data records.  Previous records are deleted from DB to avoid duplicate records.
     * Returns true if record is successfully created, otherwise returns false.
     * Utilized in ActivityAddSymptom.java immediately following the adding or editing of a symptom record.
     * @param recordID
     * @param selectedSymptomTagIDsList
     * @return
     */
    public boolean createSymptomTagAssociativeRecord(int recordID, ArrayList<Integer> selectedSymptomTagIDsList){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            //remove existing db associative records; probably redundant for new records but possible fragments could remain from database manipulation
            String deleteQuery = "DELETE FROM " + TABLE_SYM_TAG_ASSOC +
                    " WHERE " + COLUMN_A_SYM_ID + " = " + recordID + ";";
            db.execSQL(deleteQuery);
        } catch (Exception e) {
            //nothing?
        }
        try{
            for (int symID : selectedSymptomTagIDsList){
                String insertQuery =  "INSERT INTO " + TABLE_SYM_TAG_ASSOC + "(" + COLUMN_A_SYM_ID + ", " + COLUMN_A_SYM_TAG_ID + ") " +
                        "VALUES (" + recordID + ", " + symID + ");";
                db.execSQL(insertQuery);
            }
        } catch (Exception e) {
            db.close();
            return false;
        }
        db.close();
        return true;
    }

    //region Supporting Tag Recycler Views
    /**
     * Gets all irritant tag records from DB. Returns in the form of list of ModelIrritantTags.
     * Utilized to populate recycler views in ActivityAddIrritant.java, ActivityGraphView.java, and ActivityNewIrritantTags.java.
     * @return
     */
    public List<ModelIrritantTag> getAllIrritantTags() {
        List<ModelIrritantTag> compiledResults = new ArrayList();
        String queryString = "SELECT * FROM " + TABLE_IRR_TAGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do {
                int irrTagId = cursor.getInt(0);
                String irrTagTitle = cursor.getString(1);

                ModelIrritantTag newIrritantTag = new ModelIrritantTag(irrTagId, irrTagTitle);
                compiledResults.add(newIrritantTag);
            } while (cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();

        return compiledResults;
    }

    /**
     * Gets all symptom tag records from DB. Returns in the form of list of ModelSymptomTags.
     * Utilized to populate recycler views in ActivityAddSymptom.java, ActivityGraphView.java, and ActivityNewSymptomTags.java.
     * @return
     */
    public List<ModelSymptomTag> getAllSymptomTags() {
        List<ModelSymptomTag> compiledResults = new ArrayList();
        String queryString = "SELECT * FROM " + TABLE_SYM_TAGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do {
                int symTagId = cursor.getInt(0);
                String symTagTitle = cursor.getString(1);

                ModelSymptomTag newSymptomTag = new ModelSymptomTag(symTagId, symTagTitle);
                compiledResults.add(newSymptomTag);
            } while (cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();

        return compiledResults;
    }

    /**
     * Gets all tag IDs associated to an irritant record based on irritant record ID.  Returns in the form of array list of ints.
     * Utilized in ActivityAddIrritant.java to ensure the proper tags in the recycler view are toggled as selected when an existing record is accessed for editing.
     * @param idOfExistingIrritantRecord
     * @return
     */
    public ArrayList<Integer> getTagIDsAssociatedToThisIrritantRecord(int idOfExistingIrritantRecord) {
        ArrayList<Integer> compiledResults = new ArrayList();
        String queryString = "SELECT " + COLUMN_IRR_TAG_ID + " FROM " + TABLE_IRR_TAGS +
                " INNER JOIN " + TABLE_IRR_TAG_ASSOC +
                " ON " + TABLE_IRR_TAGS + "." + COLUMN_IRR_TAG_ID + " = " + TABLE_IRR_TAG_ASSOC + "." + COLUMN_A_IRR_TAG_ID +
                " INNER JOIN " + TABLE_IRRITANTS +
                " ON " + TABLE_IRR_TAG_ASSOC + "." + COLUMN_A_IRR_ID + " = " + TABLE_IRRITANTS + "." + COLUMN_IRR_ID +
                " WHERE " + COLUMN_IRR_ID + " = " + idOfExistingIrritantRecord +  ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do {
                int foundIrrTagId = cursor.getInt(0);

                compiledResults.add(foundIrrTagId);
            } while (cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();

        return compiledResults;
    }

    /**
     * Gets all tag IDs associated to an symptom record based on symptom record ID.  Returns in the form of array list of ints.
     * Utilized in ActivityAddSymptom.java to ensure the proper tags in the recycler view are toggled as selected when an existing record is accessed for editing.
     * @param idOfExistingSymptomRecord
     * @return
     */
    public ArrayList<Integer> getTagIDsAssociatedToThisSymptomRecord(int idOfExistingSymptomRecord) {
        ArrayList<Integer> compiledResults = new ArrayList();
        String queryString = "SELECT " + COLUMN_SYM_TAG_ID + " FROM " + TABLE_SYM_TAGS +
                " INNER JOIN " + TABLE_SYM_TAG_ASSOC +
                " ON " + TABLE_SYM_TAGS + "." + COLUMN_SYM_TAG_ID + " = " + TABLE_SYM_TAG_ASSOC + "." + COLUMN_A_SYM_TAG_ID +
                " INNER JOIN " + TABLE_SYMPTOMS +
                " ON " + TABLE_SYM_TAG_ASSOC + "." + COLUMN_A_SYM_ID + " = " + TABLE_SYMPTOMS + "." + COLUMN_SYM_ID +
                " WHERE " + COLUMN_SYM_ID + " = " + idOfExistingSymptomRecord +  ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do {
                int foundSymTagId = cursor.getInt(0);

                compiledResults.add(foundSymTagId);
            } while (cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();

        return compiledResults;
    }

    //endregion
}

