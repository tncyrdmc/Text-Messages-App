package protocol_whatsapp;

import java.util.ArrayList;
import protocol.ServerProtocol;
import protocol_http.HttpProtocol;
import Whatsapp.User;
import Whatsapp.Whatsapp;

public class WhatsAppProtocol implements ServerProtocol<String> {
	private Whatsapp whatsapp;
	private HttpProtocol h;

	// Constructor
	public WhatsAppProtocol(){
		whatsapp = new Whatsapp();
		h = new HttpProtocol();
	}

	@Override
	// gets a message, creating a reply message and returns it.
	public String processMessage(String msg){
		msg = msg.trim();
		if(msg.equals("exit"))
			return "exit$";
		if(h.processMessage(msg) != null)
			return h.processMessage(msg);
		String[] str = msg.split("\n");
		String ans = null;
		String msgBody = h.getMsgBody(msg);
		String cookieValue = "";
		if(str.length >= 2)
			cookieValue = h.getCookieValue(str[1]);
			if(h.getUriMsg(str[0]).equals("login.jsp")){
				ans = login(msgBody,cookieValue);
			}else{
				if(!whatsapp.isLoggedin(cookieValue))
					return h.createHttpMsgWithoutHeader(403,"Error 403: Forbidden");
				switch(h.getUriMsg(str[0])){
				case "list.jsp":
					ans = list(msgBody);
					break;
				case "create_group.jsp":
					ans = createGroup(msgBody);
					break;
				case "send.jsp":
					ans = send(msgBody,cookieValue);
					break;
				case "add_user.jsp":
					ans = addUser(msgBody,cookieValue);
					break;
				case "remove_user.jsp":
					ans = removeUser(msgBody,cookieValue);
				break;
				case "queue.jsp":
					ans = queue(msgBody,cookieValue);
					break;
				case "logout.jsp":
					ans = logout(cookieValue);
				break;
				}
			}
		return ans.trim();
	}

	// gets a msg body and cookie, making the nessecary steps as described in the assignment and returns the reply msg.
	protected String login(String msgBody,String cookie){
		if(cookie != "")
			return h.createValidTextHttpMsg("Error 987: You already logged in, please log out first");
		if(!(msgBody.contains("UserName=") && msgBody.contains("Phone=")))
			return h.createValidTextHttpMsg("Error 765: Cannot login, missing parameters");
		String[] str = msgBody.split("&");
		String usernameStr = str[0];
		String phoneStr = str[1];
		String username = usernameStr.substring(9);
		String phone = phoneStr.substring(6);
		if(usernameStr.startsWith("UserName=") && username.length() > 0 && 
				phoneStr.startsWith("Phone=") && phone.length() > 0){
			if(whatsapp.findUserByPhone(phone) != null && (whatsapp.findUserByPhone(phone) != whatsapp.findUser(username)))
				return h.createValidTextHttpMsg("Error 852: The phone is already exists");
			String cookieValue = whatsapp.getUserCookie(username,phone);
			if(cookieValue == null){ // The user not exists
				User u = new User(phone,username);
				cookieValue = u.getHashCode(); 
				whatsapp.addUser(cookieValue, u);
			}
			return h.createHttpMsgWithCookie(200,cookieValue,"Welcome " + username + "@" + phone);	
		}
		return h.createValidTextHttpMsg("Error 765: Cannot login, missing parameters");
	}
	
	// gets a msg body and cookie, making the nessecary steps as described in the assignment and returns the reply msg.
	protected String list(String msgBody){
		String[] str = msgBody.split("=");
		String listType = str[1];
		if(!msgBody.startsWith("List="))
			return h.createValidTextHttpMsg("Error 273: missing parameters");
		if(listType.equals("Users"))
			return h.createValidTextHttpMsg(whatsapp.getUsersList());
		if(listType.equals("Groups"))
			return h.createValidTextHttpMsg(whatsapp.getGroupsList());
		if(listType.equals("Group&GroupName")){
			String[] str1 = msgBody.split("&");
			String[] groupName = str1[1].split("=");
			if(!whatsapp.isGroupExist(groupName[1]))
				return h.createValidTextHttpMsg("Error 199: Unknown group");
			return h.createValidTextHttpMsg(whatsapp.getGroupList(groupName[1]));
		}
		return h.createValidTextHttpMsg("Error 273: missing parameters");
	}

	// gets a msg body and cookie, making the nessecary steps as described in the assignment and returns the reply msg.
	protected String createGroup(String msgBody){
		String[] str = msgBody.split("&");
		String groupNameStr = str[0];
		String usersStr = str[1];
		String groupName = groupNameStr.substring(10);
		String users = usersStr.substring(6);
		if(groupNameStr.startsWith("GroupName=") && groupName.length() > 0 && 
				usersStr.startsWith("Users=") && users.length() > 0){
			String[] usersArr = users.split(",");
			if(whatsapp.isGroupExist(groupName))
				return h.createValidTextHttpMsg("Error 511: Group Name Already Taken");
			ArrayList<User> groupUsers = new ArrayList<User>();
			for(int i=0; i<usersArr.length; i++){
				User u;
				if((u = whatsapp.findUser(usersArr[i]))!=null)
					groupUsers.add(u);
				else
					return h.createValidTextHttpMsg("Error 929: Unknown User " + usersArr[i]);					
			}
			whatsapp.addGroup(groupName, groupUsers);
			return h.createValidTextHttpMsg("Group " + groupName + " Created");

		}else{
			return h.createValidTextHttpMsg("Error 675: Cannot create group, missing parameters");	
		}
	}
	
