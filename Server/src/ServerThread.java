import java.io.*;
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
        InputStream inputStream = clientSocket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line=reader.readLine()) != null){
            if ("quit".equalsIgnoreCase(line)) {
                break;
            }
            String typedLine = "You typed: " + line + "\n";
            outputStream.write(typedLine.getBytes());
        }
        clientSocket.close();
    }
}
