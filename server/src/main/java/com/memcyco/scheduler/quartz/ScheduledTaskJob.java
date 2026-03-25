package com.memcyco.scheduler.quartz;

import com.memcyco.scheduler.model.ScheduleEntity;
import com.memcyco.scheduler.repo.ScheduleRepository;
import com.memcyco.scheduler.tasks.TaskDefinition;
import com.memcyco.scheduler.tasks.TaskParameterValidation;
import com.memcyco.scheduler.tasks.TaskParamsJson;
import com.memcyco.scheduler.tasks.TaskRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTaskJob implements Job {

  public static final String SCHEDULE_ID = "scheduleId";

  private static final Logger log = LoggerFactory.getLogger(ScheduledTaskJob.class);

  private final ScheduleRepository scheduleRepository;
  private final TaskRegistry taskRegistry;
  private final ObjectMapper objectMapper;

  public ScheduledTaskJob(
      ScheduleRepository scheduleRepository,
      TaskRegistry taskRegistry,
      ObjectMapper objectMapper
  ) {
    this.scheduleRepository = scheduleRepository;
    this.taskRegistry = taskRegistry;
    this.objectMapper = objectMapper;
  }

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    Long scheduleId = (Long) context.getMergedJobDataMap().get(SCHEDULE_ID);
    if (scheduleId == null) {
      throw new JobExecutionException("Missing scheduleId in Quartz job data");
    }

    ScheduleEntity schedule = scheduleRepository.findById(scheduleId).orElse(null);
    if (schedule == null) {
      log.warn("Schedule {} not found — skipping (stale trigger?).", scheduleId);
      return;
    }
    if (!schedule.isEnabled()) {
      log.debug("Schedule {} disabled — skipping.", scheduleId);
      return;
    }

    try {
      Map<String, Object> params = TaskParamsJson.parse(objectMapper, schedule.getTaskParamsJson());
      TaskDefinition task = TaskDefinition.fromKey(schedule.getTaskKey());
      TaskParameterValidation.requirePresent(task, params);
      taskRegistry.executorFor(schedule.getTaskKey()).execute(params);
    } catch (IllegalArgumentException e) {
      throw new JobExecutionException(e.getMessage(), e);
    }
  }
}
