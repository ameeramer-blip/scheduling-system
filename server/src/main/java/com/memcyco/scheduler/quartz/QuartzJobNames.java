package com.memcyco.scheduler.quartz;

import org.quartz.JobKey;
import org.quartz.TriggerKey;

/** Shared Quartz group + naming so jobs/triggers stay easy to find in logs. */
public final class QuartzJobNames {

  public static final String GROUP = "schedules";

  private QuartzJobNames() {}

  public static JobKey jobKey(Long scheduleId) {
    return JobKey.jobKey("schedule-" + scheduleId, GROUP);
  }

  public static TriggerKey triggerKey(Long scheduleId) {
    return TriggerKey.triggerKey("trigger-" + scheduleId, GROUP);
  }
}
