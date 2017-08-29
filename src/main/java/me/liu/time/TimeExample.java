package me.liu.time;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * Created by tongwei on 17/8/14.
 */
public class TimeExample {

    public static void main(String[] args) {

        LocalTime now = LocalTime.now();

        System.out.println(now);

        Instant endInstant = Instant.now().truncatedTo(ChronoUnit.HOURS);

        Instant startInstant = endInstant.minus(24, ChronoUnit.HOURS);

        long end = endInstant.toEpochMilli();

        long st = endInstant.minus(24, ChronoUnit.HOURS).toEpochMilli();


        System.out.println(endInstant);
        System.out.println(startInstant);


        String str = "2017081400";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        ZonedDateTime dateTime = LocalDateTime.parse(str, formatter).atZone(ZoneId.systemDefault());



        ZonedDateTime dateTime2  = ZonedDateTime.now();
        System.out.println(formatter.format(dateTime2));

        System.out.println(dateTime);

    }
}
