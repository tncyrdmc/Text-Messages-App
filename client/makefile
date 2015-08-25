CFLAGS:=-c -Wall -Weffc++ -g
LDFLAGS:=-lboost_system -lboost_locale -lboost_thread

all: client

client: bin/HttpMsg.o bin/Input.o bin/Queue.o bin/Command.o bin/ConnectionHandler.o bin/Run.o 
	g++ -o bin/client bin/ConnectionHandler.o bin/Run.o bin/Input.o bin/Queue.o bin/Command.o bin/HttpMsg.o $(LDFLAGS) 
	@echo 'Finished the compilation process..'
	@echo 'Please enter "cd bin" and "./client [host] [port]"'

bin/ConnectionHandler.o: src/ConnectionHandler.cpp
	g++ $(CFLAGS) -o bin/ConnectionHandler.o src/ConnectionHandler.cpp

bin/Run.o: src/Run.cpp
	g++ $(CFLAGS) -o bin/Run.o src/Run.cpp
	
bin/Input.o: src/Input.cpp
	g++ $(CFLAGS) -o bin/Input.o src/Input.cpp
	
bin/Command.o: src/Command.cpp
	g++ $(CFLAGS) -o bin/Command.o src/Command.cpp

bin/HttpMsg.o: src/HttpMsg.cpp
	g++ $(CFLAGS) -o bin/HttpMsg.o src/HttpMsg.cpp
	
bin/Queue.o: src/Queue.cpp
	g++ $(CFLAGS) -o bin/Queue.o src/Queue.cpp
	
clean:
	rm -f bin/*
