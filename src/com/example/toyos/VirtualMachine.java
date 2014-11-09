package com.example.toyos;

import android.content.Context;
import android.widget.Toast;



public class VirtualMachine {

	interface Instruction{
		void exec();
	}
	
	private final int REG_FILE_SIZE = 4;
	private final int MEMORY_SIZE = 256;
	private final int INSTRUCTION_SET_SIZE = 26;
	
	private final int OPCODE_MASK = 0xF800;		// 1111 1000 0000 0000
	private final int DEST_REG_MASK = 0x0600;  	// 0000 0110 0000 0000
	private final int SOURCE_REG_MASK = 0x00C0; // 0000 0000 1100 0000
	private final int CONST_MASK = 0x00ff;		// 0000 0000 1111 1111
	private final int I_BIT_MASK = 0x0100;	 	// 0000 0001 0000 0000
	private final int SIGN_BIT_MASK = 0x80; 	//		 	 1000 0000
	private final int SIGN_EXTEND_MASK = 0xffff0000;
	
	
	// Used to access the status register 
	private final int OVERFLOW_BIT_MASK = 0x10; // 0001 0000
	private final int LESS_BIT_MASK = 0x08; 	// 0000 1000
	private final int EQUAL_BIT_MASK = 0x04; 	// 0000 0100
	private final int GREATER_BIT_MASK = 0x02; 	// 0000 0010
	private final int CARRY_BIT_MASK = 0x01; 	// 0000 0001
	
	Context mcontext;
	private int opcode;
	private int rs, rd;
	private int constant, address;
	
	// internal registers and other goodies
	private int programCounter;
	private int instructionReg;
	private int statusReg;
	private int stackPointer;
	private int base;
	private int limit;
	private int[] register;		// register file
	private int[] memory;		// physical memory
	private boolean immBit;		// immediate bit	
	private int clock;			
	
	private Instruction[] instructionSet;
	private boolean stop;

	
	/**
	 * Constructor
	 */
	VirtualMachine(Context c){
		mcontext = c;
		register = new int[REG_FILE_SIZE];
		memory = new int[MEMORY_SIZE];
		instructionSet = new Instruction[INSTRUCTION_SET_SIZE];
		instructionSet[0] = load;
		instructionSet[1] = store;
		instructionSet[2] = add;
		instructionSet[3] = addc;
		instructionSet[4] = sub;
		instructionSet[5] = subc;
		instructionSet[6] = and;
		instructionSet[7] = xor;
		instructionSet[8] = compl;
		instructionSet[9] = shl;
		instructionSet[10] = shla;
		instructionSet[11] = shr;
		instructionSet[12] = shra;
		instructionSet[13] = compr;
		instructionSet[14] = getstat;
		instructionSet[15] = putstat;
		instructionSet[16] = jump;
		instructionSet[17] = jumpl;
		instructionSet[18] = jumpe;
		instructionSet[19] = jumpg;
		instructionSet[20] = call;
		instructionSet[21] = ret;
		instructionSet[22] = read;
		instructionSet[23] = write;
		instructionSet[24] = halt;
		instructionSet[25] = noop;
		stop = false;
		programCounter = 0;
		instructionReg = 0;
		clock = 0;
		base = limit = 0;
		stackPointer = 256;  
	}
	
	void runProcess(){
		while(!stop){
		
			instructionReg = memory[programCounter];
			decodeInstruction(instructionReg);
			instructionSet[opcode].exec();
			
		}
	}
	 
	/**
	 * Decodes a machine instruction. That is, it extracts
	 * the opcode and all possible arguments to complete an
	 * instruction.
	 * @param instruction
	 */
	void decodeInstruction(int instruction){
		opcode = (instruction & OPCODE_MASK) >> 11;
		rd = (instruction & DEST_REG_MASK ) >> 9;
		rs = (instruction & SOURCE_REG_MASK) >> 6;
		address = (instruction & CONST_MASK); //TODO constant needs to be sign extended
		constant = (instruction & CONST_MASK); //TODO constant needs to be sign extended
		immBit = ((instruction & I_BIT_MASK) > 0);
	}
	
	
	Instruction load = new Instruction(){
		public void exec(){
			if(immBit)
				display("loadi");
			else
				display("load");
		}
	};
	
	Instruction store = new Instruction(){
		public void exec(){
			display("store");
		}
	};
	
	Instruction add = new Instruction(){
		public void exec(){
			if(immBit)
				display("addi");
			else
				display("add");
			/*
			if(immediateBit)
				register[rd] += constant;
			else
				register[rd] += register[rs]; 
				*/
		}
	};
	
	Instruction addc = new Instruction(){
		public void exec(){
			if(immBit)
				display("addci");
			else
				display("addc");
		}
	};
	
	Instruction sub = new Instruction(){
		public void exec(){
			if(immBit)
				display("subi");
			else
				display("sub");
			//register[rd] -= register[rs];
		}
	};
	
	Instruction subc = new Instruction(){
		public void exec(){
			if(immBit)
				display("subci");
			else
				display("subc");
		}
	};
	
	Instruction and = new Instruction(){
		public void exec(){
			if(immBit)
				display("andi");
			else
				display("and");
			//register[rd] &= register[rs];
		}
	};
	
	Instruction xor = new Instruction(){
		public void exec(){
			if(immBit)
				display("xori");
			else
				display("xor");
			//register[rd] ^= register[rs];
		}
	};
	
	Instruction compl = new Instruction(){
		public void exec(){
			display("compl");
		}
	};
	
	Instruction shl = new Instruction(){
		public void exec(){
			display("shl");
		}
	};
	
	Instruction shla  = new Instruction(){
		public void exec(){
			display("shla");
		}
	};
	
	Instruction shr = new Instruction(){
		public void exec(){
			display("shr");
		}
	};
	
	Instruction shra = new Instruction(){
		public void exec(){
			display("shra");
		}
	};
	
	Instruction compr = new Instruction(){
		public void exec(){
			if(immBit)
				display("compri");
			else
				display("compr");
		}
	};	
	
	Instruction getstat = new Instruction(){
		public void exec(){
			display("getstat");
		}
	};
	
	Instruction putstat = new Instruction(){
		public void exec(){
			display("putstat");
		}
	};
	
	Instruction jump = new Instruction(){
		public void exec(){
			display("jump");
		}
	};
	
	Instruction jumpl = new Instruction(){
		public void exec(){
			display("jumpl");
		}
	};
	
	Instruction jumpe = new Instruction(){
		public void exec(){
			display("jumpe");
		}
	};
	
	Instruction jumpg = new Instruction(){
		public void exec(){
			display("jumpg");
		}
	};


	/* Memory layout:
	   0[ 	]Top
		[	]
		[	]
	 255[	]
	  		 <--- stack pointer (initially)
	 * Stack grows upwards.
	 */
	Instruction call = new Instruction(){
		public void exec(){
			display("call");
		}
	};
	
	Instruction ret = new Instruction(){
		public void exec(){
			display("return");
		}
	};
	
	Instruction read = new Instruction(){
		public void exec(){
			display("read");
		}
	};
	
	Instruction write = new Instruction(){
		public void exec(){
			display("write");
		}
	};
	
	Instruction halt = new Instruction(){
		public void exec(){
			display("halt");
			stop = true;
		}
	};
	
	Instruction noop = new Instruction(){
		public void exec(){
			display("noop");
		}
	};
	
	void display(String str){
		Toast.makeText(mcontext, str, Toast.LENGTH_SHORT).show();
	}
	
	
}
