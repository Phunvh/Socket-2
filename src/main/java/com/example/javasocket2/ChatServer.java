package com.example.javasocket2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
    private static final int PORT = 12345;
    private static final Map<String, PrintWriter> clients = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");

                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // Yêu cầu client nhập username
                out.println("Enter your message:");
                username = in.readLine();

                // Thêm client vào danh sách
                synchronized (clients) {
                    clients.put(username, out);
                }

                // Đọc và gửi tin nhắn từ client
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(username + ": " + message);
                    // Gửi tin nhắn tới tất cả client (trừ chính client gửi)
                    synchronized (clients) {
                        for (PrintWriter writer : clients.values()) {
                            if (writer != out) {
                                writer.println(username + ": " + message);
                            }
                        }
                    }
                }

                // Khi client ngắt kết nối, loại bỏ nó khỏi danh sách
                synchronized (clients) {
                    clients.remove(username);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

