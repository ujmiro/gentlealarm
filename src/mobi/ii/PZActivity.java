package mobi.ii;

import java.util.List;

import DB.OrmManager;
import DB.POCO.User;
import android.os.Bundle;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class PZActivity extends OrmLiteBaseActivity<OrmManager> {
		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
     // get our dao
    // 	RuntimeExceptionDao<User, Integer> simpleDao = getHelper().getUserDao();
     	// query for all of the data objects in the database
     //	List<User> list = simpleDao.queryForAll();
     //	list.get(0);
    }
}