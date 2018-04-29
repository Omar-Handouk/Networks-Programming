package BasicSockets.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket; //Client Socket, used to establish a connection to server by server's IP and Port
    private Scanner userInput; //User input via keyboard
    private DataInputStream remoteInput; //server response to client queries
    private DataOutputStream channelInput; //Stream used to transmit user's input to communication channel between him and server

    public Client(String remoteIP, int port) {

        //Establishing Connection
        System.err.println(">> Attempting to establish connection.............");
        try {

            socket = new Socket(remoteIP, port); //Connect to server
            System.err.println(">> Connection Established");
            userInput = new Scanner(System.in);
            remoteInput = new DataInputStream(socket.getInputStream()); //Channel's output to current Client
            channelInput = new DataOutputStream(socket.getOutputStream()); //Client's Input to channel

        } catch (IOException error) {
            error.printStackTrace();
        }

        System.err.println("---------------------------------------------------------------------------------");

        String userMessage = "";

        while (!userMessage.equals("quit")) {
            try {
                System.err.println(">> ");
                userMessage = userInput.nextLine(); //Client's Message to be transmitted
                channelInput.writeUTF(userMessage); //Transmission of message to the channel

                System.out.println("Happy Trigger: " + remoteInput.readUTF()); //Remote Response
            } catch (IOException error) {
                error.printStackTrace();
            }
        }

        //Quit Condition has been reached..Connection Termination
        System.err.println(">> Attempting to terminate connection to server");
        try {
            socket.close();
            userInput.close();
            remoteInput.close();
            channelInput.close();
        } catch (IOException error) {
            error.printStackTrace();
        }

        System.err.println(">> Connection Terminated");
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 5050);
    }
}