package com.memcyco.scheduler.quartz;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Builds a Quartz cron from weekly UI fields: days 1–7 (Mon=1) and time HH:mm (local interpretation; trigger uses UTC).
 */
public final class WeeklyCronExpression {

  private WeeklyCronExpression() {}

  public static String toQuartzCron(String weeklyDaysCsv, String weeklyTimeHhMm) {
    int[] days = Arrays.stream(weeklyDaysCsv.split(","))
        .map(String::trim)
        .filter(s -> !s.isBlank())
        .mapToInt(Integer::parseInt)
        .toArray();
    if (days.length == 0) {
      throw new IllegalArgumentException("weeklyDays must contain at least one day (1-7)");
    }

    String[] parts = weeklyTimeHhMm.split(":");
    if (parts.length != 2) {
      throw new IllegalArgumentException("weeklyTime must be HH:mm");
    }
    int hour = Integer.parseInt(parts[0]);
    int minute = Integer.parseInt(parts[1]);

    String dow = Arrays.stream(days)
        .mapToObj(WeeklyCronExpression::dayOfWeekToken)
        .distinct()
        .collect(Collectors.joining(","));

    return String.format("0 %d %d ? * %s", minute, hour, dow);
  }

  private static String dayOfWeekToken(int day) {
    return switch (day) {
      case 1 -> "MON";
      case 2 -> "TUE";
      case 3 -> "WED";
      case 4 -> "THU";
      case 5 -> "FRI";
      case 6 -> "SAT";
      case 7 -> "SUN";
      default -> throw new IllegalArgumentException("weeklyDays must be in range 1-7 (Mon=1)");
    };
  }
}
