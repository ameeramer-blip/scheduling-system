package com.memcyco.scheduler.tasks;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class TaskRegistry {

  private final Map<TaskDefinition, TaskExecutor> executors;

  public TaskRegistry(List<TaskExecutor> executors) {
    var map = new EnumMap<TaskDefinition, TaskExecutor>(TaskDefinition.class);
    for (TaskExecutor ex : executors) {
      map.put(ex.definition(), ex);
    }
    this.executors = Map.copyOf(map);
  }

  public TaskExecutor executorFor(String taskKey) {
    TaskDefinition def = TaskDefinition.fromKey(taskKey);
    TaskExecutor ex = executors.get(def);
    if (ex == null) {
      throw new IllegalStateException("No executor registered for task: " + def);
    }
    return ex;
  }
}
