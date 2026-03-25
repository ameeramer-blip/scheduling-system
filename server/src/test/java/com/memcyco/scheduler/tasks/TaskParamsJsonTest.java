package com.memcyco.scheduler.tasks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TaskParamsJsonTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void parses_json_object() {
    Map<String, Object> m = TaskParamsJson.parse(mapper, "{\"message\":\"x\"}");
    assertThat(m).containsEntry("message", "x");
  }

  @Test
  void empty_string_yields_empty_map() {
    assertThat(TaskParamsJson.parse(mapper, "")).isEmpty();
    assertThat(TaskParamsJson.parse(mapper, null)).isEmpty();
  }

  @Test
  void invalid_json_throws() {
    assertThatThrownBy(() -> TaskParamsJson.parse(mapper, "not-json"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid taskParamsJson");
  }
}
