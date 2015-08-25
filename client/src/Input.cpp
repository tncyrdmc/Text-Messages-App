#include "../include/Input.h"
#include "../include/ConnectionHandler.h"

// Constructor
Input::Input(ConnectionHandler *c,Command *com):connectionHandler(c),command(com){}

// an infinite loop (until the client exits) that sends and gets messages from and to the server.
void Input::operator()(){
    string line;
    
    while (1) {
      const short bufsize = 1024;
      char buf[bufsize];
      std::cin.getline(buf, bufsize);
      std::string line(buf);
      int len=line.length();
      trim(line);
      if(len == 0)
	continue;
	
	
      command->setMsg(line);
      if(!command->analyze()){
	std::cout << command->getReplyMsg() << "\n";
	continue;
      }
	
    std::string reply_msg = command->getReplyMsg();
    if (reply_msg != "" && !connectionHandler->sendLine(reply_msg)) {
      std::cout << "Disconnected. Exiting...\n" << std::endl;
      break;
    }
        
    std::string answer;
    if (!connectionHandler->getLine(answer)) {
      std::cout << "Disconnected. Exiting...\n" << std::endl;
      break;
    }
        
        answer = ConnectionHandler::url_decode(answer);
	trim(answer);
	
	std::vector<std::string> strs;
	boost::split(strs,answer,boost::is_any_of("\n"));
	if ((strs.size()>1) && ((strs.at(1)).length()>10) && (strs.at(1).substr(0,10) == "Set-Cookie")) {
	  std::vector<std::string> cookieStr;
	  boost::split(cookieStr,strs.at(1),boost::is_any_of("="));
	  command->setCookie(cookieStr.at(1));
	}
	
	string msg_body = HttpMsg::getMsgBody(answer+"\n\n","\n\n");
	if(msg_body != "")
	  std::cout << "< " << msg_body << "\n";
        
	if(!(command->isAlive())){
	  break;
	}
    }
}
