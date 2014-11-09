package com.example.toyos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/*
 * This activity could have been implemented using the
 * Dialog class. As it is, this activity behaves much
 * like a dialog since it uses the Theme.Holo.Dialog theme.
 */

public class ArgumentActivity extends Activity {

	EditText const_addr;
	EditText comment;
	String opcode;
	String reg0, reg1;
	Spinner spinner0, spinner1;
	ArrayAdapter<CharSequence> adapter0;
	ArrayAdapter<CharSequence> adapter1;
	TextView text;
	int dialog_type;
	
	/**
	 * OnItemSelected listeners for register spinners.
	 */
	private AdapterView.OnItemSelectedListener listener0 = new AdapterView.OnItemSelectedListener(){
		
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
			reg0 = new String(Integer.toString(pos));				
		}
		
		public void onNothingSelected(AdapterView<?> parent){} // nothing to do here
	};

	/**
	 * Second OnItemSelected listener. Not always used.
	 */
	private AdapterView.OnItemSelectedListener listener1 = new AdapterView.OnItemSelectedListener(){
		
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
			reg1 = new String(Integer.toString(pos));				
		}
		
		public void onNothingSelected(AdapterView<?> parent){} // nothing to do here
	};
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent prev = getIntent();
		if(prev.hasExtra(MainActivity.OPCODE))
			opcode = new String(prev.getStringExtra(MainActivity.OPCODE));
		
		// Determines which layout to use for the dialog box
		if(prev.hasExtra(MainActivity.DIALOG_TYPE))
			dialog_type = prev.getIntExtra(MainActivity.DIALOG_TYPE, MainActivity.COMMENT);
		

		switch(dialog_type){
		case MainActivity.COMMENT:
			
			setContentView(R.layout.argument_comment);
			break;
				
		case MainActivity.SINGLE_VALUE:
			
			setContentView(R.layout.argument_single_value);
			text = (TextView) findViewById(R.id.opcode_text);
			text.setText(opcode);
				
			const_addr = (EditText)findViewById(R.id.const_value);
			break;
				
		case MainActivity.SINGLE_REG:
			
			setContentView(R.layout.argument_one_register);
			text = (TextView) findViewById(R.id.opcode_text);
			text.setText(opcode);
			
			spinner0 = (Spinner) findViewById(R.id.first_reg_spinner);
			adapter0 = ArrayAdapter.createFromResource(this,
					R.array.registers_array,R.layout.my_spinner_item);
			adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner0.setAdapter(adapter0);
			spinner0.setOnItemSelectedListener(listener0);
			break;
			
		case MainActivity.REG_AND_VALUE:
			
			setContentView(R.layout.argument_register_value);
			text = (TextView) findViewById(R.id.opcode_text);
			text.setText(opcode);
				
			const_addr = (EditText)findViewById(R.id.const_value);
				
			spinner0 = (Spinner) findViewById(R.id.first_reg_spinner);
			adapter0 = ArrayAdapter.createFromResource(this,
					R.array.registers_array,R.layout.my_spinner_item);
			adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner0.setAdapter(adapter0);
			spinner0.setOnItemSelectedListener(listener0);
			break;
			
		case MainActivity.REG_AND_REG:
			
			setContentView(R.layout.argument_two_registers);
			text = (TextView) findViewById(R.id.opcode_text);
			text.setText(opcode);
				
			spinner0 = (Spinner) findViewById(R.id.first_reg_spinner);
			adapter0 = ArrayAdapter.createFromResource(this,
					R.array.registers_array,R.layout.my_spinner_item);
			adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner0.setAdapter(adapter0);
			spinner0.setOnItemSelectedListener(listener0);
			
				
			spinner1 = (Spinner) findViewById(R.id.second_reg_spinner);		
			adapter1 = ArrayAdapter.createFromResource(this,
					R.array.registers_array,R.layout.my_spinner_item);
			adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner1.setAdapter(adapter1);
			spinner1.setOnItemSelectedListener(listener1);
			    
		}
		
		comment = (EditText) findViewById(R.id.comment_edittext);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.argument, menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.argument, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Returns a fully constructed instruction to the parent activity (GridActivity).
	 * It's easier than having this inside finish() since finish() is called
	 * by other events as well.
	 * @param instr
	 */
	public void finishWithResult(String instr){
		Intent result = new Intent();
		result.putExtra(MainActivity.INSTRUCTION, instr);
		setResult(MainActivity.RESULT_OK, result);
		finish();
	}
	
	
	/**	
	 * Runs whenever the OK or Cancel buttons in the dialog are pressed.
	 * Builds a fully built instruction and calls finisWithResult();
	 * @param v
	 */
	public void onClickEvent(View v){

		switch(v.getId()){
		case R.id.ok_button:
			
			String instr = opcode;
			if(reg0 != null)
				instr = instr + "  " + reg0;
			if(reg1 != null)
				instr = instr + "  " + reg1;
			if(const_addr != null)
				if(const_addr.getText().toString().isEmpty()){
					Toast.makeText(getApplicationContext(), "You didn't enter a constant/address",
							Toast.LENGTH_SHORT).show();
					return;
				}else{
					instr = instr + "  " + const_addr.getText().toString();
				}
			
			// Technically, comment should never be null.
			// It doesn't hurt to check though.
			if(comment != null)
				if(!comment.getText().toString().isEmpty())
					if(dialog_type == MainActivity.COMMENT){
						instr = instr + " " + "<font color=#008800>" + 
								comment.getText().toString() + "</font>"; 
					}else{
						String h = "<font color=#008800>" +
								comment.getText().toString()
								+ "</font>";
						instr = instr + "  ! " + h;
					}
			
			finishWithResult(instr);
			break;
			
		case R.id.cancel_button:
			finish();
		}
	}
	
}
