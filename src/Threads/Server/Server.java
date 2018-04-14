package Threads.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private Socket socket;

    private DataInputStream userInput;
    private DataOutputStream serverResponse;

    public Server(int port) {

        System.out.println("<<<Server Initialization>>>");

        System.out.println("Attempting to establish a server....");

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Server established successfully");
        System.out.println("--------------------------------------------------");

        while (true) {

            System.out.println("Awaiting new client....");

            socket = null; //Just to ensure that a new Socket is created

            try {

                socket = serverSocket.accept();

                System.out.println("Client connected\n" +
                        "Client connection info: " + socket);

                userInput = new DataInputStream(socket.getInputStream());
                serverResponse = new DataOutputStream(socket.getOutputStream());

                System.out.println("Attempting to create a new client thread....");

                Thread client = new ClientHandler(userInput, serverResponse, socket);

                client.start();

                System.out.println("Thread created successfully");
                System.out.println("--------------------------------------------------");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server(5050);
    }
}
