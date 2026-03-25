package com.memcyco.scheduler.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRescheduler implements ApplicationRunner {

  private final ScheduleService scheduleService;

  public StartupRescheduler(ScheduleService scheduleService) {
    this.scheduleService = scheduleService;
  }

  @Override
  public void run(ApplicationArguments args) {
    scheduleService.rescheduleAllFromDatabase();
  }
}
