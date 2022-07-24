package com.sotska;

import com.sotska.handler.RequestHandler;

import java.io.*;
import java.net.ServerSocket;

public class HttpServer {

    public static void main(String[] args) {
        var httpServer = new HttpServer();
        httpServer.start(3000, "src/main/resources");
    }

    public void start(int port, String webAppPath) {
        try (var serverSocket = new ServerSocket(port)) {
            while (true) {
                try (var socket = serverSocket.accept();
                     var bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     var outputStream = socket.getOutputStream()
                ) {
                    var requestHandler = new RequestHandler(bufferedReader, outputStream, webAppPath);
                    requestHandler.handle();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
