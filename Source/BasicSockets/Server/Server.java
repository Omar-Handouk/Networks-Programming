package BasicSockets.Server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket; //Server socket is used as a generic listener to any client wishing to connect to the server
    private Socket socket; //socket used as the other terminal in the communication channel between client and server
    private DataInputStream serverResponse; //Server's input
    private DataInputStream channelOutput; //Client's input stream via channel
    private DataOutputStream channelInput; //stream used to transmit server's response via comm channel

    public Server(int port) {

        //Establishing Server
        System.err.println(">> Attempting to create portal.................................");
        try {
            serverSocket = new ServerSocket(port); //Initiating server socket
            System.err.println(">> Awaiting Connection");
            socket = serverSocket.accept(); // Accepting client
            System.err.println(">> Connection Established");
            serverResponse = new DataInputStream(System.in);
            channelOutput = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            channelInput = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.err.println("---------------------------------------------------------------------------------");

        String clientMessage = "";


        while (!clientMessage.equals("quit")) {
            try {
                System.err.println(">> ");
                clientMessage = channelOutput.readUTF();
                System.out.println("John Doe: " + clientMessage);
                channelInput.writeUTF(serverResponse.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        System.err.println(">> Attempting to terminate connection to server");

        try {
            serverSocket.close();
            socket.close();
            serverResponse.close();
            channelOutput.close();
            channelInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.err.println(">> Connection Terminated");
    }

    public static void main(String[] args) {
        Server server = new Server(5050);
    }
}