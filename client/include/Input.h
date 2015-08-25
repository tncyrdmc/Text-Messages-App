#ifndef _INPUT__
#define _INPUT__

#include "ConnectionHandler.h"
#include "Command.h"
#include "HttpMsg.h"
#include <iostream>
#include <boost/thread.hpp>
#include <boost/date_time.hpp>
#include <string>
#include <iostream>
#include <stdlib.h>
#include <boost/locale.hpp>
#include <boost/algorithm/string.hpp>

using namespace std;
using namespace boost::algorithm;

class Input{
  
public:
  Input(ConnectionHandler *c,Command *com);
  void operator()();
  
private:
  ConnectionHandler *connectionHandler;
  Command *command;
 
};

#endif