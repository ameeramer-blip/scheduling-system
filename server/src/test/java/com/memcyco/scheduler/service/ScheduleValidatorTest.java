package com.memcyco.scheduler.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.memcyco.scheduler.model.ScheduleEntity;
import com.memcyco.scheduler.model.ScheduleType;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class ScheduleValidatorTest {

  @Test
  void accepts_valid_interval() {
    ScheduleEntity e = baseEntity();
    e.setScheduleType(ScheduleType.INTERVAL);
    e.setIntervalValue(5);
    e.setIntervalUnit("MINUTES");
    assertThatCode(() -> ScheduleValidator.validateForScheduling(e)).doesNotThrowAnyException();
  }

  @Test
  void rejects_interval_without_unit() {
    ScheduleEntity e = baseEntity();
    e.setScheduleType(ScheduleType.INTERVAL);
    e.setIntervalValue(1);
    e.setIntervalUnit(null);
    assertThatThrownBy(() -> ScheduleValidator.validateForScheduling(e))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("intervalUnit");
  }

  @Test
  void rejects_once_without_start() {
    ScheduleEntity e = baseEntity();
    e.setScheduleType(ScheduleType.ONCE);
    e.setStartAt(null);
    assertThatThrownBy(() -> ScheduleValidator.validateForScheduling(e))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("startAt");
  }

  @Test
  void accepts_once_with_start() {
    ScheduleEntity e = baseEntity();
    e.setScheduleType(ScheduleType.ONCE);
    e.setStartAt(Instant.parse("2030-01-01T10:00:00Z"));
    assertThatCode(() -> ScheduleValidator.validateForScheduling(e)).doesNotThrowAnyException();
  }

  private static ScheduleEntity baseEntity() {
    ScheduleEntity e = new ScheduleEntity();
    e.setId(1L);
    e.setName("n");
    e.setTaskKey("LOG");
    e.setEnabled(true);
    return e;
  }
}
