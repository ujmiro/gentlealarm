package DB.POCO;

import java.sql.Date;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Users")
public class User {
	@DatabaseField(generatedId = true, columnName="Id")
	private int id;
	@DatabaseField(canBeNull = false, columnName="Name")
	private String name;
	@DatabaseField(canBeNull = true, columnName = "CreatedAt")
	private Date createdAt;
	@DatabaseField(canBeNull = false, columnName = "PhaseAmount")
	private int phaseAmount;
	@ForeignCollectionField(eager = false)
	ForeignCollection<Setting> Settings;
	@ForeignCollectionField(eager = false)
	ForeignCollection<Alarm> Alarms;
	
	public User() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public int getPhaseAmount() {
		return phaseAmount;
	}

	public void setPhaseAmount(int phaseAmount) {
		this.phaseAmount = phaseAmount;
	}

	public ForeignCollection<Setting> getSettings() {
		return Settings;
	}

	public void setSettings(ForeignCollection<Setting> settings) {
		Settings = settings;
	}

	public ForeignCollection<Alarm> getAlarms() {
		return Alarms;
	}

	public void setAlarms(ForeignCollection<Alarm> alarms) {
		Alarms = alarms;
	}
}
