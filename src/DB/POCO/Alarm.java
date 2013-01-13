package DB.POCO;

import java.util.GregorianCalendar;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Alarms")
public class Alarm {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(canBeNull = false)
	private long alarmAt;
	@DatabaseField(foreign = true)
	private User user;
	@DatabaseField(canBeNull = false, columnName="Executed")
	private boolean executed;
	
	public boolean isExecuted() {
		return executed;
	}
	public void setExecuted(boolean executed) {
		this.executed = executed;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getAlarmAt() {
		return alarmAt;
	}
	public void setAlarmAt(GregorianCalendar alarmAt) {
		this.alarmAt = alarmAt.getTimeInMillis();
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
