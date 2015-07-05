package com.basicsetup.db;

import android.content.Context;

import com.basicsetup.db.Models.ModelDemo;

import java.util.ArrayList;
import java.util.List;

public class DbConfiguration implements IDbConfiguration {

	private static boolean DB_IN_SDCARD=true;
	private String databaseName;
	private String databasePath;
	private List<DbModel> models;
	private int version;

	public static DbConfiguration getInstance(Context context) {

		List<DbModel> list = new ArrayList<DbModel>();
		list.add(new ModelDemo());
	 

		DbConfiguration config = new DbConfiguration();
		config.setDatabaseName("databasename.db");

		if ((DB_IN_SDCARD) && (context != null && context.getExternalFilesDir(null) != null))
			config.setDatabasePath(context.getExternalFilesDir(null)
					.getAbsolutePath());
		else
			config.setDatabasePath(null);

		config.setDatabaseVersion(1);
		config.setModels(list);

		return config;
	}

	@Override
	public String getDatabaseName() {
		return databaseName;
	}

	@Override
	public String getDatabasePath() {
		return databasePath;
	}

	@Override
	public List<DbModel> getModels() {
		return models;
	}

	@Override
	public int getDatabaseVersion() {
		return version;
	}

	@Override
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;

	}

	@Override
	public void setDatabasePath(String databasePath) {
		this.databasePath = databasePath;

	}

	@Override
	public void setModels(List<DbModel> models) {
		this.models = models;

	}

	@Override
	public void setDatabaseVersion(int version) {
		this.version = version;

	}

}
