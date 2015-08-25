package Whatsapp;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Whatsapp {

		private ConcurrentHashMap<String, User> users;
		private ConcurrentHashMap<String, Group > groups;
		
		// Constructor
		public Whatsapp() {
			this.users = new ConcurrentHashMap<String, User>();
			this.groups = new ConcurrentHashMap<String, Group>();
		}

		// Gets a user name, returns the user if it exists in the users data structure. returns false otherwise.
		public User findUser(final String userName){
			for(String s : users.keySet()){
				if(users.get(s).isNameEquals(userName))
					return users.get(s);
			}
			return null;
		}

		// Gets a phone of a user, returns the user if it exists in the users data structure. returns false otherwise.		
		public User findUserByPhone(final String phone){
			for(String s : users.keySet()){
				if(users.get(s).isPhoneEquals(phone))
					return users.get(s);
			}
			return null;
		}

		// Gets a user name and a phone, returns the cookie of the user if it exists in the users data structure. returns null otherwise.		
		public String getUserCookie(final String userName, final String phone){
			for(String s : users.keySet()){
				if(users.get(s).equals(userName, phone))
					return s;
			}
			return null;
		}
		
		// gets a cookie value and a user, adds it to the users data structure.
		public void addUser(final String cookieValue, User user){
			users.put(cookieValue, user);
		}
		
		// gets a group name, returns true if group exists in the data structure. returns false otherwise.
		public boolean isGroupExist(String groupName){
			return groups.containsKey(groupName);
		}
		
		// gets a group name, returns the group if group exists in the data structure. returns false otherwise.
		public Group getGroupByName(String groupName){
			return groups.get(groupName);
		}
		
		// gets a group name and an ArrayList of users, adds the group to the groups data structure.
		public void addGroup(String groupName, ArrayList<User> groupUsers){
			groups.put(groupName, new Group(groupUsers));
		}
		
		// gets a cookie, returns true if a user with the cookie exists in the data structure. returns false otherwise.
		public User getUser(String cookie){
			return users.get(cookie);
		}
		
		// gets a group name and a user, returns true if the user exists in the group. returns false otherwise.
		public boolean isUserExistsInGroup(String groupName, User u){
			return groups.get(groupName).isUserExists(u);
		}
		
		// gets a group name and a user phone, returns true if the user exists in the group. returns false otherwise.
		public boolean isUserPhoneExistsInGroup(String groupName, String phone){
			return groups.get(groupName).isUserPhoneExists(phone);
		}
		
		// gets a group name and a user, adds the user to the group with the group name.
		public void addUserToGroup(String groupName, User u){
			groups.get(groupName).addUser(u);
		}
		
		// gets a group name and a user, removes the user to the group with the group name.
		public void removeUserFromGroup(String groupName, User u){
			groups.get(groupName).removeUser(u);
			if(groups.get(groupName).noOfUsers() == 0)
				groups.remove(groupName);
		}
		
		// returns a string with the details of all of the users.
		public String getUsersList(){
			if(users.size() == 0)
				return "No Users";
			StringBuilder s = new StringBuilder();
			for(String cookieValue : users.keySet()){
				s.append(users.get(cookieValue).getUserName() +"@" + users.get(cookieValue).getPhone() + "\n");
			}
			return s.toString().trim();	
		}
		
		// returns a string with the names of all of the groups.
		public String getGroupsList(){
			if(groups.size() == 0)
				return "No Groups";
			StringBuilder s = new StringBuilder();
			for(String groupName : groups.keySet())
				s.append(groupName + ":" + groups.get(groupName).getPhonesList() + "\n");
			return s.toString().trim();	
		}
		
		// gets a group name, returns a string with all of the phones of the members of the group. 
		public String getGroupList(String groupName){
			return groups.get(groupName).getPhonesList().trim();
		}
		
		// gets the data required, send a message to the user with the data.
		public void sendMsgToUser(User u,String cookieSender, String msg){
			u.addMsg(users.get(cookieSender).getPhone(), msg);
		}
		
		// gets a group name and a msg, sends the msg to the group.
		public void sendMsgToGroup(String groupName, String msg){
			groups.get(groupName).sendMsg(groupName, msg);
		}
		
		// gets a cookie value of a user, returns a string of all the unread msgs of the user.
		public String getUserMsgs(String cookieValue){
			return users.get(cookieValue).getUnreadMsgs();
		}
		
		// gets a cookie of a user, returns true if the user exists in the data structure. returns false otherwise.
		public boolean isLoggedin(String cookieValue){
			return users.containsKey(cookieValue);
		}
}