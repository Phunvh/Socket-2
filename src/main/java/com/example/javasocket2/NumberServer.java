package com.example.javasocket2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class NumberServer {
    public static void main(String[] args) {
        final int PORT = 12345;

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running...");

            int number = 1;
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                while (number <= 1000) {
                    out.println(number);
                    number++;
                    Thread.sleep(1000);
                }

                clientSocket.close();
                System.out.println("Client disconnected.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}