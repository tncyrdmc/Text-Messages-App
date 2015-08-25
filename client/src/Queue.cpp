#include "../include/Queue.h"
#include "../include/ConnectionHandler.h"
#include "../include/HttpMsg.h"
#include <boost/thread/thread.hpp>

// Constructor
Queue::Queue(ConnectionHandler *c,Command *com):connectionHandler(c),command(com){}

// an infinite loop (until the client exits) that sends and gets queue messages from and to the server.
void Queue::operator()(){
    string line;
    
    while (1) {
	boost::this_thread::sleep( boost::posix_time::seconds(2) );
	
	if(command->isLogin()){
	
	std::string reply_msg = HttpMsg::getQueueMsg(command->getCookie());
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
        string msg_body = HttpMsg::getMsgBody(answer+"\n\n","\n\n");
	if(msg_body != "No new messages")
	  std::cout << "< " << msg_body << "\n";
        
	}
        
	if(!(command->isAlive())){
	  std::string exit_msg = "exit$";
	  if (!connectionHandler->sendLine(exit_msg)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
	  }
	  break;
	}
    }
}
 
