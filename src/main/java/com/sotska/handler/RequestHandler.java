package com.sotska.handler;

import com.sotska.exception.WebServerException;
import com.sotska.parser.RequestParser;
import com.sotska.writer.ResponseWriter;
import com.sotska.entity.Request;

import java.io.*;

public class RequestHandler {

    private BufferedReader bufferedReader;
    private OutputStream outputStream;
    private String webAppPath;

    public RequestHandler(BufferedReader bufferedReader, OutputStream outputStream, String webAppPath) {
        this.bufferedReader = bufferedReader;
        this.outputStream = outputStream;
        this.webAppPath = webAppPath;
    }

    public void handle() {
        try {
            Request request = RequestParser.parse(bufferedReader);
            ResponseWriter.writeFileContent(outputStream, webAppPath + request.getUri());

        } catch (WebServerException e) {
            e.getCause().printStackTrace();
            ResponseWriter.writeErrorMessage(outputStream, e.getHttpStatus());
        }
    }
}
