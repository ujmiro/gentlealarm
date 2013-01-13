package Singletons;

import java.util.Arrays;

import DB.POCO.Alarm;
import DB.POCO.Setting;
import DB.POCO.User;

public class Common {

	protected static Common common = new Common();
	private User user;
	private Setting[] settings = new Setting[4];
	private Alarm[] alarms = new Alarm[1];
	
	
	private Common(){}
	
	public static Common getInstance(){
		return common;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		settings = user.getSettings().toArray(settings);
		Arrays.sort(settings);
		alarms = user.getAlarms().toArray(alarms);
	}

	public Setting[] getSettings() {
		return settings;
	}

	public Alarm[] getAlarms() {
		return alarms;
	}
}
