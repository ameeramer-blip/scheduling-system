package com.memcyco.scheduler.tasks;

import java.util.List;

public enum TaskDefinition {
  LOG("Log Task", List.of(
      new TaskParamDefinition("message", ParamType.STRING, true)
  )),
  DUMMY_EMAIL("Dummy Email Sender", List.of(
      new TaskParamDefinition("to", ParamType.STRING, true),
      new TaskParamDefinition("subject", ParamType.STRING, true),
      new TaskParamDefinition("body", ParamType.STRING, false)
  ));

  private final String displayName;
  private final List<TaskParamDefinition> params;

  TaskDefinition(String displayName, List<TaskParamDefinition> params) {
    this.displayName = displayName;
    this.params = params;
  }

  public String displayName() {
    return displayName;
  }

  public List<TaskParamDefinition> params() {
    return params;
  }

  /** Resolves API task keys (enum names). Throws {@link IllegalArgumentException} if unknown. */
  public static TaskDefinition fromKey(String taskKey) {
    if (taskKey == null || taskKey.isBlank()) {
      throw new IllegalArgumentException("taskKey is required");
    }
    try {
      return TaskDefinition.valueOf(taskKey.trim());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Unknown taskKey: " + taskKey);
    }
  }
}

