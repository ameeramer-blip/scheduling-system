package com.memcyco.scheduler.error;

public class ScheduleNotFoundException extends RuntimeException {

  public ScheduleNotFoundException(Long id) {
    super("Schedule not found: " + id);
  }
}
