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
import java.util.List;

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

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement1 = "CREATE TABLE " + TABLE_IRRITANTS +
                " (" + COLUMN_IRR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IRR_TITLE + " TEXT, " +
                COLUMN_IRR_TIMEDATE + " TEXT, " +
                COLUMN_IRR_SEVERITY + " TEXT)";

        String createTableStatement2 = "CREATE TABLE " + TABLE_SYMPTOMS +
                " (" + COLUMN_SYM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SYM_TITLE + " TEXT, " +
                COLUMN_SYM_TIMEDATE + " TEXT, " +
                COLUMN_SYM_SEVERITY + " TEXT, " +
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

        db.execSQL(createTableStatement1);
        db.execSQL(createTableStatement2);
        db.execSQL(createTableStatement3);
        db.execSQL(createTableStatement4);
        db.execSQL(createTableStatement5);
        db.execSQL(createTableStatement6);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //region ADD RECORDS
    public boolean addIrritantRecord(ModelIrritant modelIrritant){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_IRR_TITLE, modelIrritant.getIrrTitle());
        contentValues.put(COLUMN_IRR_TIMEDATE, modelIrritant.getIrrTimeDate());
        contentValues.put(COLUMN_IRR_SEVERITY, modelIrritant.getIrrSeverity());

        long insert = db.insert(TABLE_IRRITANTS, null, contentValues);
        db.close();
        if (insert == -1) { return false; } else { return true; }
    }

    public boolean addSymptomRecord(ModelSymptom modelSymptom){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_SYM_TITLE, modelSymptom.getSymTitle());
        contentValues.put(COLUMN_SYM_TIMEDATE, modelSymptom.getSymTimeDate());
        contentValues.put(COLUMN_SYM_SEVERITY, modelSymptom.getSymSeverity());
        contentValues.put(COLUMN_SYM_IMAGE_PATH, modelSymptom.getSymImagePath());

        long insert = db.insert(TABLE_SYMPTOMS, null, contentValues);
        db.close();
        if (insert == -1) { return false; } else { return true; }
    }
    //endregion

    //region GET ALL RECORDS
    // gets all irritant records, to be used in populating the fragment 1 recycler view
    public List<ModelIrritant> getAllIrritants() {
        List<ModelIrritant> compiledResults = new ArrayList();
        String queryString = "SELECT * FROM " + TABLE_IRRITANTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do {
                int irrId = cursor.getInt(0);
                String irrTitle = cursor.getString(1);
                String irrDateTime = cursor.getString(2);
                String irrSeverity = cursor.getString(3);

                ModelIrritant newIrritant = new ModelIrritant(irrId, irrTitle, irrDateTime, irrSeverity);
                compiledResults.add(newIrritant);
            } while (cursor.moveToNext());
        } else {
            // TODO tbd
        }

        cursor.close();
        db.close();

        return compiledResults;
    }

    // gets all symptoms records, to be used in populating the fragment 2 recycler view
    public List<ModelSymptom> getAllSymptoms() {
        List<ModelSymptom> compiledResults = new ArrayList();
        String queryString = "SELECT * FROM " + TABLE_SYMPTOMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do {
                int symId = cursor.getInt(0);
                String symTitle = cursor.getString(1);
                String symDateTime = cursor.getString(2);
                String symSeverity = cursor.getString(3);

                //TODO image path null will need changed upon addition of camera feature
                ModelSymptom newSymptom = new ModelSymptom(symId, symTitle, symDateTime, symSeverity, null);
                compiledResults.add(newSymptom);
            } while (cursor.moveToNext());
        } else {
            // TODO tbd
        }

        cursor.close();
        db.close();

        return compiledResults;
    }
    //endregion

    public ModelSymptom getSingleSymptomRecord(int id){
        ModelSymptom newSymptom = new ModelSymptom(id, null, null, null, null);
        String queryString = "SELECT * FROM " + TABLE_SYMPTOMS + " WHERE " + COLUMN_SYM_ID + "=" + id + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            String symTitle = cursor.getString(1);
            String symDateTime = cursor.getString(2);
            String symSeverity = cursor.getString(3);

            //TODO image path null will need changed upon addition of camera feature
            newSymptom = new ModelSymptom(id, symTitle, symDateTime, symSeverity, null);

        } else  {
            //
        }
        cursor.close();
        db.close();
        return newSymptom;
    }

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

    public ModelIrritant getSingleIrritantRecord(int id){
        ModelIrritant newIrritant = new ModelIrritant(id, null, null, null);
        String queryString = "SELECT * FROM " + TABLE_IRRITANTS + " WHERE " + COLUMN_IRR_ID + "=" + id + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            String irrTitle = cursor.getString(1);
            String irrDateTime = cursor.getString(2);
            String irrSeverity = cursor.getString(3);

            newIrritant = new ModelIrritant(id, irrTitle, irrDateTime, irrSeverity);

        } else  {
            //
        }
        cursor.close();
        db.close();
        return newIrritant;
    }

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

    //returns list of symptom models that meet the tag association requirement - list is used to populate graph
    public List<ModelSymptom> getSelectedSymptoms(int tagId) {
        List<ModelSymptom> compiledResults = new ArrayList();
        //String queryString = "SELECT " + TABLE_SYMPTOMS + "." + COLUMN_SYM_TITLE + ", " + TABLE_SYM_TAGS + "." + COLUMN_SYM_TAG_TITLE +
        String queryString = "SELECT * FROM " + TABLE_SYMPTOMS +
        " INNER JOIN " + TABLE_SYM_TAG_ASSOC +
        " ON " + TABLE_SYMPTOMS + "." + COLUMN_SYM_ID + " = " + TABLE_SYM_TAG_ASSOC + "." + COLUMN_A_SYM_ID +
        " INNER JOIN " + TABLE_SYM_TAGS +
        " ON " + TABLE_SYM_TAG_ASSOC + "." + COLUMN_A_SYM_TAG_ID + " = " + TABLE_SYM_TAGS + "." + COLUMN_SYM_TAG_ID +
        " WHERE " + COLUMN_SYM_TAG_ID + " = " + tagId + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do {
                int symId = cursor.getInt(0);
                String symTitle = cursor.getString(1);
                String symDateTime = cursor.getString(2);
                String symSeverity = cursor.getString(3);

                //TODO image path null will need changed upon addition of camera feature
                ModelSymptom newSymptom = new ModelSymptom(symId, symTitle, symDateTime, symSeverity, null);
                compiledResults.add(newSymptom);
            } while (cursor.moveToNext());
        } else {
            // TODO tbd
        }

        cursor.close();
        db.close();

        return compiledResults;
    }

    //returns list of irritant models that meet the tag association requirement - list is used to populate graph
    public List<ModelIrritant> getSelectedIrritants(int tagId) {
        List<ModelIrritant> compiledResults = new ArrayList();
        String queryString = "SELECT * FROM " + TABLE_IRRITANTS +
                " INNER JOIN " + TABLE_IRR_TAG_ASSOC +
                " ON " + TABLE_IRRITANTS + "." + COLUMN_IRR_ID + " = " + TABLE_IRR_TAG_ASSOC + "." + COLUMN_A_IRR_ID +
                " INNER JOIN " + TABLE_IRR_TAGS +
                " ON " + TABLE_IRR_TAG_ASSOC + "." + COLUMN_A_IRR_TAG_ID + " = " + TABLE_IRR_TAGS + "." + COLUMN_IRR_TAG_ID +
                " WHERE " + COLUMN_IRR_TAG_ID + " = " + tagId + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do {
                int irrId = cursor.getInt(0);
                String irrTitle = cursor.getString(1);
                String irrDateTime = cursor.getString(2);
                String irrSeverity = cursor.getString(3);

                ModelIrritant newIrritant = new ModelIrritant(irrId, irrTitle, irrDateTime, irrSeverity);
                compiledResults.add(newIrritant);
            } while (cursor.moveToNext());
        } else {
            // TODO tbd
        }

        cursor.close();
        db.close();

        return compiledResults;
    }

    //region Dummy data
    //create dummy symptom tag associative data
    public boolean createDummySymptomAssociativeData(){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            //value pairs go (sym ID. tag ID)
            String insertQuery =  "INSERT INTO " + TABLE_SYM_TAG_ASSOC + "(" + COLUMN_A_SYM_ID + ", " + COLUMN_A_SYM_TAG_ID + ") " +
                    "VALUES (1, 1), (2, 1), (3, 1), (4, 1), (2, 2), (4, 3), (5, 3), (6, 3);";
            db.execSQL(insertQuery);
            db.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    //create dummy symptom tag data
    public boolean createDummySymptomTagData(){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            String insertQuery =  "INSERT INTO " + TABLE_SYM_TAGS + "(" + COLUMN_SYM_TAG_TITLE + ") " +
                    "VALUES ('Itchy'), ('Swelling'), ('Dry');";
            db.execSQL(insertQuery);
            db.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    //create dummy irritant tag associative data
    public boolean createDummyIrritantAssociativeData(){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            //value pairs go (sym ID. tag ID)
            String insertQuery =  "INSERT INTO " + TABLE_IRR_TAG_ASSOC + "(" + COLUMN_A_IRR_ID + ", " + COLUMN_A_IRR_TAG_ID + ") " +
                    "VALUES (1, 1), (2, 2), (3, 3), (4, 3);";
            db.execSQL(insertQuery);
            db.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    //create dummy irritant tag data
    public boolean createDummyIrritantTagData(){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            String insertQuery =  "INSERT INTO " + TABLE_IRR_TAGS + "(" + COLUMN_IRR_TAG_TITLE + ") " +
                    "VALUES ('Eggs'), ('Dairy'), ('Citrus');";
            db.execSQL(insertQuery);
            db.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    //endregion
}

