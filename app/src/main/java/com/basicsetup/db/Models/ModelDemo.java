package com.basicsetup.db.Models;


import com.basicsetup.db.DbModel;
import com.basicsetup.db.dao.DemoDao;

/**
 * Created by pankaj on 5/7/15.
 */
public class ModelDemo implements DbModel {

    private long _id;
    private String demoId;
    private String demoName;

    @Override
    public long getId() {
        return _id;
    }

    @Override
    public void setId(long id) {
        this._id = id;
    }

    @Override
    public String getTableName() {
        return DemoDao.TABLE_NAME;
    }

    @Override
    public String getCreateStatement() {
        return DemoDao.CREATE_TABLE;
    }

    @Override
    public String getPrimaryKey() {
        return demoId;
    }

    public String getDemoId() {
        return demoId;
    }

    public void setDemoId(String demoId) {
        this.demoId = demoId;
    }

    public String getDemoName() {
        return demoName;
    }

    public void setDemoName(String demoName) {
        this.demoName = demoName;
    }
}
