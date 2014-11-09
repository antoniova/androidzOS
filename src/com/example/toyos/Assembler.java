package com.example.toyos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Assembler {
	
	interface Instruction{
		void exec();
	}
	
	// Implemented in the MainActivity
	public interface EventListener {
		void errorEvent(String msg);
		void successEvent(String msg, ArrayList<Short> obj);
		void otherEvent(String msg);
	}
	
	private static final int LOAD_OP 	= 0;
	private static final int LOADI_OP 	= 0;
	private static final int STORE_OP 	= 1;
	private static final int ADD_OP 	= 2;
	private static final int ADDI_OP 	= 2;
	private static final int ADDC_OP 	= 3;
	private static final int ADDCI_OP 	= 3;
	private static final int SUB_OP		= 4;
	private static final int SUBI_OP	= 4;
	private static final int SUBC_OP 	= 5;
	private static final int SUBCI_OP	= 5;		
	private static final int AND_OP		= 6;
	private static final int ANDI_OP	= 6;
	private static final int XOR_OP		= 7;
	private static final int XORI_OP	= 7;
	private static final int COMPL_OP 	= 8;
	private static final int SHL_OP		= 9;
	private static final int SHLA_OP	= 10;
	private static final int SHR_OP		= 11;
	private static final int SHRA_OP	= 12;
	private static final int COMPR_OP	= 13; 
	private static final int COMPRI_OP	= 13;
	private static final int GETSTAT_OP	= 14;
	private static final int PUTSTAT_OP	= 15;
	private static final int JUMP_OP	= 16;
	private static final int JUMPL_OP	= 17;
	private static final int JUMPE_OP	= 18;
	private static final int JUMPG_OP	= 19;
	private static final int CALL_OP	= 20;
	private static final int RET_OP		= 21;
	private static final int READ_OP	= 22;
	private static final int WRITE_OP	= 23;
	private static final int HALT_OP	= 24;
	private static final int NOOP_OP	= 25;
	private static final String SUCCESS = "Compilation finished successfully";
	
	
	private Map<String, Instruction> mymap;
	private EventListener event;
	private String opcode;
	private int rdest, rsource, constant, address;
	private boolean error = false;
	private Tokenizer tokenizer;
	private String lineNumber;
	
	//ArrayList<Integer> objectCode;
	ArrayList<Short> objectCode;
	

	// Constructor
	Assembler(){
		objectCode  = new ArrayList<Short>();
		mymap = new HashMap<String,Instruction>();
		mymap.put("load", load);
		mymap.put("loadi", loadi);
		mymap.put("store", store);
		mymap.put("add", add);
		mymap.put("addi", addi);
		mymap.put("addc", addc);
		mymap.put("addci", addci);
		mymap.put("sub", sub);
		mymap.put("subi", subi);
		mymap.put("subc", subc);
		mymap.put("subci", subci);
		mymap.put("and", and);
		mymap.put("andi", andi);
		mymap.put("xor", xor);
		mymap.put("xori", xori);
		mymap.put("compl", compl);
		mymap.put("shl", shl);
		mymap.put("shla", shla);
		mymap.put("shr", shr);
		mymap.put("shra", shra);
		mymap.put("compr", compr);
		mymap.put("compri", compri);
		mymap.put("getstat", getstat);
		mymap.put("putstat", putstat);
		mymap.put("jump", jump);
		mymap.put("jumpl", jumpl);
		mymap.put("jumpe", jumpe);
		mymap.put("jumpg", jumpg);
		mymap.put("call", call);
		mymap.put("return", ret);
		mymap.put("read", read);
		mymap.put("write", write);
		mymap.put("halt", halt);
		mymap.put("noop", noop);
		
	}
	
	void setEventListener(EventListener ev){
		event = ev;
	}
	
	void testEvent(){
		event.errorEvent("Hello from the Assember");
	}
	

	void assemble(ArrayList<String> srcCode){
		String line;
		for(int i = 0; i < srcCode.size(); i++){
			lineNumber = Integer.toString(i);
			line = new String(srcCode.get(i));
			if( !line.isEmpty() && (line.charAt(0) != '!') ){
				tokenizer = new Tokenizer(line);
				opcode = tokenizer.nextToken();
				mymap.get(opcode).exec(); 
			}
		}
		
		if(!error)
			event.successEvent(SUCCESS, objectCode);
	} 
	
	/**
	 *   INSTRUCTIONS		
	 */
	
	
	Instruction load = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			address = (Integer.valueOf( tokenizer.nextToken())).intValue();
			if(validAddress())
				objectCode.add((short)( (LOAD_OP << 11) | (rdest << 9) | address )  ) ;
			else
				invalidAddressError();

		}
	};
	
	Instruction loadi = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			constant = (Integer.valueOf( tokenizer.nextToken())).intValue();
			if(validConstant())
				objectCode.add((short) ( (LOADI_OP << 11)|(rdest << 9)|(1 << 8)|(constant & 0xff) ) );
			else
				invalidConstantError();
		}	
	};
	
	Instruction store = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			address = (Integer.valueOf( tokenizer.nextToken())).intValue();
			if(validAddress())
				objectCode.add((short) ( (STORE_OP << 11)|(rdest << 9)|address ) );
			else
				invalidAddressError();
		}
	};

	Instruction add = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			rsource = (Integer.valueOf( tokenizer.nextToken())).intValue();
			objectCode.add((short) ( (ADD_OP << 11)|(rdest << 9)|(rsource << 6) ) );
		}
	};
	
	Instruction addi = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			constant = (Integer.valueOf( tokenizer.nextToken())).intValue();
			if(validConstant())
				objectCode.add((short) ( (ADDI_OP << 11)|(rdest << 9)|(1 << 8)|(constant & 0xff) ) );
			else
				invalidConstantError();
		}
	};
	
	Instruction addc = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			rsource = (Integer.valueOf( tokenizer.nextToken())).intValue();
			objectCode.add((short) ( (ADDC_OP << 11)|(rdest << 9)|(rsource << 6) ) );
		}
	};
	
	Instruction addci = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			constant = (Integer.valueOf( tokenizer.nextToken())).intValue();
			if(validConstant())
				objectCode.add((short)( (ADDCI_OP << 11) | (rdest << 9) | (1 << 8) | (constant & 0xff) ));
			else
				invalidConstantError();
		}
	};
	
	Instruction sub = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			rsource = (Integer.valueOf( tokenizer.nextToken())).intValue();
			objectCode.add((short)( (SUB_OP << 11) | (rdest << 9) | (rsource << 6) ));
		}
	};
	
	Instruction subi = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			constant = (Integer.valueOf( tokenizer.nextToken())).intValue();
			if(validConstant())
				objectCode.add((short)( (SUBI_OP << 11) | (rdest << 9) | (1 << 8) | (constant & 0xff) ));
			else
				invalidConstantError();
		}
	};
	
	Instruction subc = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			rsource = (Integer.valueOf( tokenizer.nextToken())).intValue();
			objectCode.add((short)( (SUBC_OP << 11) | (rdest << 9) | (rsource << 6) ));
		}
	};
	
	Instruction subci = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			constant = (Integer.valueOf( tokenizer.nextToken())).intValue();
			if(validConstant())
				objectCode.add((short)( (SUBCI_OP << 11) | (rdest << 9) | (1 << 8) | (constant & 0xff) ));
			else
				invalidConstantError();
		}
	};
	
	Instruction and = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			rsource = (Integer.valueOf( tokenizer.nextToken())).intValue();
			objectCode.add((short)( (AND_OP << 11) | (rdest << 9) | (rsource << 6) ));
		}
	};
	
	Instruction andi = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			constant = (Integer.valueOf( tokenizer.nextToken())).intValue();
			if(validConstant())
				objectCode.add((short)( (ANDI_OP << 11) | (rdest << 9) | (1 << 8) | (constant & 0xff) ));
			else
				invalidConstantError();
		}
	};
	
	Instruction xor = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			rsource = (Integer.valueOf( tokenizer.nextToken())).intValue();
			objectCode.add((short)( (XOR_OP << 11) | (rdest << 9) | (rsource << 6) ));
		}
	};
	
	Instruction xori = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			constant = (Integer.valueOf( tokenizer.nextToken())).intValue();
			if(validConstant())
				objectCode.add((short)( (XORI_OP << 11) | (rdest << 9) | (1 << 8) | (constant & 0xff) ));
			else
				invalidConstantError();
		}
	};
	
	Instruction compl = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			objectCode.add((short)( (COMPL_OP << 11) | (rdest << 9) ));
		}
	};
	
	Instruction shl = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			objectCode.add((short)( (SHL_OP << 11) | (rdest << 9) ));
		}
	};
	
	Instruction shla = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			objectCode.add((short)( (SHLA_OP << 11) | (rdest << 9) ));
		}
	};
	
	Instruction shr = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			objectCode.add((short)( (SHR_OP << 11) | (rdest << 9) ));
		}
	};
	
	Instruction shra = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			objectCode.add((short)( (SHRA_OP << 11) | (rdest << 9) ));
		}
	};
	
	Instruction compr = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			rsource = (Integer.valueOf( tokenizer.nextToken())).intValue();
			objectCode.add((short)( (COMPR_OP << 11) | (rdest << 9) | (rsource << 6) ));
		}
	}; 
	
	Instruction compri = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			constant = (Integer.valueOf( tokenizer.nextToken())).intValue();
			if(validConstant())
				objectCode.add((short)( (COMPRI_OP << 11) | (rdest << 9) | (1 << 8) | (constant & 0xff) ));
			else
				invalidConstantError();
		}
	};
	
	Instruction getstat = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			objectCode.add((short)( (GETSTAT_OP << 11) | (rdest << 9) ));
		}
	};
	
	Instruction putstat = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			objectCode.add((short)( (PUTSTAT_OP << 11) | (rdest << 9) ));
		}
	};
	
	Instruction jump = new Instruction(){
		public void exec(){
			address = (Integer.valueOf( tokenizer.nextToken())).intValue();
			if(validAddress())
				objectCode.add((short)( (JUMP_OP << 11) | address ));
			else
				invalidAddressError();
		}
	};
	
	Instruction jumpl = new Instruction(){
		public void exec(){
			address = (Integer.valueOf( tokenizer.nextToken())).intValue();
			if(validAddress())
				objectCode.add((short)( (JUMPL_OP << 11) | address ));
			else
				invalidAddressError();
		}
	};
	
	Instruction jumpe = new Instruction(){
		public void exec(){
			address = (Integer.valueOf( tokenizer.nextToken())).intValue();
			if(validAddress())
				objectCode.add((short)( (JUMPE_OP << 11) | address ));
			else
				invalidAddressError();
		}
	};
	
	Instruction jumpg = new Instruction(){
		public void exec(){
			address = (Integer.valueOf( tokenizer.nextToken())).intValue();
			if(validAddress())
				objectCode.add((short)( (JUMPG_OP << 11) | address ));
			else
				invalidAddressError();
		}
	};
	
	Instruction call = new Instruction(){
		public void exec(){
			address = (Integer.valueOf( tokenizer.nextToken())).intValue();
			if(validAddress())
				objectCode.add((short)( (CALL_OP << 11) | address ));
			else
				invalidAddressError();
		}
	};
	
	Instruction ret = new Instruction(){
		public void exec(){
			objectCode.add((short)(RET_OP << 11));
		}
	};
	
	Instruction read = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			objectCode.add((short)( (READ_OP << 11) | (rdest << 9) ));
		}
	};
	
	Instruction write = new Instruction(){
		public void exec(){
			rdest = (Integer.valueOf( tokenizer.nextToken())).intValue();
			objectCode.add((short)( (WRITE_OP << 11) | (rdest << 9) ));
		}
	};
	
	Instruction halt = new Instruction(){
		public void exec(){
			objectCode.add((short)(HALT_OP << 11));
		}
	};
	
	Instruction noop = new Instruction(){
		public void exec(){
			objectCode.add((short)(NOOP_OP << 11));
		}
	};
	// END OF INSTRUCTIONS
	
	
	boolean validAddress(){
		return (address >= 0) && (address <= 255);
	}
	
	boolean validConstant(){
		return (constant >= -128) && (constant <= 128);
	}
	
	void invalidAddressError(){
		error = true;
		String err = "Error in line " + lineNumber +
				" : Invalid addres value";
		event.errorEvent(err);
	}
		
	void invalidConstantError(){
		error = true;
		String err = "Error in line " + lineNumber +
				" : Invalid constant value";
		event.errorEvent(err);
	}
	
}
