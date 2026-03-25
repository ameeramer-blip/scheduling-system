package com.memcyco.scheduler.tasks;

import java.util.Map;

public interface TaskExecutor {
  TaskDefinition definition();

  void execute(Map<String, Object> params);
}

