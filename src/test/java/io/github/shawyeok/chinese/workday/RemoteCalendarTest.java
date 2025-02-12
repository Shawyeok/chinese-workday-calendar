package io.github.shawyeok.chinese.workday;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RemoteCalendarTest {

    private HttpServer httpServer;

    private RemoteCalendar calendar;

    @BeforeEach
    void setUp() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(0), 0);
        String path = "/calendar/years.properties";
        httpServer.createContext(path, exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                String response = "2025 = 01100111110011111001111101100000000111101111100111110011111001111100111110011111001111100111100011111001111100111110111100000111100111110011111001111100011110011111001111100111110011111001111100111110011111001111100111110011111001111100111110011111001111100111110011111011100000000111011111001111100111110011111001111100111110011111001111100111110011111001111100111";
                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(200, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        });
        httpServer.start();
        String remoteUrl = "http://127.0.0.1:" + httpServer.getAddress().getPort() + path;
        calendar = new RemoteCalendar(remoteUrl);
    }

    @AfterEach
    void tearDown() {
        if (httpServer != null) {
            httpServer.stop(0);
        }
    }

    @Test
    void isWorkday() throws IOException {
        assertFalse(calendar.isWorkday(LocalDate.of(2025, 1, 1)));
        assertTrue(calendar.isWorkday(LocalDate.of(2025, 1, 2)));
        assertTrue(calendar.isWorkday(LocalDate.of(2025, 1, 3)));
        assertTrue(calendar.isWorkday(LocalDate.of(2025, 12, 31)));
        assertEquals(1, calendar.getFetchRemoteDataTimes());
    }

    @ParameterizedTest(name = "OutOfScope isWorkday({0}) = null")
    @ValueSource(strings = {"1999-10-01", "1999-10-02", "1999-10-03", "2053-01-01", "2053-01-02", "2053-01-03"})
    void isWorkdayOutOfScope(String date) {
        assertNull(calendar.isWorkday(LocalDate.parse(date)));
    }
}