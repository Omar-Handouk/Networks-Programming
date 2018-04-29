package AdvanceThreading.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientHandler extends Thread {

    private int clientID;
    private String clientName;
    private boolean loggedIn;

    private Socket socket;

    private Server server;

    private DataInputStream messageFromClientToHandler;
    private DataOutputStream messageFromHandlerToRecipient;


    public ClientHandler(int clientID, String clientName, Server server, Socket socket, DataInputStream messageFromClientToHandler, DataOutputStream messageFromHandlerToRecipient) {
        this.clientID = clientID;
        this.clientName = clientName;
        this.loggedIn = true;
        this.server = server;
        this.socket = socket;
        this.messageFromClientToHandler = messageFromClientToHandler;
        this.messageFromHandlerToRecipient = messageFromHandlerToRecipient;
    }

    @Override
    public void run() {

        String receivedFromClient;

        while (true) {
            try {
                receivedFromClient = messageFromClientToHandler.readUTF();

                System.out.println(receivedFromClient);

                StringTokenizer tokenize = new StringTokenizer(receivedFromClient, "#");
                String recipient = tokenize.nextToken();
                int recipientID = Integer.parseInt(tokenize.nextToken());
                String message = tokenize.nextToken();

                //Need to handle exception of client not found.

                for (ClientHandler client : server.getClientList()) {
                    if (client.getClientID() == recipientID && client.isLoggedIn()) {
                        client.getMessageFromHandlerToRecipient().writeUTF(this.clientName + ": " + message);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public int getClientID() {
        return clientID;
    }

    public Socket getSocket() {
        return socket;
    }

    public Server getServer() {
        return server;
    }

    public DataInputStream getMessageFromClientToHandler() {
        return messageFromClientToHandler;
    }

    public DataOutputStream getMessageFromHandlerToRecipient() {
        return messageFromHandlerToRecipient;
    }
}
