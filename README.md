# MultiThreadedChat
A multithreaded chat desktop application.

## How does it work? 
For now, we use telnet to check wether clients can connect to the server. Each client runs on a separate thread. This way we can handle multiple clients at the same time.

Upon logging in each existing client of the server will get notified of the new user.

As of right now three accounts exist: 
* guest - guest
* tim - tim
* tom - tom

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
##### Join topic
join \<topic> where topic always starts with prefix #
##### Leave topic
leave \<topic> where topic always starts with prefix #
##### Send a message to all users subscribed to a certain topic
m \<topic> \<message> where topic always starts with prefix #

msg \<topic> \<message> where topic always starts with prefix #

message \<topic> \<message> where topic always starts with prefix #
