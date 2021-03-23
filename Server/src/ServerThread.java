import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread {
    private final int port;
    private ArrayList<ClientThread> clientThreadList = new ArrayList<>();

    public ServerThread(int port) {
        this.port = port;
    }

    public ArrayList<ClientThread> getClientThreadList() {
        return this.clientThreadList;
    }

    @Override
    public void run(){
        try {
            ServerSocket serverSocket = new ServerSocket(this.port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientThread clientThread = new ClientThread(this,clientSocket);
                this.clientThreadList.add(clientThread);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
