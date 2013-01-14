package DB.POCO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Schedules")
public class Schedule {
	@DatabaseField(generatedId = true, columnName = "Id")
	private int id;
	@DatabaseField(canBeNull = false, columnName = "AlarmAt")
	private long alarmAt;
	@DatabaseField(foreign = true, columnName = "AlarmId")
	private Alarm alarm;
	@DatabaseField(canBeNull = false, columnName = "Phase")
	private int phase;

	public Schedule(){}
	
	public Schedule(long alarmAt, int phase, Alarm alarm){
		this.alarmAt = alarmAt;
		this.phase = phase;
		this.alarm = alarm;
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

	public void setAlarmAt(long alarmAt) {
		this.alarmAt = alarmAt;
	}

	public Alarm getAlarm() {
		return alarm;
	}

	public void setAlarm(Alarm alarm) {
		this.alarm = alarm;
	}

	public int getPhase() {
		return phase;
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}
}
