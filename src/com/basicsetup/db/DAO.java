package com.basicsetup.db;
import android.content.ContentValues;
import android.database.Cursor;

public interface DAO<T> {
    
    public static final String ID = "_id";
    
    public T findByPrimaryKey(String id);
    public void create(T object);
    public void update(T object);
    public void createOrUpdate(T object);
    public void delete(String id);
    public boolean exists(String id);
    public String getPrimaryColumnName();
    
    public T fromCursor(Cursor c);
    public ContentValues values(T t);
}