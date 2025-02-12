package io.github.shawyeok.chinese.workday;

import java.time.LocalDate;

interface Calendar {

    Boolean isWorkday(LocalDate date);
}
