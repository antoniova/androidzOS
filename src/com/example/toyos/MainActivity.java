package com.example.toyos;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements SaveDialogFragment.NoticeDialogListener {

	static final String STATE_ADAPTER = "adapter_array";
	static final String DIALOG_TYPE = "dialog_key";
	static final String INSTRUCTION = "instruction_key";
	static final String OPCODE = "returnkey1";
	
	// dialog type (magic numbers)
	static final int COMMENT	 	= 10;
	static final int SINGLE_VALUE 	= 11;
	static final int SINGLE_REG 	= 12;
	static final int REG_AND_VALUE 	= 13;
	static final int REG_AND_REG 	= 14;
	// request and result codes
	static final int NEW_INSTRUCTION= 55;
	static final int EDIT_INSTRUCTION = 56;
	static final int GET_ARGUMENTS 	= 57;
	static final int RESULT_OK 		= 58;
	
	static final short OPCODE_MASK =  (short)0xF800;
		
	private int candidate = 0;
	private ActionMode mActionMode;
	private Assembler assembler;
	CustomArrayAdapter<String> adapter;
	ArrayList<String> textBuffer;	// The current text file shown on the screen.
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ListView listview = (ListView) findViewById(R.id.listview);
        textBuffer = new ArrayList<String>();
        
        if(savedInstanceState != null)
        	textBuffer = savedInstanceState.getStringArrayList(STATE_ADAPTER);
        
        adapter = new CustomArrayAdapter<String>(this,
        		android.R.layout.simple_list_item_1, textBuffer);
        
        listview.setAdapter(adapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //listview.setOnItemClickListener(clickListener);  
        listview.setOnItemLongClickListener(longClickListener);  
                
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main, menu);
    	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
        	//case R.id.action_addline:
        		//launchGridActivity();
        		//break;
        	case R.id.action_assemble:
    			assembleFile();
    			break;
        	case R.id.action_save_text_file:
        		showSaveDialog();
        		break;
        	case R.id.action_open_text_file:
        		showOpenDialog();
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save text file buffer shown on the screen
        savedInstanceState.putStringArrayList(STATE_ADAPTER, textBuffer);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
    
        
    /**
     * Receives the assembly instruction string from GridActivity
     * and inserts it into the text file buffer. 
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
    	if(intent != null){
    		if(requestCode == NEW_INSTRUCTION && resultCode == RESULT_OK){
    			if(intent.hasExtra(INSTRUCTION))
    				adapter.add(intent.getStringExtra(INSTRUCTION) );
    		}
    		//TODO
    		//if(requestCode == EDIT_INSTRUCTION && resultCode == RESULT_OK){}
    	}
    	
    }		
    
    /**
     * Called when the "add new line" button is clicked.
     * That's the big button at the bottom right corner of the screen.
     * @param v  The view associated with the button
     */
    public void onAddButtonClick(View v){
    	launchGridActivity();
    }
    
    /**
     * Launches the GridActivity. Any results sent back from 
     * GridActivity are handled in onActivityResult();
     */
    void launchGridActivity(){
    	Intent intent = new Intent(getApplicationContext(),GridActivity.class);
		startActivityForResult(intent, NEW_INSTRUCTION);
    }
    
    /**
     * Assembles the file currently shown on the screen. It creates a new
     * Assembler object and calls its assemble(ListArray<String>) method.
     */
	void assembleFile(){
		if(textBuffer.isEmpty()){
			Toast.makeText(getApplicationContext(), "There's nothing to compile", Toast.LENGTH_SHORT).show();
			return;
		}
		try{
		assembler = new Assembler();	
		assembler.setEventListener(assmListener);
		assembler.assemble(textBuffer);
		}catch(NullPointerException e){
			Toast.makeText(getApplicationContext(), "exception during assembly", Toast.LENGTH_SHORT).show();
		}
	}
    
	/**
	 * Displays the save file dialog window. If the user
	 * decides to save a file, that action will be handled
	 * by onDialogSaveFile listener
	 */
	void showSaveDialog(){
		SaveDialogFragment saveDialog = new SaveDialogFragment();
		saveDialog.show(getSupportFragmentManager(), "dialog");
	}
	
	/**
	 * Displays an open file dialog window. If an open action
	 * is requested, such action will be handled by the onDialogOpenFile
	 * listener
	 */
	void showOpenDialog(){
		SaveDialogFragment openDialog = new SaveDialogFragment();
		openDialog.setSaveDialog(false); // make the dialog a open file dialog
		openDialog.show(getSupportFragmentManager(), "dialog");
	}

	/**
	 * Saves the file currently shown on the screen with
	 * the name given in the parameter.
	 * @param file  The name of the file to save
	 */
	void saveTextFileBuffer(String filename){
		try{
			FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
			for(int i = 0; i < textBuffer.size(); i++){
				String temp = textBuffer.get(i) + "\n"; 
				fos.write(temp.getBytes());
			}
			fos.close();
		}catch(IOException e){
			Toast.makeText(getApplicationContext(), "Unable to save file", Toast.LENGTH_SHORT).show();
		}
		Toast.makeText(getApplicationContext(), "saved " + filename, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Opens the file specified in the parameter and
	 * fills the text file buffer. 
	 * @param filename   The name of the file to open
	 */
	void openTextFile(String filename){
		// For now, let's just clear the current text file
		// TODO	The current text file might need saving. Need to add that check
		if(!adapter.isEmpty())
			adapter.clear();
		try{
			FileInputStream fis = openFileInput(filename);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			while( (line = reader.readLine()) != null)
				adapter.add(line);
			reader.close();
			fis.close();
		}catch(IOException e){
			Toast.makeText(getApplicationContext(), "Unable to open file", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	//******************************************
	//	LISTENER AND CALLBACK IMPLEMENTATIONS
    //******************************************
    
    
	/**
	 * ActionMode callback. Called when an item in the list is
	 * clicked and held. Set whenever the activity is created.
	 * SomeView.setOnItemLongClick(mActionModeCallback);
	 */
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
		
		@Override
		public boolean onCreateActionMode(ActionMode arg0, Menu arg1) {
			MenuInflater inflater = arg0.getMenuInflater();
			inflater.inflate(R.menu.context_menu, arg1);
			return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode arg0, MenuItem arg1) {
			// TODO Auto-generated method stub
			switch(arg1.getItemId()){
				case R.id.action_delete_item:
						//Toast.makeText(getApplicationContext(), "not implemented yet", Toast.LENGTH_SHORT).show();
						textBuffer.remove(candidate);
	    				adapter.notifyDataSetChanged();
						arg0.finish();
						return true;
				default: 
						return true;
			}
		}
		
		@Override
		public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public void onDestroyActionMode(ActionMode arg0) {
			// TODO Auto-generated method stub
			mActionMode = null;
		}
	};
	
	
	/**
	 * Not sure I'll be using this one
	 */
	AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//TODO finish this at some point
			} 
    };
    
    
    /**
     * Handles long click events on the items of the 
     * text file. Starts the action mode
     */
    AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
    	
    	@Override
    	public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id){
    		if(mActionMode != null)
    			return false;
    
    		
    		//view.setBackgroundColor(color.red);
    		candidate = pos;
    		mActionMode = startSupportActionMode(mActionModeCallback);
    		return true;
    	}
	};
    
	/**
	 * The Assembler event interface. The assembler reports
	 * back to this activity using this interface
	 */
	Assembler.EventListener assmListener = new Assembler.EventListener(){
	
		public void errorEvent(String msg){
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		}
		
		
		// deal with compiled code
		public void successEvent(String msg, ArrayList<Short> c){
			/*
			byte[] buffer = ArrayListToByteArray(c);
			
			String filename = "executable.txt";
			FileOutputStream outputStream;
			try{
				outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
				outputStream.write(buffer);
				outputStream.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			FileInputStream inputStream;
			byte[] newBuffer = new byte[c.size() * 2];
			try{
				inputStream = openFileInput(filename);
				inputStream.read(newBuffer);
				inputStream.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			Short z = byteArrayToShort(buffer);
			*/
			//Toast.makeText(getApplicationContext(), String.valueOf(z) , Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), msg , Toast.LENGTH_SHORT).show();
		}
		
		public void otherEvent(String msg){
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		}
	};

	short byteArrayToShort(byte[] arg){
			byte lsbyte = arg[1];
			byte msbyte = arg[0];
			return (short)( (msbyte << 8) | lsbyte);
	}
		
	byte[] ArrayListToByteArray(ArrayList<Short> arg){
			byte[] array = new byte[arg.size() * 2]; // Two bytes in a short
			for(int i = 0; i < arg.size(); i++){
				Short temp = arg.get(i).shortValue();
				array[i*2] = (byte)(temp >> 8);
				array[i*2+1] = temp.byteValue();
			}
			return array;
	}
		
	
	/**
	 * Saves the text file currently shown on the screen
	 * to disk. Called after the user clicks OK on the save
	 * dialog window.
	 */
	/*
	SaveDialogFragment.NoticeDialogListener dialogListener =
			new SaveDialogFragment.NoticeDialogListener() {
				
				@Override
				public void onDialogSaveFile(String file) {
					// TODO Auto-generated method stub
					saveTextFileBuffer(file);
					Toast.makeText(getApplicationContext(), "Saved " + file + ".s", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onDialogOpenFile(String file) {
					// TODO Auto-generated method stub
					
				}
			};
			*/
	/**
	 * Called if the user proceeds to save a file after
	 * being presented with the save dialog window
	 */
	public void onDialogSaveFile(String file) {
		saveTextFileBuffer(file);
	}

	/**
	 * Called if the user proceeds to open a file after
	 * being presented with the open file dialog window
	 */
	public void onDialogOpenFile(String file) {
		openTextFile(file);
	}
}
