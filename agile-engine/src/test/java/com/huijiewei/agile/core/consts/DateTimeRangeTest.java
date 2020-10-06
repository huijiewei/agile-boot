package com.huijiewei.agile.core.consts;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

/**
 * @author huijiewei
 */

@SpringBootTest
class DateTimeRangeTest {

    @Test
    void parse() {
        String[] range1 = new String[]{"2020-10-01", "2020-10-17"};

        DateTimeRange dateTimeRange1 = DateTimeRange.parse(range1);

        Assert.assertEquals(dateTimeRange1.getBegin(), LocalDateTime.of(2020, 10, 1, 0, 0, 0));
        Assert.assertEquals(dateTimeRange1.getEnd(), LocalDateTime.of(2020, 10, 17, 23, 59, 59));

        String[] range2 = new String[]{"2020-10-01 10:13:00", "2020-10-17 11:12:56"};

        DateTimeRange dateTimeRange2 = DateTimeRange.parse(range2);

        Assert.assertEquals(dateTimeRange2.getBegin(), LocalDateTime.of(2020, 10, 1, 10, 13, 0));
        Assert.assertEquals(dateTimeRange2.getEnd(), LocalDateTime.of(2020, 10, 17, 11, 12, 56));

    }
}