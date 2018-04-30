package AdvanceThreading.Server;

import Exceptions.NonExistentClientException;
import Exceptions.OfflineClientException;
import Exceptions.WrongFormatException;

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

        try {
            this.messageFromHandlerToRecipient.writeUTF("" + clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String receivedFromClient;

        while (true) {
            try {
                receivedFromClient = messageFromClientToHandler.readUTF();

                System.err.println(receivedFromClient);

                if (!receivedFromClient.matches(".*\\S.*#\\d+#.*\\S.*"))
                    throw new WrongFormatException("Message Format is Incorrect");

                StringTokenizer tokenize = new StringTokenizer(receivedFromClient, "#");

                /* TODO Utilizing recipient name for message transmission
                   TODO Confirming User Information with both User name and User ID
                 */

                String recipient = tokenize.nextToken();
                int recipientID = Integer.parseInt(tokenize.nextToken());
                String message = tokenize.nextToken();

                boolean clientFound = false;

                for (ClientHandler client : server.getClientList()) {
                    if (client.getClientID() == recipientID) {
                        if (client.isLoggedIn()) {
                            client.getMessageFromHandlerToRecipient().writeUTF(this.clientName + ": " + message);
                            clientFound = true;
                            break;
                        } else
                            throw new OfflineClientException("Client is Offline");
                    }
                }

                if (!clientFound)
                    throw new NonExistentClientException("Client Does Not Exist");

                receivedFromClient = null;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (OfflineClientException e) {
                e.printStackTrace();
            } catch (NonExistentClientException e) {
                e.printStackTrace();
            } catch (WrongFormatException e) {
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
