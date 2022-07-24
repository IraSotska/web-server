package com.sotska.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class RequestHandlerTest {

    @DisplayName("Should Handle Request")
    @Test
    void shouldHandleRequest() throws IOException {
        var requestAsString = """
                GET /test.txt HTTP/1.1
                Host: localhost:3000
                Connection: keep-alive""";

        var expectedResult = "HTTP/1.1 OK\r\n" +
                "\r\n" +
                "<!doctype html>\r\n" +
                "<html lang=\"en\">\r\n" +
                "<meta charset=\"UTF-8\">\r\n" +
                "<meta name=\"viewport\">";

        handleRequestAndCompareResult(requestAsString, expectedResult);
    }

    @DisplayName("Should Return 404 If No File")
    @Test
    void shouldReturn404IfNoFile() throws IOException {
        var requestAsString = """
                GET /test1.txt HTTP/1.1
                Host: localhost:3000
                Connection: keep-alive""";

        var expectedResult = "HTTP/1.1 404\r\n" +
                "\r\n";

        handleRequestAndCompareResult(requestAsString, expectedResult);
    }

    @DisplayName("Should Return 400 If Not Correct Request")
    @Test
    void shouldReturn400IfNotCorrectRequest() throws IOException {
        var requestAsString = """
                GET /test.txt HTTP/1.1
                abc
                Connection: keep-alive""";

        var expectedResult = "HTTP/1.1 400\r\n" +
                "\r\n";

        handleRequestAndCompareResult(requestAsString, expectedResult);
    }

    @DisplayName("Should Return 405 If Method Not Allowed")
    @Test
    void shouldReturn405IfMethodNotAllowed() throws IOException {
        var requestAsString = """
                PUT /test.txt HTTP/1.1
                Host: localhost:3000
                Connection: keep-alive""";

        var expectedResult = "HTTP/1.1 405\r\n" +
                "\r\n";

        handleRequestAndCompareResult(requestAsString, expectedResult);
    }

    private void handleRequestAndCompareResult(String request, String expectedResult) throws IOException {
        try (var bufferedReader = new BufferedReader(new StringReader(request));
             var outputStream = new ByteArrayOutputStream();
        ) {
            var requestHandler = new RequestHandler(bufferedReader, outputStream, "src/test/resources");
            requestHandler.handle();

            assertEquals(expectedResult, outputStream.toString());
        }
    }
}