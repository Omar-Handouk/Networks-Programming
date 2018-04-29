package AdvanceThreading.Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;

    private Scanner clientInput;
    private DataInputStream toReceive;
    private DataOutputStream toSend;

    private String IP_Address;
    private int port;

    private String userName;
    private int clientID;

    public Client(String IP_Address, int port, String userName) {
        this.IP_Address = IP_Address;
        this.port = port;
        this.userName = userName;
        this.initialize();
    }

    private void initialize() {
        try {
            socket = new Socket(IP_Address, port);

            clientInput = new Scanner(System.in);

            //Remove Buffered Streams if needed

            toReceive = new DataInputStream(socket.getInputStream());
            toSend = new DataOutputStream(socket.getOutputStream());

            toSend.writeUTF(userName); //Initially send the Client's name
            clientID = toReceive.readInt();

        } catch (IOException e) {
            e.printStackTrace();
        }


        SendMessage sendMessage = new SendMessage(clientInput, toSend);
        ReceiveMessage receiveMessage = new ReceiveMessage(toReceive);

        sendMessage.start();
        receiveMessage.start();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Socket getSocket() {
        return socket;
    }

    public Scanner getClientInput() {
        return clientInput;
    }

    public DataInputStream getToReceive() {
        return toReceive;
    }

    public DataOutputStream getToSend() {
        return toSend;
    }

    public String getIP_Address() {
        return IP_Address;
    }

    public int getPort() {
        return port;
    }

    public int getClientID() {
        return clientID;
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 5050, "Jupiter");
    }
}
