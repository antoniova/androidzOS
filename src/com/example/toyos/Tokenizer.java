package com.example.toyos;

public class Tokenizer {
	
	private String buffer;
	private boolean hasNext;
	
	Tokenizer(String str){
		buffer = new String(str);
		hasNext = true;
	}
	
	public String nextToken(){
		
		String temp = new String(buffer.trim());
		
		// Look for white spaces
		String [] substring = temp.split("\\s", 2);
		
		// If the regular expression is not found, String[] substring
		// will only have one element, the original string itself. We 
		// must catch that!.
		try{
			buffer = new String(substring[1]);
		}catch(ArrayIndexOutOfBoundsException e){
			buffer = new String(substring[0]);
			hasNext = false;
		}
			
		return substring[0];
	}
	
	public boolean hasNext(){
		return hasNext;
	}

}
