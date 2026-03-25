package com.memcyco.scheduler.tasks;

import java.util.Map;

public final class TaskParameterValidation {

  private TaskParameterValidation() {}

  public static void requirePresent(TaskDefinition task, Map<String, Object> params) {
    for (var p : task.params()) {
      if (!p.required()) {
        continue;
      }
      Object v = params.get(p.name());
      if (v == null || String.valueOf(v).isBlank()) {
        throw new IllegalArgumentException("Missing required parameter: " + p.name());
      }
    }
  }
}
