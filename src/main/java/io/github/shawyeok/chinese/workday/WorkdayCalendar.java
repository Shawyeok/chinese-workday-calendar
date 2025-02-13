package io.github.shawyeok.chinese.workday;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class WorkdayCalendar {

    public static final ZoneId CHINA_TIMEZONE = ZoneId.of("UTC+8");

    private final Calendar localCalendar;

    private final Calendar remoteCalendar;

    private final Calendar weekdayCalendar = new WeekdayCalendar();

    public WorkdayCalendar(Builder builder) {
        this.localCalendar = new LocalCalendar(builder.localCalendarResource);
        this.remoteCalendar = new RemoteCalendar(builder.remoteCalendarUrl);
    }

    public boolean isWorkday(String date) {
        return isWorkday(LocalDate.parse(date));
    }

    public boolean isWorkday(Date date) {
        return isWorkday(date.toInstant().atZone(CHINA_TIMEZONE).toLocalDate());
    }

    public boolean isWorkday(long timeMillis) {
        return isWorkday(Instant.ofEpochMilli(timeMillis).atZone(CHINA_TIMEZONE).toLocalDate());
    }

    public boolean isWorkday(LocalDateTime dateTime) {
        return isWorkday(dateTime.toLocalDate());
    }

    public boolean isWorkday(int year, int month, int day) {
        return isWorkday(LocalDate.of(year, month, day));
    }

    public boolean isWorkday(LocalDate date) {
        Boolean workday = localCalendar.isWorkday(date);
        if (workday == null) {
            workday = remoteCalendar.isWorkday(date);
        }
        if (workday == null) {
            workday = weekdayCalendar.isWorkday(date);
        }
        return workday;
    }

    public LocalDate nextWorkday(LocalDate date) {
        return nextWorkday(date, 1);
    }

    private LocalDate nextWorkday(LocalDate date, int days) {
        if (days < 1) {
            throw new IllegalArgumentException("days must be greater than 0");
        }
        LocalDate next = date;
        for (int i = days; i > 0; ) {
            next = next.plusDays(1);
            if (isWorkday(next)) {
                i--;
            }
        }
        return next;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        public static final String DEFAULT_LOCAL_CALENDAR_RESOURCE = "workday/years.properties";

        public static final String DEFAULT_REMOTE_CALENDAR_URL =
                "https://chinese-workday-calendar.aops.io/years.properties";

        String localCalendarResource = DEFAULT_LOCAL_CALENDAR_RESOURCE;

        String remoteCalendarUrl = DEFAULT_REMOTE_CALENDAR_URL;

        public Builder localCalendarResource(String resourcePath) {
            this.localCalendarResource = resourcePath;
            return this;
        }

        public Builder remoteCalendarUrl(String url) {
            this.remoteCalendarUrl = url;
            return this;
        }

        public WorkdayCalendar build() {
            return new WorkdayCalendar(this);
        }
    }
}
