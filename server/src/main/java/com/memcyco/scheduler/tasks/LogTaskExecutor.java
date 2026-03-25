package com.memcyco.scheduler.tasks;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogTaskExecutor implements TaskExecutor {
  private static final Logger log = LoggerFactory.getLogger(LogTaskExecutor.class);

  @Override
  public TaskDefinition definition() {
    return TaskDefinition.LOG;
  }

  @Override
  public void execute(Map<String, Object> params) {
    Object message = params.get("message");
    log.info("LogTask executed. message={}", message);
  }
}

