package com.sotska.parser;

import com.sotska.entity.HttpMethod;
import com.sotska.entity.Request;
import com.sotska.exception.WebServerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import static com.sotska.entity.HttpStatus.*;

public class RequestParser {

    public static Request parse(BufferedReader bufferedReader) throws WebServerException {
        var request = new Request();
        injectMethodAndUri(bufferedReader, request);
        injectHeaders(bufferedReader, request);
        return request;
    }

    private static void injectMethodAndUri(BufferedReader bufferedReader, Request request) throws WebServerException {
        try {
            var line = bufferedReader.readLine();
            request.setHttpMethod(HttpMethod.valueOf(line.substring(0, line.indexOf(" "))));
            request.setUri(line.substring(line.indexOf("/"), line.lastIndexOf(" ")));

        } catch (IllegalArgumentException e) {
            throw new WebServerException(e, METHOD_NOT_ALLOWED);
        } catch (IOException e) {
            throw new WebServerException(e, INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new WebServerException(e, BAD_REQUEST);
        }
    }

    private static void injectHeaders(BufferedReader bufferedReader, Request request) throws WebServerException {
        var headers = new HashMap<String, String>();

        try {
            var headerLine = bufferedReader.readLine();
            while (headerLine != null && !headerLine.isEmpty()) {
                var headerArray = headerLine.split(": ");
                headers.put(headerArray[0], headerArray[1]);
                headerLine = bufferedReader.readLine();
            }
            request.setHeaders(headers);
        } catch (IOException e) {
            throw new WebServerException(e, INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new WebServerException(e, BAD_REQUEST);
        }
    }
}
