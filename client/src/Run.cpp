#include "../include/ConnectionHandler.h"
#include "../include/Command.h"
#include "../include/Input.h"
#include "../include/Queue.h"
#include <string>
#include <iostream>
#include <stdlib.h>
#include <boost/locale.hpp>
#include <boost/algorithm/string.hpp>

using namespace std;
using namespace boost::algorithm;


// the main function of the application.
int main (int argc, char *argv[]) {
    if (argc < 3) { // Parameters check
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    
    cout << "Welcome to Whatsapp Client!\n";
    cout << "Please Enter A Commands to execute and press enter:\n";
     
     Command *c = new Command();
     ConnectionHandler *connectionHandler = new ConnectionHandler(host,port);
     
      if (!connectionHandler->connect()) {
	  std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
	  return 1;
      }
      cout << "Connected!\n";
      
      Queue q(connectionHandler,c);
      boost::thread workerThread(q);
     
      
      Input i(connectionHandler,c);
      boost::thread workerThread1(i);
      
      workerThread.join();
      workerThread1.join();
    
      delete c;
      delete connectionHandler;
    
      return 0;
}


