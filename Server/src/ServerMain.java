import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
    Main thread will handle accept, client connections will be handled by other thread.
    Every client connection is handled by a thread
*/

public class ServerMain {
    public static void main(String[] args) {
        int port = 8088;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(clientSocket);
                serverThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            {

            }
        }
    }
}
