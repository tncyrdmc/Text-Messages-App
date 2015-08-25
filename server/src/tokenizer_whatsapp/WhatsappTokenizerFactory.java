package tokenizer_whatsapp;

import tokenizer.Tokenizer;
import tokenizer.TokenizerFactory;

public class WhatsappTokenizerFactory implements TokenizerFactory<String> {

	public Tokenizer<String> create(){
		return new WhatsappTokenizer();
	}
}
