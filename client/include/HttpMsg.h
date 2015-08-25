#ifndef _HTTP_MSG__
#define _HTTP_MSG__

#include <string>
#include <vector>

using namespace std;

class HttpMsg{
  
public:
  HttpMsg();
  static string getValidMsgHttpFormat(string request_type, string request_uri);
  static string getLoginBody(string username,string phone);
  static string getValidMsg(string request_type, string request_uri, string msgBody,string cookie);
  static string getQueueMsg(string cookie);
  static string getMsgBody(string s,string delimiter);
  static string getString(std::vector<std::string> v, std::string delimiter, int min_index);
};
 
#endif