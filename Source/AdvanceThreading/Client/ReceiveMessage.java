package AdvanceThreading.Client;

import java.io.DataInputStream;
import java.io.IOException;

public class ReceiveMessage implements Runnable { // TODO Replace Thread extension to Runnable Implementation

    private DataInputStream toReceive;

    public ReceiveMessage(DataInputStream toReceive) {
        this.toReceive = toReceive;
    }

    @Override
    public void run() {

        while (true) {
            try {
                String message = toReceive.readUTF();
                System.out.println(message); //Check if it prints in the client
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public DataInputStream getToReceive() {
        return toReceive;
    }
}
