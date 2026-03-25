package com.memcyco.scheduler.tasks;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DummyEmailTaskExecutor implements TaskExecutor {
  private static final Logger log = LoggerFactory.getLogger(DummyEmailTaskExecutor.class);

  @Override
  public TaskDefinition definition() {
    return TaskDefinition.DUMMY_EMAIL;
  }

  @Override
  public void execute(Map<String, Object> params) {
    log.info("DummyEmailTask executed. to={}, subject={}, body={}",
        params.get("to"), params.get("subject"), params.get("body"));
  }
}

