package DB;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import mobi.ii.SetAlarmActivity;
import mobi.ii.WakeUpReceiver;
import DB.POCO.Alarm;
import DB.POCO.Schedule;
import Singletons.Common;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class AlarmScheduler {
	
	private OrmLiteBaseActivity<OrmManager> context;
	private AlarmManager alarmManager;
	private PendingIntent pendingIntent;
	
	public AlarmScheduler(OrmLiteBaseActivity<OrmManager> ctx){
		context = ctx;
		alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	}
	
	public void scheduleAlarms(GregorianCalendar alarmAt){
		Alarm alarm = new Alarm(alarmAt, Common.getInstance().getUser());
		try {
			context.getHelper().getAlarmDao().create(alarm);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i <= Common.getInstance().getUser().getPhaseAmount(); ++i){
			try {
				context.getHelper().getScheduleDao().create(new Schedule(alarmAt.getTimeInMillis(), i, alarm));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (alarmAt.getTimeInMillis() - SetAlarmActivity.BREAK_TIME * 60 * 1000 < new GregorianCalendar().getTimeInMillis())
				break;
			alarmAt.add(Calendar.MINUTE, - SetAlarmActivity.BREAK_TIME);
		}
	}
	
	public void broadCastAlarm(){
		Schedule schedule = null;
		try {
			schedule = context.getHelper().getScheduleDao().queryBuilder().orderBy("AlarmAt", true).queryForFirst();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		if (schedule == null)
			return;
		
		Intent intent = new Intent(context, WakeUpReceiver.class);
	    intent.putExtra("scheduleId", schedule.getId());
	    
		pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		alarmManager.set(AlarmManager.RTC_WAKEUP, schedule.getAlarmAt(), pendingIntent);
	}
	
	public void DeleteSchedule(Schedule schedule){
		try {
			context.getHelper().getScheduleDao().delete(schedule);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void finishAlarm(Alarm alarm){
		try {
			List<Schedule> schedules = context.getHelper().getScheduleDao().queryBuilder().where().eq("AlarmId", alarm.getId()).query();
			for (Schedule schedule : schedules)
				context.getHelper().getScheduleDao().delete(schedule);
			context.getHelper().getAlarmDao().delete(alarm);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		broadCastAlarm();
	}
}
