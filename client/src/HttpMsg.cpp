#include "../include/HttpMsg.h"
#include "../include/ConnectionHandler.h"

// Constructor
HttpMsg::HttpMsg(){}

// Gets the data required, returns a string with a msg of the HTTP protocol format.
  string HttpMsg::getValidMsgHttpFormat(string request_type, string request_uri){
   string str = request_type + " " + request_uri + " HTTP/1.1"; 
   return str;
  }
  
  // gets a user name and a phone and returns a string with the msg body of the login format. 
  string HttpMsg::getLoginBody(string username,string phone){
    string str = "UserName=" + username + "&Phone=" + phone;
    return str;
  }
  
  // Gets the data required, returns a string with a msg of the HTTP protocol format.
  string HttpMsg::getValidMsg(string request_type, string request_uri, string msgBody,string cookie){
    string header = "";
    if(cookie != "")
      header = "\nCookie: user_auth=" + cookie;
    string str = HttpMsg::getValidMsgHttpFormat(request_type,request_uri) + header + "\n\n" + ConnectionHandler::url_encode(msgBody) + "\n$";
    return str;
}
 
 // Gets the data required, returns a string with a msg of the HTTP protocol format.
 string HttpMsg::getQueueMsg(string cookie){
   string str = HttpMsg::getValidMsg("GET","/queue.jsp","",cookie);
   return str;
 }
 
 // gets a string and a delimiter, returns the msg body of the string.
 string HttpMsg::getMsgBody(string s,string delimiter){
  size_t pos = 0;
  std::string token;
  vector<string> v;

  while ((pos = s.find(delimiter)) != std::string::npos) {
      token = s.substr(0, pos);
      v.push_back(token);
      s.erase(0, pos + delimiter.length());
  }
  if(v.size() == 2)
    return (v.at(1)).substr(0,v.at(1).length()-2);
  else
    return "";
}

// gets a vector and other data, returns a string with the vector contents.
string HttpMsg::getString(std::vector<std::string> v, std::string delimiter, int min_index){
    std::string str = "";
    for(unsigned int i=min_index;i<v.size();i++)
      str = str + v.at(i) + delimiter;
    return str;
}