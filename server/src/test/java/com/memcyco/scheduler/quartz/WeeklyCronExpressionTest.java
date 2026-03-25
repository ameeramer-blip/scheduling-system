package com.memcyco.scheduler.quartz;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class WeeklyCronExpressionTest {

  @Test
  void builds_quartz_cron_for_single_day() {
    String cron = WeeklyCronExpression.toQuartzCron("1", "09:30");
    assertThat(cron).isEqualTo("0 30 9 ? * MON");
  }

  @Test
  void builds_quartz_cron_for_multiple_days() {
    String cron = WeeklyCronExpression.toQuartzCron("1,3,5", "14:05");
    assertThat(cron).isEqualTo("0 5 14 ? * MON,WED,FRI");
  }

  @Test
  void rejects_empty_days() {
    assertThatThrownBy(() -> WeeklyCronExpression.toQuartzCron("", "09:00"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void rejects_bad_time_format() {
    assertThatThrownBy(() -> WeeklyCronExpression.toQuartzCron("1", "9"))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
