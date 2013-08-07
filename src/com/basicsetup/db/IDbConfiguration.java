package com.basicsetup.db;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class DbConfiguration.
 */
interface IDbConfiguration{
   
	String getDatabaseName();
  
	String getDatabasePath();
  
	List<DbModel> getModels();
  
	int getDatabaseVersion();
	
	IDbConfiguration getInstance();
	
}
