package tokenizer_whatsapp;


import java.io.IOException;
import java.io.InputStreamReader;

import tokenizer.Tokenizer;

public class WhatsappTokenizer implements Tokenizer<String> {

	private char delimiter;
	private InputStreamReader isr;
	private boolean alive;

	// Constructor
	public WhatsappTokenizer(){
		delimiter = '$';
		isr = null;
		alive = true;
	}

	@Override
	// returns a string contains the next message of the InputStreamReader
	public String nextMessage() {
		if (!isAlive())
			return null;

		String ans = null;
		try {
			int c;
			StringBuilder sb = new StringBuilder();
			while ((c = isr.read()) != -1) {
				if (c == delimiter){
					break;
				}else{
					sb.append((char) c);
				}
			}
			ans = sb.toString();
		} catch (IOException e) {
			alive = false;
			e.printStackTrace();
		}
		return ans;

	}

	@Override
	// GETTER for the member variable isAlive
	public boolean isAlive() {
		return alive;
	}

	@Override
	// SETTER for the member variable isr
	public void addInputStream(InputStreamReader inputStreamReader) {
		isr = inputStreamReader;
	}

}
