package protocol_http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import protocol.ServerProtocol;
import protocol.ServerProtocolFactory;

public class HttpProtocol implements ServerProtocol<String>, ServerProtocolFactory<String> {

	
	@Override
	// Factory method
	public ServerProtocol<String> create() {
		return new HttpProtocol();
	}

	@Override
	// gets a message, creating a reply message and returns it.
	public String processMessage(String msg) {
		String[] lines = ((msg.trim()).split("\n"));
		String firstLine = "";
		if(lines.length >= 1)
			firstLine = lines[0];
		else
			return createHttpMsgWithoutHeader(715,"Error 715: Invalid Format");
		
		if(!(firstLine.matches("((^POST)|(^GET))+ /+((login)|(list)|(create_group)|(send)|(add_user)|(remove_user)|(logout)|(queue))+.+(jsp)+( HTTP/1.1)")))
			return createHttpMsgWithoutHeader(404,"Error 404: Not Found");
		
		if((firstLine.matches("(^POST)+ /+((login)|(list)|(create_group)|(send)|(add_user)|(remove_user))+.+(jsp)+( HTTP/1.1)")) || 
				(firstLine.matches("(^GET)+ /+((logout)|(queue))+.+(jsp)+( HTTP/1.1)")))
			return null;
		return createHttpMsgWithoutHeader(405,"Error 405: Method Not Allowed");
	}

	// returns true if the msg is a POST msg. otherwise, returns false.
	protected boolean isPostMsg(String line){
		return line.startsWith("POST");
	}
	
	// returns true if the msg is a POST msg. otherwise, returns false.
	protected boolean isGetMsg(String line){
		return line.startsWith("GET");
	}
	
	// gets a uri cookie line, returns the uri of the message.
	public String getUriMsg(String line){
		return (line.split(" "))[1].substring(1);
	}
	
	// gets a header cookie line, returns the cookie value of the line.
	public String getCookieValue(String line){
		boolean hasCookie = line.startsWith("Cookie:");
		if(hasCookie)
			return (line.split("="))[1];
		return "";
	}
	
	// gets a header cookie line, returns the cookie name of the line.
	protected String getCookieName(String line){
		boolean hasCookie = line.startsWith("Cookie:");
		if(hasCookie)
			return (line.substring(7).split("="))[0];
		return null;
	}
	
	// gets a msg, returns the msg body.
	public String getMsgBody(String msg){
		if(!msg.contains("\n\n"))
			return null;
		String msgBody = null;
		try {
			msgBody = URLDecoder.decode(msg.split("\n\n")[1], "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return msgBody;
	}
	
	// gets the data required, creates and returns a msg in a HTTP format.
	protected String createHttpMsg(int statusCode, String header, String msgBody){
		return "HTTP/1.1 " + String.valueOf(statusCode) + "\n" + header + "\n\n" + msgBody + "\n$";
	}
	
	// gets the data required, creates and returns a msg in a HTTP format (without a header).
	public String createHttpMsgWithoutHeader(int statusCode, String msgBody){
		return "HTTP/1.1 " + String.valueOf(statusCode) + "\n\n" + msgBody + "\n$";
	}
	
	// gets the data required, creates and returns a msg in a HTTP format (without a header and with a cookie).
	public String createHttpMsgWithCookie(int statusCode, String cookieValue, String msgBody){
		return "HTTP/1.1 " + String.valueOf(statusCode) + "\nSet-Cookie: user_auth=" + cookieValue  + "\n\n" + msgBody + "\n$";
	}
	
	// gets a msg body, creates and returns a HTTP msg with the msg body.
	public String createValidTextHttpMsg(String msgBody){
		return "HTTP/1.1 200\n\n" + msgBody + "\n$";
	}

	@Override
	// gets a msg, check if it's an "exit" msg, returns true if it is. otherwise, returns false.
	public boolean isEnd(String msg) {
		return msg.equalsIgnoreCase("exit");
	}

}
