package com.sotska.parser;


import com.sotska.exception.WebServerException;
import com.sotska.parser.RequestParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import static com.sotska.entity.HttpMethod.GET;
import static com.sotska.entity.HttpStatus.BAD_REQUEST;
import static com.sotska.entity.HttpStatus.METHOD_NOT_ALLOWED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestParserTest {

    @Test
    @DisplayName("Should Parse Request")
    void shouldParseRequest() {
        var requestAsString = """
                GET /some HTTP/1.1
                Host: localhost:3000
                Connection: keep-alive
                sec-ch-ua: ".Not/A)Brand";v="99", "Google Chrome";v="103", "Chromium";v="103""";
        var reader = new BufferedReader(new StringReader(requestAsString));

        var resultRequest = RequestParser.parse(reader);

        assertEquals(Map.of("Host", "localhost:3000", "Connection", "keep-alive", "sec-ch-ua",
                "\".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103"), resultRequest.getHeaders());

        assertEquals("/some", resultRequest.getUri());
        assertEquals(GET, resultRequest.getHttpMethod());
    }

    @Test
    @DisplayName("Should Throw Method Not Allow If Parse Post Request")
    void shouldThrowMethodNotAllowIfParsePostRequest() {
        var requestAsString = """
                POST /some HTTP/1.1
                Host: localhost:3000
                Connection: keep-alive
                sec-ch-ua: ".Not/A)Brand";v="99", "Google Chrome";v="103", "Chromium";v="103""";

        var reader = new BufferedReader(new StringReader(requestAsString));
        var actualException = catchThrowable(() -> RequestParser.parse(reader));

        assertThat(actualException).isExactlyInstanceOf(WebServerException.class);
        assertEquals(METHOD_NOT_ALLOWED, ((WebServerException) actualException).getHttpStatus());
    }

    @Test
    @DisplayName("Should Throw Bad Request If Not Valid Request Header")
    void shouldThrowBadRequestIfNotValidRequestHeader() {
        var requestAsString = """
                GET /some HTTP/1.1
                Host: localhost:3000
                Connection: keep-alive
                sec-ch-uaNot/A)Brand";v="99", "Google Chrome";v="103", "Chromium";v="103""";

        var reader = new BufferedReader(new StringReader(requestAsString));
        var actualException = catchThrowable(() -> RequestParser.parse(reader));

        assertThat(actualException).isExactlyInstanceOf(WebServerException.class);
        assertEquals(BAD_REQUEST, ((WebServerException) actualException).getHttpStatus());
    }

    @Test
    @DisplayName("Should Throw Bad Request If Not Valid Uri String")
    void shouldThrowBadRequestIfNotValidUriString() {
        var requestAsString = """
                abc
                Host: localhost:3000
                Connection: keep-alive
                sec-ch-ua: ".Not/A)Brand";v="99", "Google Chrome";v="103", "Chromium";v="103""";

        var reader = new BufferedReader(new StringReader(requestAsString));
        var actualException = catchThrowable(() -> RequestParser.parse(reader));

        assertThat(actualException).isExactlyInstanceOf(WebServerException.class);
        assertEquals(BAD_REQUEST, ((WebServerException) actualException).getHttpStatus());
    }
}