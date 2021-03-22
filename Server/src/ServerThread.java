import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread extends Thread {
    private final Socket clientSocket;

    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run(){
        try {
            handleConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleConnection() throws IOException, InterruptedException {
        OutputStream outputStream = clientSocket.getOutputStream();
        for(int i =0 ; i<10; i++){
            outputStream.write(("Helloo" + i + "\n").getBytes());
            Thread.sleep(1000);
        }
        clientSocket.close();
    }
}
