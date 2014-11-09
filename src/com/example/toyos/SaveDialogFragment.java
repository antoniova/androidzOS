package com.example.toyos;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

public class SaveDialogFragment extends DialogFragment{
	
	public interface NoticeDialogListener{
		public void onDialogSaveFile(String file);
		public void onDialogOpenFile(String file);
	}
	
	NoticeDialogListener mListener;
	
	private final String WRONG_FILE_NAME = "Invalid name. Don't forget "
			+ "the \".s\" extension.\nOnly characters," +
			" numbers and underscores are allowed";
	private final String DIALOG_TITLE = "Save file:";
	private final String DIALOG_TITLE_OPEN = "Open file:";

	// Used to determine whether the dialog to be created should be
	// a save file dialog or an open file dialog
	// Default is save file dialog.
	private boolean saveDialog = true;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		if(saveDialog){
			builder.setView(inflater.inflate(R.layout.save_dialog, null));
			builder.setTitle(DIALOG_TITLE);
		}else{
			builder.setView(inflater.inflate(R.layout.open_textfile_dialog, null));
			builder.setTitle(DIALOG_TITLE_OPEN);
		}
		
		
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EditText t;
				if(saveDialog)
					t = (EditText) getDialog().findViewById(R.id.save_file_name);
				else
					t = (EditText) getDialog().findViewById(R.id.open_file_name);
				
				if(t != null){ // It doesn't hurt to check for this
					String fileName = t.getText().toString();
					if(!fileName.isEmpty()){
						if(isProperName(fileName)){
							if(saveDialog)
								mListener.onDialogSaveFile(fileName);
							else
								mListener.onDialogOpenFile(fileName);
						}else
							Toast.makeText(getActivity(), WRONG_FILE_NAME, Toast.LENGTH_LONG).show();
					}
				}
			}
		} );
		
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		// Nothing to do here. The dialog closes, which is the expected behavior.
			@Override 
			public void onClick(DialogInterface dialog, int which) {}
		});
		// Create AlertDialog and return it
		return builder.create();
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try{
			// Instantiate the NoticeDialogListener so we can send events to the host
			mListener = (NoticeDialogListener) activity;
		}catch(ClassCastException e){
			// The activity doesn't implement the callback interface, throw exception
			throw new ClassCastException(activity.toString() +
			" must implement NoticeDialogListener");
		}
		
	}
		
		
	private boolean isProperName(String fileName){
		// Let's be a little forgiving and allow space around the
		// file name. Let's trim it though.
		String temp = new String(fileName.trim());
		Pattern p = Pattern.compile("[^a-zA-Z0-9_\\.]");
		Matcher m = p.matcher(temp);
		// Look for anything that's not a-z, A-Z, 0-9,"_" or "." as 
		// specified in the Pattern definition above
		if(m.find())
			return false;
		String[] result = temp.split("\\.", 2);
		if( !(result[0].isEmpty()) && (result.length > 1) )
			return result[1].matches("s");
		return false;
	}
	
	public void setSaveDialog(boolean arg){
		saveDialog = arg;
	}
}
