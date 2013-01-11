package DB.POCO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Settings")
public class Setting implements Comparable<Setting> {

	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(canBeNull = false, columnName = "Phase")
	private int phase;
	@DatabaseField(canBeNull = false, columnName = "Volume")
	private int volume;
	@DatabaseField(canBeNull = false, columnName = "TimeInSeconds")
	private int timeInSeconds;
	@DatabaseField(foreign = true, columnName = "UserId")
	private User user;

	public int compareTo(Setting setting) {
		if (phase == setting.phase)
			return 0;
		else 
            return phase < setting.phase ? -1 : 1;
	}
	
	public void setTimeInSeconds(String value){
		int number = Integer.valueOf(value.substring(0, value.indexOf(" ")));
		if (value.contains("min")){
			timeInSeconds = number * 60;
		}else{
			timeInSeconds = number;
		}
	}
	
	public String getTimeInSecondsAsString(){
		if (timeInSeconds < 60)
			return timeInSeconds + " s";
		return timeInSeconds / 60 + " min";
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPhase() {
		return phase;
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public int getTimeInSeconds() {
		return timeInSeconds;
	}

	public void setTimeInSeconds(int timeInSeconds) {
		this.timeInSeconds = timeInSeconds;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
