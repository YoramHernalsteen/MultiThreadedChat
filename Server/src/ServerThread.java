import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private final Socket clientSocket;
    private String user;

    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
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
        OutputStream outputStream = clientSocket.getOutputStream();
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
                        handleLogin(outputStream, input);
                        break;
                    default:
                        outputStream.write(("unknown: " + command + "\r\n").getBytes());
                        break;
                }
            }
        }
        clientSocket.close();
    }

    private void handleLogin(OutputStream outputStream, String[] input) throws IOException {
        if (input.length > 2) {
            String userToCheck = input[1];
            String password = input[2];
            if ((userToCheck.equals("guest") && password.equals("guest")) ||
                    (userToCheck.equals("tim") && password.equals("tim")) ||
                    (userToCheck.equals("tom") && password.equals("tom"))
            ) {
                outputStream.write((userToCheck + " logged in! \r\n").getBytes());
                this.user = userToCheck;
            } else {
                outputStream.write(("login failed for user:" + userToCheck + " with password:" + password + " \r\n").getBytes());
            }
        }
    }
}
