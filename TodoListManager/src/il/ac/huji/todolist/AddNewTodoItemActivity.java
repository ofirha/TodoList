package il.ac.huji.todolist;

import java.util.GregorianCalendar;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddNewTodoItemActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_todo_item);
		Button ok = (Button) findViewById(R.id.btnOK);
		Button cancel = (Button) findViewById(R.id.btnCancel);
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				EditText task = (EditText) findViewById(R.id.edtNewItem);
				String taskString = task.getText().toString();
				if(taskString == null || taskString.isEmpty())
				{
					return;
				}
				DatePicker date = (DatePicker) findViewById(R.id.datePicker);
				GregorianCalendar calendar = new GregorianCalendar(date.getYear(), date.getMonth(), date.getDayOfMonth());
				intent.putExtra("title",taskString);
				intent.putExtra("dueDate",calendar.getTimeInMillis());
				setResult(RESULT_OK, intent);
				finish();
				
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_new_todo_item, menu);
		return true;
	}

}
