package ru.hldspm.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**Starts repository server*/
public class RepoServer {

    /**Starts server*/
    public static void startServer(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("[HLDS:PM] Repository is running on port:  " + port);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted connection from " + clientSocket.getInetAddress());
            handleClient(clientSocket);
        }
    }

    /**Handles the client connection*/
    private static void handleClient(Socket clientSocket) {
        try {
            InputStream input = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            StringBuilder sb = new StringBuilder();
            int data;
            while ((data = input.read()) != -1) {
                char character = (char) data;
                sb.append(character);
                if (sb.toString().endsWith("\n")) {
                    break;
                }
            }

            String receivedData = sb.toString().trim();
            System.out.println("Received: " + receivedData);

            if ("alive".equals(receivedData)) {
                writer.println(true);
                System.out.println("Sent response: ALIVE");
            } else if ("fetchgold".equals(receivedData)) {
                System.out.println();
                writer.println("Coming soon...");
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
