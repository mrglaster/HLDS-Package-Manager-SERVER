package ru.hldspm.server;
import ru.hldspm.db.repository.GameContentRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


/**Starts repository server*/
public class RepoServer {

    /**Starts server*/
    public static void startServer(int port) throws IOException {
        try ( ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[HLDS:PM] Repository is running on port:  " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket.getInetAddress());
                handleClient(clientSocket);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**Handles the client connection*/
    private static void handleClient(Socket clientSocket) throws IOException {
        try {
            InputStream input = clientSocket.getInputStream();
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
            } else if (receivedData.contains("fetchgold")) {
                // Expected string example: fetchgold;cstrike;linux
                String[] params = receivedData.split(";");
                String fileName = "cached/" + params[1] + "_" + params[2] + ".json";
                BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
                String line;
                while ((line = fileReader.readLine()) != null) {
                    writer.println(line);
                }
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**Caches information about the content in the repository for each game and platform*/
    public static void createRepoInformation() throws IOException {
        File theDir = new File("cached/");
        if (!theDir.exists()){
            theDir.mkdirs();
            String[] supportedGames = {"valve", "cstrike", "dod", "czero", "tfc", "ts"};
            for (var i : supportedGames){
                System.out.println("[HLDS PM] Writing cache for game: " + i);
                GameContentRepository.createGameCache(i);
            }
            System.out.println("[HLDS PM] Repository cache has been created");
        } else {
            System.out.println("[HLDS PM] Repository cache found. Continuing...");
        }
    }
}
