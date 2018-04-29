package Threads.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientHandler extends Thread {

    private final DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
    private final DateFormat time = new SimpleDateFormat("hh:mm:ss");

    private final DataInputStream userQuery;
    private final DataOutputStream serverResponse;

    private final Socket socket;


    public ClientHandler(DataInputStream userQuery, DataOutputStream serverResponse, Socket socket) {
        this.userQuery = userQuery;
        this.serverResponse = serverResponse;
        this.socket = socket;
    }

    @Override
    public void run() {

        String query;
        String answer;

        while (true) {
            try {
                serverResponse.writeUTF("What would you like to query [Date | Time} ?\n" +
                        "Type quit to terminate connection");

                query = userQuery.readUTF();

                if ((query.toLowerCase()).equals("quit"))
                    break;

                Date DoT = new Date();

                switch (query.toLowerCase()) {
                    case "date":
                        answer = date.format(DoT);
                        serverResponse.writeUTF(answer);
                        break;
                    case "time":
                        answer = time.format(DoT);
                        serverResponse.writeUTF(answer);
                        break;
                    default:
                        serverResponse.writeUTF("Invalid query, please input a correct choice !");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            socket.close();
            userQuery.close();
            serverResponse.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
