package com.arjinmc.ormlitedemo.utils;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.arjinmc.ormlitedemo.model.PersonBean;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * @desciption database connection util
 * @author Eminem Lu
 * @email arjinmc@hicsg.com
 * @create 2015/2/26
 */
public class DBOpenHeleper extends OrmLiteSqliteOpenHelper{
	
	//for upgrade database test
	//remember to add the verson
	private final static int DB_VERSION = 1;
	private final static String DB_NAME = "demo.db";
	
	private RuntimeExceptionDao<PersonBean, Integer> runPerson = null;
	
	public DBOpenHeleper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	public DBOpenHeleper(Context context, String databaseName,
			CursorFactory factory, int databaseVersion) {
		super(context, DB_NAME, factory, DB_VERSION);
	}
	
	
	
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		 try {
			TableUtils.createTableIfNotExists(connectionSource, PersonBean.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVerson,
			int newVerson) {
		RuntimeExceptionDao<PersonBean, Integer> simpleDao = getSimplePersonDao();
		simpleDao.executeRawNoArgs("ALTER TABLE `person` ADD COLUMN age INTEGER DEFAULT 22;");
		
	}
	
	/**
	 * @desciption get Dao for person
	 * @author Eminem Lu
	 * @email arjinmc@hicsg.com
	 * @create 2015/2/26
	 * @return
	 */
	public RuntimeExceptionDao<PersonBean, Integer> getSimplePersonDao() {
		if (runPerson == null) {
			runPerson = getRuntimeExceptionDao(PersonBean.class);
		}
		return runPerson;
	}
	
}
