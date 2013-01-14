package DB.POCO;

import java.util.GregorianCalendar;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Alarms")
public class Alarm {
	@DatabaseField(generatedId = true, columnName = "Id")
	private int id;
	@DatabaseField(canBeNull = false, columnName = "AlarmAt")
	private long alarmAt;
	@DatabaseField(foreign = true, columnName = "UserId")
	private User user;
	@DatabaseField(canBeNull = false, columnName="Executed")
	private boolean executed;
	@ForeignCollectionField(eager = true)
	ForeignCollection<Schedule> Schedules;
	
	public Alarm(){}
	
	public Alarm(GregorianCalendar alarmAt, User user) {
		this.alarmAt = alarmAt.getTimeInMillis();
		this.user = user;
		this.executed = false;
	}

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
	public ForeignCollection<Schedule> getSchedules() {
		return Schedules;
	}
	public void setSchedules(ForeignCollection<Schedule> schedules) {
		Schedules = schedules;
	}
}
