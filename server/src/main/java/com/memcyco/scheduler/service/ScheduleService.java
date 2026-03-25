package com.memcyco.scheduler.service;

import com.memcyco.scheduler.api.UpsertScheduleRequest;
import com.memcyco.scheduler.error.ScheduleNotFoundException;
import com.memcyco.scheduler.model.ScheduleEntity;
import com.memcyco.scheduler.repo.ScheduleRepository;
import com.memcyco.scheduler.tasks.TaskDefinition;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScheduleService {

  private final ScheduleRepository schedules;
  private final QuartzScheduleService quartz;

  public ScheduleService(ScheduleRepository schedules, QuartzScheduleService quartz) {
    this.schedules = schedules;
    this.quartz = quartz;
  }

  @Transactional(readOnly = true)
  public List<ScheduleEntity> list() {
    return schedules.findAllByOrderByIdDesc();
  }

  @Transactional(readOnly = true)
  public ScheduleEntity get(Long id) {
    return schedules.findById(id).orElseThrow(() -> new ScheduleNotFoundException(id));
  }

  @Transactional
  public ScheduleEntity create(UpsertScheduleRequest req) {
    TaskDefinition.fromKey(req.taskKey());

    ScheduleEntity entity = new ScheduleEntity();
    applyRequest(entity, req);
    Instant now = Instant.now();
    entity.setCreatedAt(now);
    entity.setUpdatedAt(now);

    ScheduleEntity saved = schedules.save(entity);
    quartz.upsertQuartzSchedule(saved);
    return saved;
  }

  @Transactional
  public ScheduleEntity update(Long id, UpsertScheduleRequest req) {
    TaskDefinition.fromKey(req.taskKey());

    ScheduleEntity entity = get(id);
    applyRequest(entity, req);
    entity.setUpdatedAt(Instant.now());

    ScheduleEntity saved = schedules.save(entity);
    quartz.upsertQuartzSchedule(saved);
    return saved;
  }

  @Transactional
  public void delete(Long id) {
    ScheduleEntity entity = get(id);
    schedules.delete(entity);
    quartz.deleteQuartzSchedule(id);
  }

  /** Re-registers Quartz triggers from persisted rows (e.g. after restart). */
  @Transactional(readOnly = true)
  public void rescheduleAllFromDatabase() {
    for (ScheduleEntity row : schedules.findAll()) {
      quartz.upsertQuartzSchedule(row);
    }
  }

  private static void applyRequest(ScheduleEntity entity, UpsertScheduleRequest req) {
    entity.setName(req.name());
    entity.setEnabled(req.enabled());
    entity.setTaskKey(req.taskKey());
    entity.setScheduleType(req.scheduleType());
    entity.setStartAt(req.startAt());
    entity.setIntervalValue(req.intervalValue());
    entity.setIntervalUnit(req.intervalUnit());
    entity.setWeeklyDays(req.weeklyDays());
    entity.setWeeklyTime(req.weeklyTime());
    entity.setCronExpression(req.cronExpression());
    entity.setTaskParamsJson(req.taskParamsJson());
  }
}
