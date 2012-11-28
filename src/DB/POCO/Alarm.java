package DB.POCO;

import java.sql.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Alarms")
public class Alarm {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(canBeNull = false)
	private Date alarmAt;
	@DatabaseField(foreign = true)
	private User user;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getAlarmAt() {
		return alarmAt;
	}
	public void setAlarmAt(Date alarmAt) {
		this.alarmAt = alarmAt;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