	// gets a msg body and cookie, making the nessecary steps as described in the assignment and returns the reply msg.
	protected String send(String msgBody, String cookieValue){
		String[] str = msgBody.split("&");
		if(!(str.length == 3))
			return h.createValidTextHttpMsg("Error 711: Cannot send, missing parameters");
		String typeStr = str[0];
		String targetStr = str[1];
		String contentStr = str[2];
		String type = (typeStr.split("="))[1];
		String target = (targetStr.split("="))[1];
		String content = (contentStr.split("="))[1];
		if(!(typeStr.startsWith("Type=")) || !(targetStr.startsWith("Target=")) || !(contentStr.startsWith("Content=")))
			return h.createValidTextHttpMsg("Error 711: Cannot send, missing parameters");
		if(type.equals("Direct")){
			User u;
			if((u = whatsapp.findUserByPhone(target)) != null){
				whatsapp.sendMsgToUser(u, cookieValue, content);
				return h.createValidTextHttpMsg("Message sent to the user");
			}else
				h.createValidTextHttpMsg("Error 771: Target Does not Exist");
		}else if(type.equals("Group")){
			if(whatsapp.isGroupExist(target)){
				whatsapp.sendMsgToGroup(target, content);
				return h.createValidTextHttpMsg("Message sent to the group");
			}else
				return h.createValidTextHttpMsg("Error 771: Target Does not Exist");
		}
		return h.createValidTextHttpMsg("Error 836: Invalid Type");	
	}

	// gets a msg body and cookie, making the nessecary steps as described in the assignment and returns the reply msg.
	protected String addUser(String msgBody,String cookieValue){
		String[] str = msgBody.split("&");
		String targetStr = str[0];
		String userStr = str[1];
		String groupName = targetStr.substring("Target=".length());	
		String phone = userStr.substring("User=".length());

		if(!(targetStr.startsWith("Target=") && groupName.length() > 0 && 
				userStr.startsWith("User=") && phone.length() > 0))
			return h.createValidTextHttpMsg("ERROR 242: Cannot add user, missing parameters");
		
		
		if(!whatsapp.isGroupExist(groupName))
			return h.createValidTextHttpMsg("ERROR 770: Target Does not Exist");
		User u = whatsapp.getUser(cookieValue);
		if(!whatsapp.isUserExistsInGroup(groupName, u)) // the client's cookie
			return h.createValidTextHttpMsg("ERROR 669: Permission denied");
		if(whatsapp.isUserPhoneExistsInGroup(groupName, phone)) // the user's phone to insert
			return h.createValidTextHttpMsg("ERROR 142: Cannot add user, user already in group");
		if((u = whatsapp.findUserByPhone(phone)) == null)
			return h.createValidTextHttpMsg("ERROR 929: Unknown User " + phone);
		whatsapp.addUserToGroup(groupName, u);
		return h.createValidTextHttpMsg(phone + " added to " + groupName);
		}

	// gets a msg body and cookie, making the nessecary steps as described in the assignment and returns the reply msg.
	protected String removeUser(String msgBody,String cookieValue){
		String[] str = msgBody.split("&");
		String targetStr = str[0];
		String userStr = str[1];
		String groupName = targetStr.substring("Target=".length());	
		String phone = userStr.substring("User=".length());

		if(!(targetStr.startsWith("Target=") && groupName.length() > 0 && 
				userStr.startsWith("User=") && phone.length() > 0))
			return h.createValidTextHttpMsg("ERROR 336: Cannot remove, missing parameters");
		if(!whatsapp.isGroupExist(groupName))
			return h.createValidTextHttpMsg("ERROR 769: Target Does not Exist");
		User u = whatsapp.getUser(cookieValue);
		if(!whatsapp.isUserExistsInGroup(groupName, u)) // the client's cookie
			return h.createValidTextHttpMsg("ERROR 668: Permission denied");
		if((u = whatsapp.findUserByPhone(phone)) == null)
			return h.createValidTextHttpMsg("ERROR 929: Unknown User " + phone);
		if(!whatsapp.isUserPhoneExistsInGroup(groupName, phone)) // the user's phone to insert
			return h.createValidTextHttpMsg("ERROR 143: Cannot remove user, user is not in group");
		whatsapp.removeUserFromGroup(groupName, u);
		return h.createValidTextHttpMsg(phone + " removed from " + groupName);
	}

	// gets a msg body and cookie, making the nessecary steps as described in the assignment and returns the reply msg.
	public String queue(String msgBody,String cookieSender){
		return h.createValidTextHttpMsg(whatsapp.getUserMsgs(cookieSender));
	}

	// gets a msg body and cookie, making the nessecary steps as described in the assignment and returns the reply msg.
	public String logout(String cookieSender){
		return h.createValidTextHttpMsg("Goodbye");
	}

	@Override
	public boolean isEnd(String msg) {
		msg = msg.trim();
		if(msg.equals("exit"))
			return true;
		return false;
	}

}

