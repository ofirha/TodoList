package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class TodoListManagerActivity extends Activity {
	ArrayList<ParseObject> myList;
	myCursorAdapter adapter;
	ListView listFromView;
	DBHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		listFromView = (ListView) findViewById(R.id.lstTodoItems);
		myList = new ArrayList<ParseObject>();
		ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("todo");
		parseQuery.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				// TODO Auto-generated method stub
				if (arg1==null)
				{
					// sync list
					for(ParseObject object:arg0)
					{
						myList.add(object);
					}
				}
			}
			
		});
		
		helper = new DBHelper(getApplicationContext());
		SQLiteDatabase todo_dbRead = helper.getReadableDatabase();
		Cursor curs = todo_dbRead.rawQuery("SELECT * FROM "+DBHelper.table_name, null);
		DBHelper.minIdAvailable = curs.getCount()+1;
		adapter = new myCursorAdapter(getApplicationContext(), curs);
		listFromView.setAdapter(adapter);
		registerForContextMenu(listFromView);
		todo_dbRead.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menuItemAdd:
			Intent intent = new Intent(getApplicationContext(),
					AddNewTodoItemActivity.class);
			startActivityForResult(intent, 0);

		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			String task = data.getStringExtra("title");
			long date = data.getLongExtra("dueDate", 0);
			SQLiteDatabase todo_dbWrite = helper.getWritableDatabase();
			ParseObject parseObj = new ParseObject("todo");
			ContentValues todoTask = new ContentValues();

			todoTask.put("title", task);
			todoTask.put("due", date);
			todoTask.put("_id", DBHelper.minIdAvailable);
			DBHelper.minIdAvailable++;
			parseObj.put("title", task);
			parseObj.put("due", date);
			parseObj.saveInBackground();
			myList.add(parseObj);
			
			todo_dbWrite.insert(DBHelper.table_name, null ,todoTask);	
			SQLiteDatabase todo_dbRead = helper.getReadableDatabase();
			adapter.changeCursor(todo_dbRead.rawQuery("SELECT * FROM "+DBHelper.table_name, null));
			todo_dbWrite.close();
			todo_dbRead.close();
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflator = getMenuInflater();
		inflator.inflate(R.menu.item_menu, menu);
		if (v.getId() == R.id.lstTodoItems)
		{
			AdapterView.AdapterContextMenuInfo infoOnMenu = (AdapterView.AdapterContextMenuInfo) menuInfo;
			SQLiteDatabase todo_dbRead = helper.getReadableDatabase();
			Cursor curs = todo_dbRead.rawQuery("Select * from "+ DBHelper.table_name+
					" Where _id="+Integer.toString(infoOnMenu.position+1), null);
			curs.moveToFirst();
			String task = curs.getString(1).trim();
			curs.close();
			todo_dbRead.close();
			menu.setHeaderTitle(task);
			String[] splittedTask = task.split("\\s+");
			if (splittedTask[0].toLowerCase().equals("call") && splittedTask.length>1) {
				menu.getItem(1).setVisible(true).setEnabled(true).setTitle(task);
			}
			else
			{
				menu.getItem(1).setVisible(false).setEnabled(false);
			}
		}
		
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		SQLiteDatabase todo_dbRead = helper.getReadableDatabase();
		switch (item.getItemId()) {
		
		case R.id.menuItemDelete:
			SQLiteDatabase todo_dbWrite = helper.getWritableDatabase();
			todo_dbWrite.delete(DBHelper.table_name, "_id="+Integer.toString(info.position+1), null);
			ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("todo");
			parseQuery.whereEqualTo("objectId",myList.get(info.position).getObjectId().trim());
			parseQuery.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> arg0, ParseException arg1) {

					try {

						arg0.get(0).delete();

					} catch (ParseException e1) {

						e1.printStackTrace();
					}
				}
			});

			myList.remove(info.position);
			DBHelper.minIdAvailable--;
			todo_dbWrite.execSQL("update "+DBHelper.table_name+" Set _id=_id-1 where _id>"+Integer.toString(info.position+1));
			adapter.changeCursor(todo_dbRead.rawQuery("SELECT * FROM "+DBHelper.table_name, null));
			todo_dbRead.close();
			todo_dbWrite.close();
			break;
		
		case R.id.menuItemCall:
			Cursor curs = todo_dbRead.rawQuery("Select * from "+ DBHelper.table_name+" Where _id="+Integer.toString(info.position+1), null);
			curs.moveToFirst();
			String task = curs.getString(1).trim();
			String [] splittedTask = task.split("\\s+");
			Intent call = new Intent(Intent.ACTION_DIAL,
					Uri.parse("tel:"+splittedTask[1]));
			startActivity(call);
			todo_dbRead.close();
			curs.close();
			break;
		}
		return true;
	}

}
