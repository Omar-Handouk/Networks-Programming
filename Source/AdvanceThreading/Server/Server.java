package AdvanceThreading.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Server {

    private ServerSocket serverSocket;
    private Socket socket;

    private DataInputStream messageFromClientToHandler;
    private DataOutputStream messageFromHandlerToRecipient;

    private int port;
    private static int userID;
    private ArrayList<ClientHandler> clientList; // TODO Replace ArrayList with HashMap for further improvement in User search Speed

    private final DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
    private final DateFormat time = new SimpleDateFormat("hh:mm:ss");
    private Date DoT;

    private FileWriter serverLogs; // TODO LOGS
    private PrintWriter writer;

    public Server(int port) { // TODO Handle FileWriter IOException
        this.port = port;
        this.userID = 0;
        clientList = new ArrayList<>();
        this.DoT = new Date();
        this.initialize();
    }

    public void initialize() {

        System.out.println("Initializing server.....\nEstablishing server portal...");

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Server initialized successfully");

        while (true) {
            try {

                System.out.println("Awaiting new client");

                socket = serverSocket.accept();

                messageFromClientToHandler = new DataInputStream(socket.getInputStream());
                messageFromHandlerToRecipient = new DataOutputStream(socket.getOutputStream());

                String userName = messageFromClientToHandler.readUTF();

                messageFromHandlerToRecipient.writeInt(userID);

                ClientHandler client = new ClientHandler(userID, userName, this, socket, messageFromClientToHandler, messageFromHandlerToRecipient);

                Thread clientThread = new Thread(client);

                clientList.add(client);

                clientThread.start();

                userID = userID + 1;

                System.out.println(">>>New Client connected: " + socket + "\n"
                        + "Time of Connection: " + time.format(DoT) + "\n"
                        + "Date of Connection: " + date.format(DoT) + "\n"
                        + "User Info: " + client.toString() + "\n");

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
