package ru.hldspm.file_access_server.server;
import com.sun.net.httpserver.HttpServer;
import ru.hldspm.file_access_server.handlers.FileAccessHandler;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**Starts HTTP File Server for the repository*/
public class FileAccessServer {
    public static void startFileServer(String baseDir, int port){
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", new FileAccessHandler(baseDir));
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();
            System.out.println("[HLDS:PM] File Server is running on port: " + port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
