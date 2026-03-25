package com.memcyco.scheduler.service;

import com.memcyco.scheduler.model.ScheduleEntity;
import com.memcyco.scheduler.model.ScheduleType;
import com.memcyco.scheduler.quartz.QuartzJobNames;
import com.memcyco.scheduler.quartz.ScheduledTaskJob;
import com.memcyco.scheduler.quartz.WeeklyCronExpression;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Service;

@Service
public class QuartzScheduleService {

  private static final TimeZone UTC = TimeZone.getTimeZone(ZoneOffset.UTC);

  private final Scheduler scheduler;

  public QuartzScheduleService(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  public void upsertQuartzSchedule(ScheduleEntity schedule) {
    try {
      deleteQuartzSchedule(schedule.getId());
      if (!schedule.isEnabled()) {
        return;
      }

      ScheduleValidator.validateForScheduling(schedule);

      var jobDetail = JobBuilder.newJob(ScheduledTaskJob.class)
          .withIdentity(QuartzJobNames.jobKey(schedule.getId()))
          .usingJobData(ScheduledTaskJob.SCHEDULE_ID, schedule.getId())
          .build();

      Trigger trigger = buildTrigger(schedule);
      scheduler.scheduleJob(jobDetail, trigger);
    } catch (SchedulerException e) {
      throw new IllegalStateException("Failed to schedule job in Quartz", e);
    }
  }

  public void deleteQuartzSchedule(Long scheduleId) {
    if (scheduleId == null) {
      return;
    }
    try {
      scheduler.deleteJob(QuartzJobNames.jobKey(scheduleId));
    } catch (SchedulerException e) {
      throw new IllegalStateException("Failed to remove job from Quartz", e);
    }
  }

  private static Trigger buildTrigger(ScheduleEntity schedule) {
    return switch (schedule.getScheduleType()) {
      case ONCE -> onceTrigger(schedule);
      case INTERVAL -> intervalTrigger(schedule);
      case WEEKLY -> weeklyTrigger(schedule);
      case CRON -> cronTrigger(schedule);
    };
  }

  private static Trigger onceTrigger(ScheduleEntity s) {
    return TriggerBuilder.newTrigger()
        .withIdentity(QuartzJobNames.triggerKey(s.getId()))
        .startAt(Date.from(s.getStartAt()))
        .build();
  }

  private static Trigger intervalTrigger(ScheduleEntity s) {
    SimpleScheduleBuilder schedule = switch (s.getIntervalUnit().toUpperCase()) {
      case "MINUTES" -> SimpleScheduleBuilder.simpleSchedule()
          .withIntervalInMinutes(s.getIntervalValue())
          .repeatForever();
      case "HOURS" -> SimpleScheduleBuilder.simpleSchedule()
          .withIntervalInHours(s.getIntervalValue())
          .repeatForever();
      default -> throw new IllegalArgumentException("intervalUnit must be MINUTES or HOURS");
    };

    return TriggerBuilder.newTrigger()
        .withIdentity(QuartzJobNames.triggerKey(s.getId()))
        .startNow()
        .withSchedule(schedule)
        .build();
  }

  private static Trigger cronTrigger(ScheduleEntity s) {
    return TriggerBuilder.newTrigger()
        .withIdentity(QuartzJobNames.triggerKey(s.getId()))
        .withSchedule(
            CronScheduleBuilder.cronSchedule(s.getCronExpression()).inTimeZone(UTC))
        .build();
  }

  private static Trigger weeklyTrigger(ScheduleEntity s) {
    String cron = WeeklyCronExpression.toQuartzCron(s.getWeeklyDays(), s.getWeeklyTime());
    return TriggerBuilder.newTrigger()
        .withIdentity(QuartzJobNames.triggerKey(s.getId()))
        .withSchedule(CronScheduleBuilder.cronSchedule(cron).inTimeZone(UTC))
        .build();
  }
}
