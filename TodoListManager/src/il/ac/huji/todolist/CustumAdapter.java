package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustumAdapter extends BaseAdapter {

	private final ArrayList<String[]> objects;
	private final LayoutInflater inflater;
	public CustumAdapter(Context context, int resource, ArrayList<String[]> objects) {
		inflater = LayoutInflater.from(context);
		this.objects = objects;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		if (row == null)
		{
			row = (View) inflater.inflate(R.layout.list_row, parent, false);
		}
		TextView todo = (TextView) row.findViewById(R.id.txtTodoTitle);
		TextView date = (TextView) row.findViewById(R.id.txtTodoDueDate);
		todo.setText(objects.get(position)[0]);
		date.setText(objects.get(position)[1]);
		String [] splittedDate = objects.get(position)[1].split("/");
		Calendar dateGreg = new GregorianCalendar(Integer.parseInt(splittedDate[2]),
				Integer.parseInt(splittedDate[1])-1,Integer.parseInt(splittedDate[0])+1);	
		Calendar todayDate = new GregorianCalendar();
		if(todayDate.compareTo(dateGreg)>0){
			todo.setTextColor(Color.RED);
			date.setTextColor(Color.RED);
		}
		else
		{
			todo.setTextColor(Color.BLACK);
			date.setTextColor(Color.BLACK);
		}
		
		return row;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return objects.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return objects.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
}
