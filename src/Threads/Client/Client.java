package Threads.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;

    private Scanner userInput;
    private DataInputStream serverResponse;
    private DataOutputStream channelInput;

    public Client(String IP_Address, int port) {

        System.out.println("Attempting to establish connection to server.......");

        try {
            socket = new Socket(IP_Address, port);

            userInput = new Scanner(System.in);
            serverResponse = new DataInputStream(socket.getInputStream());
            channelInput = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Connection established successfully\n" +
                "------------------------------------------------------------");

        while (true) {
            try {
                System.out.print(">> Server: " + serverResponse.readUTF() + "\n" +
                        ">> You: ");

                String send = userInput.nextLine();

                channelInput.writeUTF(send);

                if (send.toLowerCase().equals("quit"))
                    break;

                System.out.println(">> Server: " + serverResponse.readUTF());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Attempting to terminate connection......");

        try {
            socket.close();
            userInput.close();
            serverResponse.close();
            channelInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Connection terminated");
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 5050);
    }
}
