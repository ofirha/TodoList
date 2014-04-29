package il.ac.huji.todolist;

import com.parse.Parse;
import com.parse.ParseUser;
import android.app.Application;

public class parserApplication extends Application{
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//Parse.initialize(this, "wVDbH2EHaDhrQcWKi5eoxzZb2r9RyVpbEf90ycFq", "fTzsJZdfFq4KJaFF0ktLkv4gcQvHRSmyxGdL4O7w");
		//ParseUser.enableAutomaticUser();
	}
}
