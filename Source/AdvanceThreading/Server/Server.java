package AdvanceThreading.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private ServerSocket serverSocket;
    private Socket socket;

    private DataInputStream messageFromClientToHandler;
    private DataOutputStream messageFromHandlerToRecipient;

    private int port;
    private static int userID;
    private ArrayList<ClientHandler> clientList;

    public Server(int port) {
        this.port = port;
        this.userID = 0;
        clientList = new ArrayList<>();
        this.initialize();
    }

    public void initialize() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                socket = serverSocket.accept();

                messageFromClientToHandler = new DataInputStream(socket.getInputStream());
                messageFromHandlerToRecipient = new DataOutputStream(socket.getOutputStream());

                String userName = messageFromClientToHandler.readUTF();

                messageFromHandlerToRecipient.writeInt(userID);

                ClientHandler client = new ClientHandler(userID, userName, this, socket, messageFromClientToHandler, messageFromHandlerToRecipient);

                clientList.add(client);

                client.start();

                userID = userID + 1;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public Socket getSocket() {
        return socket;
    }

    public DataInputStream getMessageFromClientToHandler() {
        return messageFromClientToHandler;
    }

    public DataOutputStream getMessageFromHandlerToRecipient() {
        return messageFromHandlerToRecipient;
    }

    public int getPort() {
        return port;
    }

    public static int getUserID() {
        return userID;
    }

    public ArrayList<ClientHandler> getClientList() {
        return clientList;
    }

    public static void main(String[] args) {
        Server server = new Server(5050);
    }
}
