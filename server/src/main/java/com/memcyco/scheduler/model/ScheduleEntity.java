package com.memcyco.scheduler.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "schedules")
public class ScheduleEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private boolean enabled = true;

  @Column(nullable = false)
  private String taskKey;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ScheduleType scheduleType;

  @Column
  private Instant startAt; // for ONCE

  @Column
  private Integer intervalValue; // for INTERVAL

  @Column
  private String intervalUnit; // MINUTES/HOURS

  @Column
  private String weeklyDays; // comma-separated 1-7 (Mon=1)

  @Column
  private String weeklyTime; // HH:mm

  @Column
  private String cronExpression; // for CRON

  @Lob
  @Column
  private String taskParamsJson; // simple JSON map string

  @Column(nullable = false)
  private Instant createdAt = Instant.now();

  @Column(nullable = false)
  private Instant updatedAt = Instant.now();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getTaskKey() {
    return taskKey;
  }

  public void setTaskKey(String taskKey) {
    this.taskKey = taskKey;
  }

  public ScheduleType getScheduleType() {
    return scheduleType;
  }

  public void setScheduleType(ScheduleType scheduleType) {
    this.scheduleType = scheduleType;
  }

  public Instant getStartAt() {
    return startAt;
  }

  public void setStartAt(Instant startAt) {
    this.startAt = startAt;
  }

  public Integer getIntervalValue() {
    return intervalValue;
  }

  public void setIntervalValue(Integer intervalValue) {
    this.intervalValue = intervalValue;
  }

  public String getIntervalUnit() {
    return intervalUnit;
  }

  public void setIntervalUnit(String intervalUnit) {
    this.intervalUnit = intervalUnit;
  }

  public String getWeeklyDays() {
    return weeklyDays;
  }

  public void setWeeklyDays(String weeklyDays) {
    this.weeklyDays = weeklyDays;
  }

  public String getWeeklyTime() {
    return weeklyTime;
  }

  public void setWeeklyTime(String weeklyTime) {
    this.weeklyTime = weeklyTime;
  }

  public String getCronExpression() {
    return cronExpression;
  }

  public void setCronExpression(String cronExpression) {
    this.cronExpression = cronExpression;
  }

  public String getTaskParamsJson() {
    return taskParamsJson;
  }

  public void setTaskParamsJson(String taskParamsJson) {
    this.taskParamsJson = taskParamsJson;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }
}

