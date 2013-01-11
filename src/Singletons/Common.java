package Singletons;

import java.util.Arrays;

import DB.POCO.Setting;
import DB.POCO.User;

public class Common {

	protected static Common common = new Common();
	private User user;
	private Setting[] settings = new Setting[4];
	
	
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
	}

	public Setting[] getSettings() {
		return settings;
	}
}
