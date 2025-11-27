package io.github.shawyeok.chinese.workday;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LocalCalendarTest {

    LocalCalendar calendar = new LocalCalendar("workday/years.properties");

    @ParameterizedTest(name = "isWorkday({0}) = {1}")
    @CsvFileSource(resources = {
            "2007.csv", "2008.csv", "2009.csv", "2010.csv",
            "2011.csv", "2012.csv", "2013.csv", "2014.csv", "2015.csv", "2016.csv", "2017.csv", "2018.csv", "2019.csv",
            "2020.csv", "2021.csv", "2022.csv", "2023.csv", "2024.csv", "2025.csv", "2026.csv"})
    void isWorkday(String date, int expected) {
        if (expected == 0) {
            assertFalse(calendar.isWorkday(LocalDate.parse(date)));
        } else {
            assertTrue(calendar.isWorkday(LocalDate.parse(date)));
        }
    }

    @ParameterizedTest(name = "OutOfScope isWorkday({0}) = null")
    @ValueSource(strings = {"1999-10-01", "1999-10-02", "1999-10-03", "2053-01-01", "2053-01-02", "2053-01-03"})
    void isWorkdayOutOfScope() {
        assertNull(calendar.isWorkday(LocalDate.parse("1999-10-01")));
    }
}
