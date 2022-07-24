package com.sotska.writer;

import com.sotska.entity.HttpStatus;
import com.sotska.exception.WebServerException;

import java.io.*;

import static com.sotska.entity.HttpStatus.INTERNAL_SERVER_ERROR;
import static com.sotska.entity.HttpStatus.NOT_FOUND;

public class ResponseWriter {

    private static final String LINE_SEPARATOR = "\r\n";

    public static void writeFileContent(OutputStream outputStream, String path) throws WebServerException {

        try (var fileInputStream = new FileInputStream((path))) {
            outputStream.write((addLineSeparator("HTTP/1.1 OK")).getBytes());
            fileInputStream.transferTo(outputStream);

        } catch (FileNotFoundException e) {
            throw new WebServerException(e, NOT_FOUND);
        } catch (IOException e) {
            throw new WebServerException(e, INTERNAL_SERVER_ERROR);
        }
    }

    public static void writeErrorMessage(OutputStream outputStream, HttpStatus httpStatus) {
        try {
            outputStream.write((addLineSeparator("HTTP/1.1 " + httpStatus.getCode())).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String addLineSeparator(String message) {
        return message + LINE_SEPARATOR + LINE_SEPARATOR;
    }
}
