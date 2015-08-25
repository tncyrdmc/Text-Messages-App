#ifndef _QUEUE__
#define _QUEUE__

#include "ConnectionHandler.h"
#include "Command.h"
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


class Queue{
  
public:
  Queue(ConnectionHandler *c,Command *com);
  void operator()();
  
private:
  ConnectionHandler *connectionHandler;
  Command *command;
 
};
 
#endif