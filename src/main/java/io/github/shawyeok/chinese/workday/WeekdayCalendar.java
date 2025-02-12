package io.github.shawyeok.chinese.workday;

import java.time.DayOfWeek;
import java.time.LocalDate;

class WeekdayCalendar implements Calendar {

    @Override
    public Boolean isWorkday(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.MONDAY
                || dayOfWeek == DayOfWeek.TUESDAY
                || dayOfWeek == DayOfWeek.WEDNESDAY
                || dayOfWeek == DayOfWeek.THURSDAY
                || dayOfWeek == DayOfWeek.FRIDAY;
    }
}
