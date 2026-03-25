package com.memcyco.scheduler.api;

import com.memcyco.scheduler.model.ScheduleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record UpsertScheduleRequest(
    @NotBlank String name,
    boolean enabled,
    @NotBlank String taskKey,
    @NotNull ScheduleType scheduleType,
    Instant startAt,
    Integer intervalValue,
    String intervalUnit,
    String weeklyDays,
    String weeklyTime,
    String cronExpression,
    String taskParamsJson
) {}

