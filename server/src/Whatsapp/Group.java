package Whatsapp;

import java.util.ArrayList;

public class Group {

	private ArrayList<User> users; // A Users data structure
	
	// Constructor
	public Group(ArrayList<User> users){
		this.users = users;
	}
	
	// returns the no. of Users in the data structure
	public int noOfUsers(){
		return users.size();
	}
	
	// gets a string if the string is a user name of a user in the data structure.
	public boolean isUserExists(String userName){
		for(int i=0; i<users.size();i++)
			if(users.get(i).isNameEquals(userName))
				return true;
		return false;
	}
	
	// gets a string if the string is a phone of a user in the data structure.
	public boolean isUserPhoneExists(String phone){
		for(int i=0; i<users.size();i++)
			if(users.get(i).isPhoneEquals(phone))
				return true;
		return false;
	}
	
	// gets a user, returns true if it's exists in the users data structure. 
	public boolean isUserExists(User u){
		for(int i=0; i<users.size();i++)
			if(users.get(i) == u)
				return true;
		return false;
	}
	
	// gets a user and adds it to the data structure.
	public void addUser(User u){
		users.add(u);
	}
	
	// gets a user and removes it from the data structure.
	public void removeUser(User u){
		users.remove(u);
	}

	// gets a string with the phones of all of the users.
	public String getPhonesList() {
		if(users.size() == 0)
			return "No Users";
		StringBuilder s = new StringBuilder();
		for(User u : users){
			s.append(u.getPhone() + ",");
		}
		return (s.substring(0, s.length()-1)).toString();	
	}
	
	// gets a string with the users and the phones of all of the users.
	public String getUsersPhonesList() {
		StringBuilder s = new StringBuilder();
		for(User u : users){
			s.append(u.getUserName() + "@" + u.getPhone() + "\n");
		}
		return s.toString();	
	}
	
	// gets a sender and a msg and sends the msg from the sender to all of the users in the group.
	public void sendMsg(String sender, String msg){
		for(User u : users)
			u.addMsg(sender, msg);
	}
	
}
