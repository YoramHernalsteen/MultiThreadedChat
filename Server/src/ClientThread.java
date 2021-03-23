import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ClientThread extends Thread {
    private final Socket clientSocket;
    private String user;
    private final ServerThread serverThread;
    private OutputStream outputStream;
    private HashSet<String> topicSet = new HashSet<>();

    public ClientThread(ServerThread serverThread, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.serverThread = serverThread;
    }

    @Override
    public void run() {
        try {
            handleConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleConnection() throws IOException, InterruptedException {
        this.outputStream = clientSocket.getOutputStream();
        InputStream inputStream = clientSocket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        loop: while ((line = reader.readLine()) != null) {
            String[] input = line.split(" ");
            if (input.length > 0) {
                String command = input[0];
                switch (command) {
                    case "quit":
                    case "logout":
                        handleLogout();
                        break loop;
                    case "login":
                        handleLogin(input);
                        break;
                    case "m":
                    case "msg":
                    case "message":
                        String [] messageCommand = line.split(" ",3);
                        if(messageCommand.length>2){
                            sendMessage(messageCommand);
                        } else{
                            send("message not formatted properly \r\n");
                        }
                        break;
                    case "join":
                        if(input.length>1){
                            joinTopic(input);
                        } else{
                            send("topic join not formatted properly \r\n");
                        }
                        break;
                    case "leave":
                        if(input.length>1){
                            leaveTopic(input);
                        } else{
                            send("topic leave not formatted properly \r\n");
                        }
                        break;
                    default:
                        this.outputStream.write(("unknown: " + command + "\r\n").getBytes());
                        break;
                }
            }
        }
    }

    private void leaveTopic(String[] input) {
        String topic = input[1];
        this.topicSet.remove(topic);
    }

    private boolean isMemberOfTopic(String topic){
        return this.topicSet.contains(topic);
    }
    private void joinTopic(String[] input) {
        String topic = input[1];
        this.topicSet.add(topic);
    }

    private void sendMessage(String[] messageCommand) throws IOException {
        String receiver = messageCommand[1];
        String message = messageCommand[2];
        List<ClientThread>clientThreadList = this.serverThread.getClientThreadList();
        for(ClientThread clientThread : clientThreadList){
            if(receiver.charAt(0) == '#'){
                if(clientThread.isMemberOfTopic(receiver)){
                    clientThread.send(this.getUser() + " to "+ receiver + ": " + message + "\r\n");
                }

            } else{
                if(receiver.equals(clientThread.getUser())){
                    clientThread.send(this.getUser() + ": " + message + "\r\n");
                }
            }

        }
    }

    private void handleLogout() throws IOException {
        this.serverThread.removeClientThread(this);
        ArrayList<ClientThread> clientThreadList = serverThread.getClientThreadList();
        for(ClientThread clientThread: clientThreadList){
            if(!this.user.equals(clientThread.getUser())){
                clientThread.send(this.user+" is now offline \r\n");
            }
        }
        clientSocket.close();
    }

    /*
    * After successful login, all clients will get notified. This client will get notified about the existence of the other clients.
    * */
    private void handleLogin( String[] input) throws IOException {
        if (input.length > 2) {
            String userToCheck = input[1];
            String password = input[2];
            if ((userToCheck.equals("guest") && password.equals("guest")) ||
                    (userToCheck.equals("tim") && password.equals("tim")) ||
                    (userToCheck.equals("tom") && password.equals("tom"))
            ) {
                this.outputStream.write(("login ok \r\n").getBytes());
                this.user = userToCheck;
                ArrayList<ClientThread> clientThreadList = serverThread.getClientThreadList();
                for(ClientThread clientThread: clientThreadList){
                    if(!this.user.equals(clientThread.getUser())){
                        if(clientThread.getUser() != null){
                          send(clientThread.getUser() + " is online \r\n");
                        }
                        clientThread.send(this.user+" is now online \r\n");
                    }
                }
            } else {
                this.outputStream.write(("login failed for user:" + userToCheck + " with password:" + password + " \r\n").getBytes());
            }
        }
    }

    private void send(String message) throws IOException {
        if(this.user != null){
            this.outputStream.write(message.getBytes());
        }
    }

    public String getUser(){
        return this.user;
    }
}
