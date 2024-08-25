# Java Selectors scenario
: use of
To manage multiple input channels/Selectors, especially in Java, 
I/O in network programming and
NIO is part of the Selectors library. The output does not require multiple
threads to be used
Such as reading, writing or connecting to channels to I/O and allow you to events
manage This approach is for programs that need to be asynchronous
Simultaneous management of a large number of connections 
(such as chat or web servers) is very practical
is

## Scenario:
Suppose you want to create a simple chat server that can connect to multiple users at the same time and
Create (Thread) to handle their messages. Instead of one thread for each user
Use it to get better performance. Non-blocking I/O and Selectors can be from
## Purpose:
To manage several communication channels (silent) at the same time in Selectors use
A simple chat server.
### Sample code:
Connection of clients (on a server) SocketChannel to manage multiple Selectors in this example
Chat is used.