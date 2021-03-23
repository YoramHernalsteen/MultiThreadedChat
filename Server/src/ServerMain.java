/*
    Main thread will handle accept, client connections will be handled by other thread.
    Every client connection is handled by a thread
*/

public class ServerMain {
    public static void main(String[] args) {
        ServerThread serverThread = new ServerThread(8088);
        serverThread.start();
    }
}
