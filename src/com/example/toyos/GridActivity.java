package com.example.toyos;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class GridActivity extends ActionBarActivity {

		private static final String DEBUG_TAG = "Gestures";
		 		
		private static final int COMMENT= 0;
		private static final int LOAD 	= 1;
		private static final int LOADI 	= 2;
		private static final int STORE 	= 3;
		private static final int ADD 	= 4;
		private static final int ADDI 	= 5;
		private static final int ADDC 	= 6;
		private static final int ADDCI 	= 7;
		private static final int SUB	= 8;
		private static final int SUBI	= 9;
		private static final int SUBC 	= 10;
		private static final int SUBCI	= 11;		
		private static final int AND	= 12;
		private static final int ANDI	= 13;
		private static final int XOR	= 14;
		private static final int XORI	= 15;
		private static final int COMPL 	= 16;
		private static final int SHL	= 17;
		private static final int SHLA	= 18;
		private static final int SHR	= 19;
		private static final int SHRA	= 20;
		private static final int COMPR	= 21; 
		private static final int COMPRI	= 22;
		private static final int GETSTAT= 23;
		private static final int PUTSTAT= 24;
		private static final int JUMP	= 25;
		private static final int JUMPL	= 26;
		private static final int JUMPE	= 27;
		private static final int JUMPG	= 28;
		private static final int CALL	= 29;
		private static final int RET	= 30;
		private static final int READ	= 31;
		private static final int WRITE	= 32;
		private static final int HALT	= 33;
		private static final int NOOP	= 34;
		
			
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        
        GridView gridview = (GridView) findViewById(R.id.gridview);
        if(gridview == null){
        	Log.d(DEBUG_TAG, "error getting gridview");
        	finish();
        }
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
   
            	switch(position){
             	case COMMENT:
             		launchArgumentActivity("!", MainActivity.COMMENT);
            		break;
           		case LOAD 	:
           			launchArgumentActivity("load", MainActivity.REG_AND_VALUE);
            		break;
        		case LOADI 	:
        			launchArgumentActivity("loadi", MainActivity.REG_AND_VALUE);
            		break;
        		case STORE 	:
        			launchArgumentActivity("store", MainActivity.REG_AND_VALUE);
            		break;
        		case ADD 	:
        			launchArgumentActivity("add", MainActivity.REG_AND_REG);
            		break;
        		case ADDI 	:
        			launchArgumentActivity("addi", MainActivity.REG_AND_VALUE);
            		break;
        		case ADDC 	:
        			launchArgumentActivity("addc", MainActivity.REG_AND_REG);
            		break;
        		case ADDCI 	:
        			launchArgumentActivity("addci", MainActivity.REG_AND_VALUE);
            		break;
        		case SUB	:
        			launchArgumentActivity("sub", MainActivity.REG_AND_REG);
            		break;
        		case SUBI	:
        			launchArgumentActivity("subi", MainActivity.REG_AND_VALUE);
            		break;
        		case SUBC 	:
        			launchArgumentActivity("subc", MainActivity.REG_AND_REG);
            		break;
        		case SUBCI	:
        			launchArgumentActivity("subci", MainActivity.REG_AND_VALUE);
            		break;
        		case AND		:
        			launchArgumentActivity("and", MainActivity.REG_AND_REG);
            		break;
        		case ANDI	:
        			launchArgumentActivity("andi",MainActivity.REG_AND_VALUE);
            		break;
        		case XOR	:
        			launchArgumentActivity("xor", MainActivity.REG_AND_REG);
            		break;
        		case XORI	:
        			launchArgumentActivity("xori", MainActivity.REG_AND_VALUE);
            		break;
        		case COMPL 	:
        			launchArgumentActivity("compl", MainActivity.SINGLE_REG);
            		break;
        		case SHL	:
        			launchArgumentActivity("shl", MainActivity.SINGLE_REG);
            		break;
        		case SHLA	:
        			launchArgumentActivity("shla", MainActivity.SINGLE_REG);
            		break;
        		case SHR	:
        			launchArgumentActivity("shr", MainActivity.SINGLE_REG);
            		break;
        		case SHRA	:
        			launchArgumentActivity("shra", MainActivity.SINGLE_REG);
            		break;
        		case COMPR	:
        			launchArgumentActivity("compr", MainActivity.REG_AND_REG);
            		break;
        		case COMPRI	:
        			launchArgumentActivity("compri", MainActivity.REG_AND_VALUE);
            		break;
        		case GETSTAT	:
        			launchArgumentActivity("getstat", MainActivity.SINGLE_REG);
            		break;
        		case PUTSTAT	:
        			launchArgumentActivity("putstat", MainActivity.SINGLE_REG);
            		break;
        		case JUMP	:
        			launchArgumentActivity("jump", MainActivity.SINGLE_VALUE);
            		break;
        		case JUMPL	:
        			launchArgumentActivity("jumpl", MainActivity.SINGLE_VALUE);
            		break;
        		case JUMPE	:
        			launchArgumentActivity("jumpe", MainActivity.SINGLE_VALUE);
            		break;
        		case JUMPG	:
        			launchArgumentActivity("jumpg", MainActivity.SINGLE_VALUE);
            		break;
        		case CALL	:
        			launchArgumentActivity("call", MainActivity.SINGLE_VALUE);
            		break;
           		case READ	:
           			launchArgumentActivity("read", MainActivity.SINGLE_REG);
            		break;
        		case WRITE	:
        			launchArgumentActivity("write", MainActivity.SINGLE_REG);
            		break;
        		case HALT:
            		finishWithResult("halt");
            		break;
            	case RET:
            		finishWithResult("return");
            		break;
            	case NOOP:	
            		finishWithResult("noop");
            		break;
            	}
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.grid, menu);
        return true;
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
    
    public void finishWithResult(String instr){
    	Intent result = new Intent();
    	result.putExtra(MainActivity.INSTRUCTION, instr);
    	setResult(MainActivity.RESULT_OK, result);
    	finish();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
    	if(intent != null){
    		if(requestCode == MainActivity.GET_ARGUMENTS && resultCode == MainActivity.RESULT_OK){
    			if(intent.hasExtra(MainActivity.INSTRUCTION))
    				finishWithResult(intent.getStringExtra(MainActivity.INSTRUCTION));
    		}
    	}
    }
    
    public void launchArgumentActivity(String opcode, int dialog_type){
    	Intent intent = new Intent(getApplicationContext(), ArgumentActivity.class);
    	intent.putExtra(MainActivity.OPCODE, opcode);
    	intent.putExtra(MainActivity.DIALOG_TYPE, dialog_type);
    	startActivityForResult(intent, MainActivity.GET_ARGUMENTS);
    }
    
    
}


