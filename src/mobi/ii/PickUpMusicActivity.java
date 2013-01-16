package mobi.ii;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import DB.OrmManager;
import Singletons.Common;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class PickUpMusicActivity extends OrmLiteBaseActivity<OrmManager> {
	
	private ListView soundsList;
	private Cursor soundcursor;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pick_up_music);
		createMusicList();
    }
	 
	private void createMusicList(){
		String[] obtain = { MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME };
		soundcursor = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, obtain, null, null, null);
		soundsList = (ListView) findViewById(R.id.MusicList);
		soundsList.setAdapter(new SoundAdapter());
		
		
		soundsList.setOnItemClickListener(new OnItemClickListener() {
	          
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				if (Common.getInstance().getUser().getSongName() != null){
					getApplicationContext().deleteFile(Common.getInstance().getUser().getSongName());
					Common.getInstance().getUser().setSongName(null);
				}
				
				soundcursor.moveToPosition(position);
				String filename = soundcursor.getString(soundcursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
				File homeDir = new ContextWrapper(getApplicationContext()).getFilesDir();
				
				FileInputStream input = null;
				FileOutputStream output = null;
				try{
					input = new FileInputStream(filename);
					String dir = homeDir.getAbsolutePath() + "/" + soundcursor.getString(soundcursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
					output = new FileOutputStream(dir);
					int read = 0;
					byte[] buffer = new byte[4096];
					while (-1 != (read = input.read(buffer))) {
						output.write(buffer, 0, read);
					}
					Common.getInstance().getUser().setSongName(soundcursor.getString(soundcursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
				}catch(Exception e){
				} finally {
					try {
						if (input != null)
							input.close();
						if (output != null)
							output.close();
					} catch (IOException e) {}
				}
				try {
					getHelper().getUserDao().update(Common.getInstance().getUser());
				} catch (SQLException e) {
					e.printStackTrace();
				}
	          }
	    });
	}
	
	public class SoundAdapter extends BaseAdapter {

		public int getCount() {
			return soundcursor.getCount();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView != null)
				return (TextView) convertView;

			TextView tv = new TextView(getApplicationContext());
			tv.setTextSize(20);
			soundcursor.moveToPosition(position);
			tv.setText(soundcursor.getString(soundcursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
			return tv;
		}
	}

}
