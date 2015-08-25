package protocol_whatsapp;

import protocol.ServerProtocol;
import protocol.ServerProtocolFactory;

public class WhatsAppProtocolFactory implements ServerProtocolFactory<String> {
	private WhatsAppProtocol w = null;
	
	public ServerProtocol<String> create(){
		if(w == null)
			w = new WhatsAppProtocol();
		return w;
	}
	
}
