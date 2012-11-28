package DB;

import DB.POCO.Alarm;
import DB.POCO.User;
import java.sql.*;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class OrmManager extends OrmLiteSqliteOpenHelper {

		// name of the database file for your application -- change to something appropriate for your app
		private static final String DATABASE_NAME = "AlarmData.db";
		// any time you make changes to your database objects, you may have to increase the database version
		private static final int DATABASE_VERSION = 1;

		private Dao<User, Integer> userDao = null;
		private RuntimeExceptionDao<User, Integer> userRuntimeDao = null;

		public OrmManager(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		/**
		 * This is called when the database is first created. Usually you should call createTable statements here to create
		 * the tables that will store your data.
		 */
		@Override
		public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
			try {
				Log.i(OrmManager.class.getName(), "onCreate");
				TableUtils.createTable(connectionSource, User.class);
				TableUtils.createTable(connectionSource, Alarm.class);
			} catch (SQLException e) {
				Log.e(OrmManager.class.getName(), "Can't create database", e);
				throw new RuntimeException(e);
			}

			// here we try inserting data in the on-create as a test
			RuntimeExceptionDao<User, Integer> dao = getUserDao();
			// create some entries in the onCreate
			User simple = new User();
			simple.setName("Ala2");
			dao.create(simple);
		}

		/**
		 * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
		 * the various data to match the new version number.
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
			try {
				Log.i(OrmManager.class.getName(), "onUpgrade");
				TableUtils.dropTable(connectionSource, User.class, true);
				// after we drop the old databases, we create the new ones
				onCreate(db, connectionSource);
			} catch (SQLException e) {
				Log.e(OrmManager.class.getName(), "Can't drop databases", e);
				throw new RuntimeException(e);
			}
		}

		/**
		 * Returns the Database Access Object (DAO) for our User class. It will create it or just give the cached
		 * value.
		 * @throws java.sql.SQLException 
		 */
		public Dao<User, Integer> getDao() throws SQLException {
			if (userDao == null) {
				userDao = getDao(User.class);
			}
			return userDao;
		}

		/**
		 * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our User class. It will
		 * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
		 */
		public RuntimeExceptionDao<User, Integer> getUserDao() {
			if (userRuntimeDao == null) {
				userRuntimeDao = getRuntimeExceptionDao(User.class);
			}
			return userRuntimeDao;
		}

		@Override
		public void finalize(){
			close();
		}
		
		/**
		 * Close the database connections and clear any cached DAOs.
		 */
		@Override
		public void close() {
			super.close();
//			simpleRuntimeDao = null;
		}
	
}
