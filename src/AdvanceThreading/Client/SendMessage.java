package AdvanceThreading.Client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class SendMessage extends Thread {

    private Scanner clientInput;
    private DataOutputStream toSend;


    public SendMessage(Scanner clientInput, DataOutputStream toSend) {
        this.clientInput = clientInput;
        this.toSend = toSend;
    }

    @Override
    public void run() {

        while (true) {
            try {
                String message = clientInput.nextLine();
                toSend.writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Scanner getClientInput() {
        return clientInput;
    }

    public DataOutputStream getToSend() {
        return toSend;
    }
}
