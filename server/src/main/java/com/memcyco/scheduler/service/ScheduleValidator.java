package com.memcyco.scheduler.service;

import com.memcyco.scheduler.model.ScheduleEntity;
import com.memcyco.scheduler.model.ScheduleType;

public class ScheduleValidator {
  private ScheduleValidator() {}

  public static void validateForScheduling(ScheduleEntity s) {
    if (s.getTaskKey() == null || s.getTaskKey().isBlank()) {
      throw new IllegalArgumentException("taskKey is required");
    }

    if (s.getScheduleType() == null) {
      throw new IllegalArgumentException("scheduleType is required");
    }

    if (s.getName() == null || s.getName().isBlank()) {
      throw new IllegalArgumentException("name is required");
    }

    switch (s.getScheduleType()) {
      case ONCE -> requireOnce(s);
      case INTERVAL -> requireInterval(s);
      case WEEKLY -> requireWeekly(s);
      case CRON -> requireCron(s);
    }
  }

  private static void requireOnce(ScheduleEntity s) {
    if (s.getStartAt() == null) {
      throw new IllegalArgumentException("startAt is required for " + ScheduleType.ONCE);
    }
  }

  private static void requireInterval(ScheduleEntity s) {
    if (s.getIntervalValue() == null || s.getIntervalValue() <= 0) {
      throw new IllegalArgumentException("intervalValue must be > 0 for " + ScheduleType.INTERVAL);
    }
    String unit = s.getIntervalUnit();
    if (unit == null || unit.isBlank()) {
      throw new IllegalArgumentException("intervalUnit is required for " + ScheduleType.INTERVAL);
    }
    String u = unit.trim().toUpperCase();
    if (!u.equals("MINUTES") && !u.equals("HOURS")) {
      throw new IllegalArgumentException("intervalUnit must be MINUTES or HOURS");
    }
  }

  private static void requireWeekly(ScheduleEntity s) {
    if (s.getWeeklyDays() == null || s.getWeeklyDays().isBlank()) {
      throw new IllegalArgumentException("weeklyDays is required for " + ScheduleType.WEEKLY);
    }
    if (s.getWeeklyTime() == null || s.getWeeklyTime().isBlank()) {
      throw new IllegalArgumentException("weeklyTime is required for " + ScheduleType.WEEKLY);
    }
  }

  private static void requireCron(ScheduleEntity s) {
    if (s.getCronExpression() == null || s.getCronExpression().isBlank()) {
      throw new IllegalArgumentException("cronExpression is required for " + ScheduleType.CRON);
    }
  }
}

