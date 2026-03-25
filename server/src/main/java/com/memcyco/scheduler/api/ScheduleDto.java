package com.memcyco.scheduler.api;

import com.memcyco.scheduler.model.ScheduleType;
import java.time.Instant;

public record ScheduleDto(
    Long id,
    String name,
    boolean enabled,
    String taskKey,
    ScheduleType scheduleType,
    Instant startAt,
    Integer intervalValue,
    String intervalUnit,
    String weeklyDays,
    String weeklyTime,
    String cronExpression,
    String taskParamsJson,
    Instant createdAt,
    Instant updatedAt
) {}

