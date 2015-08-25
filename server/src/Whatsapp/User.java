package Whatsapp;
import java.util.ArrayList;

public class User {
	
	private final String phone;
	private final String userName;
	private ArrayList<String> msgs;
	
	// Constructor
	public User(String phone, String userName){
		this.userName = userName;
		this.phone = phone;
		msgs = new ArrayList<String>();
	}

	// GETTER for the member variable userName
	public String getUserName() {
		return userName;
	}
	
	// GETTER for the member variable phone
	public String getPhone(){
		return phone;
	}

	// gets a phone. returns true if the phone is equals to the phone member variable. otherwise, returns false.
	public boolean isPhoneEquals(String phone){
		return this.phone.equals(phone);
	}
	
	// gets a user name. returns true if the user name is equals to the user name member variable. otherwise, returns false.	
	public boolean isNameEquals(String userName){
		return this.userName.equals(userName);
	}

	// gets a user name and a phone. returns true if the user name and the phone equals to the member variables. otherwise, returns false.	
	public boolean equals(String userName, String phone) {
		return isNameEquals(userName) && isPhoneEquals(phone);
	}
	
	// returns a string with unique string based on the username and phone.
	public String getHashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return "aCg12" + String.valueOf(Math.abs(result));
	}
	
	// gets a sender and content of msg and adds it to the messages data structure.
	public void addMsg(String sender,String content){
		msgs.add("From:" + sender + "\nMsg:" + content + "\n");
	}
	
	// returns a string with a list of all the unread messages of the user.
	public String getUnreadMsgs(){
		if(msgs.size() == 0)
			return "No new messages";
		StringBuilder sb = new StringBuilder();
		while(msgs.size() > 0){
			sb.append(msgs.get(0));
			msgs.remove(0);
		}
		return sb.toString().trim();
	}
	
	

}
