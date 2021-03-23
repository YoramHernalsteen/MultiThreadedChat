# MultiThreadedChat
A multithreaded chat desktop application.

## How does it work? 
For now, we use telnet to check wether clients can connect to chat serverThread. Each client runs on a separate thread. This way we can handle multiple clients at the same time.

Upon logging in each existing client of the server will get notified of the new user.

### Commands
##### Login
login \<user> \<password>
##### Quit
logout
quit
##### Send a message to a single user
m \<receiver> \<message>
msg \<receiver> \<message>
message \<receiver> \<message>

