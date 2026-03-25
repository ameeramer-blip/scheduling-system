package com.memcyco.scheduler.service;

import com.memcyco.scheduler.api.ScheduleDto;
import com.memcyco.scheduler.model.ScheduleEntity;

public class ScheduleMapper {
  private ScheduleMapper() {}

  public static ScheduleDto toDto(ScheduleEntity e) {
    return new ScheduleDto(
        e.getId(),
        e.getName(),
        e.isEnabled(),
        e.getTaskKey(),
        e.getScheduleType(),
        e.getStartAt(),
        e.getIntervalValue(),
        e.getIntervalUnit(),
        e.getWeeklyDays(),
        e.getWeeklyTime(),
        e.getCronExpression(),
        e.getTaskParamsJson(),
        e.getCreatedAt(),
        e.getUpdatedAt()
    );
  }
}

