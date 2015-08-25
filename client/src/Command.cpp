#include "../include/Command.h"
#include "../include/HttpMsg.h"
#include <iostream>
#include <string>
#include <locale> 
#include <boost/algorithm/string.hpp> 

// Constructor
Command::Command():msg(""),reply_msg(),cookie(""),is_alive(true){}

// returns true, if the client entered "exit" in the console.
bool Command::isAlive(){
  return is_alive;
}

// returns the reply msg to send to the server.
std::string Command::getReplyMsg(){
  return reply_msg;
}

// returns the cookie of the user.
std::string Command::getCookie(){
  return cookie;
}

// SETTER for the member variable msg.
void Command::setMsg(std::string m){
  msg = m;
}

// SETTER for the member variable cookieValue.
void Command::setCookie(std::string cookieValue){
  cookie = cookieValue;
}

//changes to lower case
string Command::lower(string s){
  boost::algorithm::to_lower(s);
  return s;
}

// returns true if the client has a cookie, false otherwise.
bool Command::isLogin(){
  return cookie != "";
}

// gets a vector of the msg. returns true if the message is in the required format, false otherwise.
bool Command::login(vector<string> strs){
    if(strs.size()== 3){
      std::string username = strs.at(1);
      std::string phone = strs.at(2);
      reply_msg = HttpMsg::getValidMsg("POST","/login.jsp",HttpMsg::getLoginBody(username,phone),cookie);
      return true;
    }else{
      reply_msg = "Error 765: Cannot login, missing parameters";
      return false;
    }
    return false;
}

// gets a vector of the msg. returns true if the message is in the required format, false otherwise.
bool Command::logout(vector<string> strs){
    if(strs.size() == 1){
      reply_msg = HttpMsg::getValidMsg("GET","/logout.jsp","",cookie);
      if (cookie != ""){ // logout but not logged in
	setCookie("");
      }
      return true;
    }
    return false;
}

// gets a vector of the msg. returns true if the message is in the required format, false otherwise.
bool Command::send(vector<string> strs){
    if(strs.size()>1){
    string sendType = lower(strs.at(1));
    if(sendType == "group"){
      if(strs.size() >= 4){
	  string group_name = strs.at(2);
	  string msg = HttpMsg::getString(strs," ",3);
	  reply_msg = HttpMsg::getValidMsg("POST","/send.jsp","Type=Group&Target=" + group_name + "&Content=" + msg ,cookie);
	  return true;
      }
      else{
	  reply_msg = "Error 765: missing parameters";
	  return false;
	}
    }else if(sendType == "user"){
      if(strs.size() >= 4){
	  string phone = strs.at(2);
	  string msg = HttpMsg::getString(strs," ",3);
	  reply_msg = HttpMsg::getValidMsg("POST","/send.jsp","Type=Direct&Target=" + phone + "&Content=" + msg ,cookie);
	  return true;
      }
    else{
	  reply_msg = "Error 765: missing parameters";
	  return false;
	}
      }
    }
    return false;
  }

// gets a vector of the msg. returns true if the message is in the required format, false otherwise.
bool Command::remove(vector<string> strs){
     if(strs.size()<=3){
	if(strs.size()==3){
	  string group_name = strs.at(1);
	  string phone = strs.at(2);
	  reply_msg = HttpMsg::getValidMsg("POST","/remove_user.jsp","Target=" + group_name + "&User=" + phone ,cookie);
	  return true;
      }
      else{
	reply_msg = "Error 765: missing parameters";
	return false;
      }
    }
    return false;
}

// gets a vector of the msg. returns true if the message is in the required format, false otherwise.
bool Command::add(vector<string> strs){
    if(strs.size()<=3){
      if(strs.size()==3){
	string group_name = strs.at(1);
	string phone = strs.at(2);
	reply_msg = HttpMsg::getValidMsg("POST","/add_user.jsp","Target=" + group_name + "&User=" + phone ,cookie);
	return true;
    }
      else{
	reply_msg = "Error 765: missing parameters";
	return false;
      }
    }
    return false;
}

// gets a vector of the msg. returns true if the message is in the required format, false otherwise.
bool Command::creategroup(vector<string> strs){
    std::vector<std::string> groups;
    boost::split(groups,msg,boost::is_any_of(" "));
    if(groups.size()>2){
    std::string group_name = groups.at(1);
    std::string users = "";
    for(unsigned int i=2;i<groups.size()-1;i++)
      users = users + groups[i] + ",";
    users = users + groups[groups.size()-1];
    reply_msg = HttpMsg::getValidMsg("POST","/create_group.jsp","GroupName=" + group_name + "&Users=" + users ,cookie);
    return true;
    }
    else{
      reply_msg = "Error 765: missing parameters";
      return false;
    }
    return false;
}

// gets a vector of the msg. returns true if the message is in the required format, false otherwise.
bool Command:: exit(vector<string> strs){
     if(strs.size()==1){
      if(cookie != ""){ // not yet logout
	reply_msg = "Please logout first";
	return false;
      }
      reply_msg = "exit$";
      is_alive = false;
      return true;
    }
     return false;
  }
 
// gets a vector of the msg. returns true if the message is in the required format, false otherwise.
bool Command::list(vector<string> strs){ 
  if(strs.size() > 1){
	  string listType = lower(strs.at(1));
	  if(listType == "groups"){
	  reply_msg = HttpMsg::getValidMsg("POST","/list.jsp","List=Groups",cookie);
	  return true;
	}else if(listType == "users"){
	  reply_msg = HttpMsg::getValidMsg("POST","/list.jsp","List=Users",cookie);
	  return true;
	}else if(listType == "group"){
	  if(strs.size()==3){
	    string group_name = strs.at(2);
	    reply_msg = HttpMsg::getValidMsg("POST","/list.jsp","List=Group&GroupName=" + group_name,cookie);
	    return true;
	  }
	  else{
	    reply_msg = "Error 765: missing parameters";
	    return false;
	  }
	}
      }
      return false;
    }
     
// gets a vector of the msg. returns true if the message is in the required format, false otherwise.
 bool Command::analyze(){
  std::vector<std::string> strs;
  boost::split(strs,msg,boost::is_any_of(" "));
  std::string command = strs.at(0);
  command = lower(command);
  reply_msg = "Error, invalid format!";
  if(command == "login")
    return login(strs);
     if(command == "logout")
      return logout(strs);
     if(command == "list")
    return (list(strs));
     if(command == "send")
      return send(strs);
      if(command == "add")
	return add(strs);
     if(command == "remove")
	return remove(strs);
     if(command == "creategroup")
      return creategroup(strs);
     if(command == "exit")
    return exit(strs);
   return false;
}
