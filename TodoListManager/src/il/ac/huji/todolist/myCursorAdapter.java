package il.ac.huji.todolist;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class myCursorAdapter extends CursorAdapter {
	
	private final LayoutInflater inflater;
	TextView task;
	TextView date;
	
	public myCursorAdapter(Context context, Cursor c) {
		super(context, c,true);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		// TODO Auto-generated method stub
		task = (TextView) arg0.findViewById(R.id.txtTodoTitle);
		date = (TextView) arg0.findViewById(R.id.txtTodoDueDate);
		long dateLong = arg2.getLong(2);
		Calendar taskDate = new GregorianCalendar();
		taskDate.setTimeInMillis(dateLong);
		Calendar todayDate = new GregorianCalendar();
		
		task.setText(arg2.getString(1));
		date.setText(taskDate.get(GregorianCalendar.DAY_OF_MONTH)+"/"+
		(taskDate.get(GregorianCalendar.MONTH)+1)+"/"+
		taskDate.get(GregorianCalendar.YEAR));
		
		if(todayDate.compareTo(taskDate)>0){
			task.setTextColor(Color.RED);
			date.setTextColor(Color.RED);
		}
		else
		{
			task.setTextColor(Color.BLACK);
			date.setTextColor(Color.BLACK);
		}
		
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View row = (View) inflater.inflate(R.layout.list_row, arg2, false);
		return row;
	}

}
