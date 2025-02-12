package io.github.shawyeok.chinese.workday;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Properties;

class LocalCalendar implements Calendar {

    private final Properties cache;

    LocalCalendar(String resourcePath) {
        Properties properties = new Properties();
        try (InputStream is = WorkdayCalendar.class.getClassLoader().getResourceAsStream(resourcePath)) {
            properties.load(is);
        } catch (IOException e) {
            throw new IllegalArgumentException("Load resource with exception: " + resourcePath, e);
        }
        this.cache = properties;
    }

    LocalCalendar(Properties properties) {
        this.cache = properties;
    }

    @Override
    public Boolean isWorkday(LocalDate date) {
        String year = String.valueOf(date.getYear());
        String property = cache.getProperty(year);
        if (property != null) {
            int dayOfYear = date.getDayOfYear();
            if (property.length() >= dayOfYear) {
                return property.charAt(dayOfYear - 1) == '1';
            }
        }
        return null;
    }
}
