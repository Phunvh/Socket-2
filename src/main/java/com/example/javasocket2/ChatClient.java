package com.example.javasocket2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter your usernamerr:");
            String username = consoleIn.readLine();

            out.println(username);

            Thread messageThread = new Thread(new MessageHandler(in));
            messageThread.start();

            String message;
            while ((message = consoleIn.readLine()) != null) {
                out.println(message);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class MessageHandler implements Runnable {
        private BufferedReader in;

        public MessageHandler(BufferedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

