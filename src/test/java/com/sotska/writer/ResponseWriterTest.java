package com.sotska.writer;

import com.sotska.exception.WebServerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.sotska.entity.HttpStatus.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

class ResponseWriterTest {

    @DisplayName("Should Write File Content")
    @Test
    void shouldWriteFileContent() throws IOException {
        var expectedResult = "HTTP/1.1 OK\r\n" +
                "\r\n" +
                "<!doctype html>\r\n" +
                "<html lang=\"en\">\r\n" +
                "<meta charset=\"UTF-8\">\r\n" +
                "<meta name=\"viewport\">";

        try (var outputStream = new ByteArrayOutputStream()) {
            ResponseWriter.writeFileContent(outputStream, "src/test/resources/test.txt");

            assertEquals(expectedResult, outputStream.toString());
        }
    }

    @DisplayName("Should Throw Not Found Exception")
    @Test
    void shouldThrowNotFoundException() throws IOException {
        try (var outputStream = new ByteArrayOutputStream()) {
            var actualException = catchThrowable(() ->
                    ResponseWriter.writeFileContent(outputStream, "src/test/resources/test1.txt"));

            assertThat(actualException).isExactlyInstanceOf(WebServerException.class);
            assertEquals(NOT_FOUND, ((WebServerException) actualException).getHttpStatus());
        }
    }

    @DisplayName("Should Write Error Message")
    @Test
    void shouldWriteErrorMessage() throws IOException {
        var expectedResult = "HTTP/1.1 404\r\n" +
                "\r\n";

        try (var outputStream = new ByteArrayOutputStream()) {
            ResponseWriter.writeErrorMessage(outputStream, NOT_FOUND);

            assertEquals(expectedResult, outputStream.toString());
        }
    }
}