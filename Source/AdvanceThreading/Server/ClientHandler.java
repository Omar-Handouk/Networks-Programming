package AdvanceThreading.Server;

import Exceptions.NonExistentClientException;
import Exceptions.OfflineClientException;
import Exceptions.WrongFormatException;

import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable {

    private int clientID;
    private String clientName;
    private boolean loggedIn;

    private Socket socket;

    private Server server;

    private DataInputStream messageFromClientToHandler;
    private DataOutputStream messageFromHandlerToRecipient;

    private final DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
    private final DateFormat time = new SimpleDateFormat("hh:mm:ss");
    private Date DoT;

    private FileWriter serverLogs; // TODO LOGS
    private PrintWriter writer;

    public ClientHandler(int clientID, String clientName, Server server, Socket socket, DataInputStream messageFromClientToHandler, DataOutputStream messageFromHandlerToRecipient) {
        this.clientID = clientID;
        this.clientName = clientName;
        this.loggedIn = true;
        this.server = server;
        this.socket = socket;
        this.messageFromClientToHandler = messageFromClientToHandler;
        this.messageFromHandlerToRecipient = messageFromHandlerToRecipient;
        this.DoT = new Date();
    }

    @Override
    public void run() {

        try {
            this.messageFromHandlerToRecipient.writeUTF("Your Name: " + clientName + "\nYour ID: " + clientID + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String receivedFromClient;

        while (true) {
            try {
                receivedFromClient = messageFromClientToHandler.readUTF(); //Used to receive message from client

                System.out.println(">>>LOG:\nTime: " + time.format(DoT)
                        + "\nDate: " + date.format(DoT)
                        + "\nMessage: " + receivedFromClient
                        + "\nFrom: " + this.toString());

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

    @Override
    public String toString() {
        return "Username: " + this.clientName + ", User ID: " + this.clientID;
    }
}
