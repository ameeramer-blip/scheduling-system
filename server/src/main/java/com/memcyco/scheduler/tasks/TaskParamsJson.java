package com.memcyco.scheduler.tasks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public final class TaskParamsJson {

  private TaskParamsJson() {}

  public static Map<String, Object> parse(ObjectMapper mapper, String json) {
    if (json == null || json.isBlank()) {
      return Map.of();
    }
    try {
      return mapper.readValue(json, new TypeReference<>() {});
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid taskParamsJson", e);
    }
  }
}
