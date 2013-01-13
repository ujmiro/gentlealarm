package DB;

import DB.POCO.Alarm;
import DB.POCO.Setting;
import DB.POCO.User;
import java.sql.*;
import java.util.GregorianCalendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class OrmManager extends OrmLiteSqliteOpenHelper {

		private static final String DATABASE_NAME = "AlarmData.db";
		private static final int DATABASE_VERSION = 1;

		private Dao<User, Integer> userDao = null;
		private Dao<Alarm, Integer> alarmDao = null;
		private Dao<Setting, Integer> settingDao = null;
		
		public OrmManager(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
			try {
				Log.i(OrmManager.class.getName(), "onCreate");
				TableUtils.createTable(connectionSource, User.class);
				TableUtils.createTable(connectionSource, Alarm.class);
				TableUtils.createTable(connectionSource, Setting.class);
			} catch (SQLException e) {
				Log.e(OrmManager.class.getName(), "Can't create database", e);
				throw new RuntimeException(e);
			}
	
			try {
				User defaultUser = new User();
				defaultUser.setName("DefoultUSer");
				defaultUser.setPhaseAmount(3);
				getUserDao().create(defaultUser);
			
				for (int i = 1; i <= 4; ++i) {
					Setting setting = new Setting();
					setting.setUser(defaultUser);
					setting.setPhase(i - 1);
					setting.setTimeInSeconds(30);
					setting.setVolume(100 - (25 * (i - 1)));
					getSettingDao().create(setting);
				}
				
				Alarm alarm = new Alarm();
				alarm.setAlarmAt(new GregorianCalendar());
				alarm.setUser(defaultUser);
				alarm.setExecuted(true);
				getAlarmDao().create(alarm);
			} catch (SQLException e) {
				System.exit(-100);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
			try {
				Log.i(OrmManager.class.getName(), "onUpgrade");
				TableUtils.dropTable(connectionSource, User.class, true);
				onCreate(db, connectionSource);
			} catch (SQLException e) {
				Log.e(OrmManager.class.getName(), "Can't drop databases", e);
				throw new RuntimeException(e);
			}
		}
		
		public Dao<User, Integer> getUserDao() throws SQLException {
			if (userDao == null) {
				userDao = getDao(User.class);
			}
			return userDao;
		}
		
		public Dao<Alarm, Integer> getAlarmDao() throws SQLException {
			if (alarmDao == null) {
				alarmDao = getDao(Alarm.class);
			}
			return alarmDao;
		}
		
		public Dao<Setting, Integer> getSettingDao() throws SQLException {
			if (settingDao == null) {
				settingDao = getDao(Setting.class);
			}
			return settingDao;
		}

		@Override
		public void finalize(){
			close();
		}
		
		@Override
		public void close() {
			super.close();
			settingDao = null;
			alarmDao = null;
			userDao = null;
		}
	
}
