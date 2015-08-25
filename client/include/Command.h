#ifndef _COMMAND__
#define _COMMAND__
#include <vector>
#include <string>
#include <boost/algorithm/string.hpp>
using namespace std;

class Command{
  
public:
  Command();
  std::string getReplyMsg();
  void setMsg(std::string m);
  bool isAlive();
  bool analyze();
  std::string getCookie();
  void setCookie(std::string cookieValue);
  bool isLogin();
  
  
private:
  std::string lower(std::string s);
  std::string msg;
  std::string reply_msg;
  std::string cookie;
  bool is_alive;
  bool login(vector<string> strs);
  bool logout(vector<string> strs);
  bool send(vector<string> strs);
  bool remove(vector<string> strs);
  bool add(vector<string> strs);
  bool creategroup(vector<string> strs);
  bool exit(vector<string> strs);
  bool list(vector<string> strs);
};

#endif