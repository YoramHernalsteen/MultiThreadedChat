import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread extends Thread {
    private final Socket clientSocket;
    private String user;
    private final ServerThread serverThread;
    private OutputStream outputStream;

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
        while ((line = reader.readLine()) != null) {
            String[] input = line.split(" ");
            if (input.length > 0) {
                String command = input[0];
                switch (command) {
                    case "quit":
                        break;
                    case "login":
                        handleLogin(input);
                        break;
                    default:
                        this.outputStream.write(("unknown: " + command + "\r\n").getBytes());
                        break;
                }
            }
        }
        clientSocket.close();
    }

    private void handleLogin( String[] input) throws IOException {
        if (input.length > 2) {
            String userToCheck = input[1];
            String password = input[2];
            if ((userToCheck.equals("guest") && password.equals("guest")) ||
                    (userToCheck.equals("tim") && password.equals("tim")) ||
                    (userToCheck.equals("tom") && password.equals("tom"))
            ) {
                this.outputStream.write((userToCheck + " logged in! \r\n").getBytes());
                this.user = userToCheck;
                ArrayList<ClientThread> clientThreadList = serverThread.getClientThreadList();
                for(ClientThread clientThread: clientThreadList){
                    clientThread.sendNotification(this.user+" is now online \r\n");
                }
            } else {
                this.outputStream.write(("login failed for user:" + userToCheck + " with password:" + password + " \r\n").getBytes());
            }
        }
    }

    private void sendNotification(String message) throws IOException {
        this.outputStream.write(message.getBytes());
    }

    public String getUser(){
        return this.user;
    }
}
