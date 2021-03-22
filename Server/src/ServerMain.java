import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/*Main thread will handle accept, client connections will be handled by other thread*/

public class ServerMain {
    public static void main(String[] args) {
        int port = 8088;
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            while(true){
                Socket clientSocket = serverSocket.accept();
                Thread threadToHandleConnections = new Thread(){
                    @Override
                    public void run(){
                        try {
                            handleConnection(clientSocket);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                };
                threadToHandleConnections.start();

            }

        } catch (IOException e) {
            e.printStackTrace();{

            }
        }


    }

    private static void handleConnection(Socket clientSocket) throws IOException, InterruptedException {
        OutputStream outputStream = clientSocket.getOutputStream();
        for(int i =0 ; i<10; i++){
            outputStream.write(("Helloo" + i + "\n").getBytes());
            Thread.sleep(1000);
        }
        clientSocket.close();
    }
}
