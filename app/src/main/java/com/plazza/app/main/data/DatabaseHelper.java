package com.plazza.app.main.data;

import java.io.IOException;
import java.sql.SQLException;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.plazza.app.main.model.Section;
import com.plazza.app.main.model.Setting;


/**
 * Database helper class used to manage the creation and upgrading of your
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	// name of the database file for your application -- change to something
	// appropriate for your app
	private static final String DATABASE_NAME = "section.db";
	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 2;

	// the DAO object
	private Dao<Section, Integer> sectionDao = null;
	private Dao<Setting, Integer> settingDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		DatabaseInitializer initializer = new DatabaseInitializer(context);
		try {

			initializer.createDatabase();
			initializer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This is called when the database is first created. Usually you should
	 * call createTable statements here to create the tables that will store
	 * your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {

	}

	/**
	 * This is called when your application is upgraded and it has a higher
	 * version number. This allows you to adjust the various data to match the
	 * new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {

		onCreate(db, connectionSource);
	}

	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It
	 * will create it or just give the cached value.
	 */
	public Dao<Section, Integer> getSectionDao() throws SQLException {
		if (sectionDao == null) {
			sectionDao = getDao(Section.class);
		}
		return sectionDao;
	}

	public Dao<Setting, Integer> getSettingDao() throws SQLException {
		if (settingDao == null) {
			settingDao = getDao(Setting.class);
		}
		return settingDao;
	}

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		sectionDao = null;
	}
}
