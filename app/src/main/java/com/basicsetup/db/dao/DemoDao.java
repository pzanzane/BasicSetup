package com.basicsetup.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.basicsetup.db.Models.ModelDemo;


/**
 * Created by pankaj on 5/7/15.
 */
public class DemoDao extends BaseDAO<ModelDemo> {


    public static String COLLEGE_ID="college_id";
    public static String COLLEGE_NAME="college_name";

    public static String TABLE_NAME = "demo";

    public static String CREATE_TABLE="CREATE TABLE IF NOT EXISTS "+TABLE_NAME + " ("
                                        +ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                                        + COLLEGE_ID+" TEXT,"
                                        + COLLEGE_NAME+" TEXT"+");";

    /**
     * Instantiates a new base dao.
     *
     * @param context the context
     * @param db
     */
    public DemoDao(Context context, SQLiteDatabase db) {
        super(context, db);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getPrimaryColumnName() {
        return COLLEGE_ID;
    }

    @Override
    public ModelDemo fromCursor(Cursor c) {

        ModelDemo modelDemo = new ModelDemo();
        modelDemo.setId(c.getLong(c.getColumnIndex(ID)));
        modelDemo.setDemoId(c.getString(c.getColumnIndex(COLLEGE_ID)));
        modelDemo.setDemoName(c.getString(c.getColumnIndex(COLLEGE_NAME)));

        return modelDemo;
    }

    @Override
    public ContentValues values(ModelDemo modelDemo) {

        ContentValues values = new ContentValues();
        values.put(COLLEGE_ID, modelDemo.getDemoId());
        values.put(COLLEGE_NAME, modelDemo.getDemoName());

        return values;
    }
}
