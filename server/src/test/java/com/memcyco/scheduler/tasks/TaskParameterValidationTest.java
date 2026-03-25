package com.memcyco.scheduler.tasks;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TaskParameterValidationTest {

  @Test
  void passes_when_required_fields_present() {
    Map<String, Object> params = new HashMap<>();
    params.put("message", "hello");
    assertThatCode(() -> TaskParameterValidation.requirePresent(TaskDefinition.LOG, params))
        .doesNotThrowAnyException();
  }

  @Test
  void fails_when_required_missing() {
    assertThatThrownBy(() -> TaskParameterValidation.requirePresent(TaskDefinition.LOG, Map.of()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("message");
  }

  @Test
  void optional_body_may_be_empty_for_dummy_email() {
    Map<String, Object> params = new HashMap<>();
    params.put("to", "a@b.com");
    params.put("subject", "hi");
    assertThatCode(() -> TaskParameterValidation.requirePresent(TaskDefinition.DUMMY_EMAIL, params))
        .doesNotThrowAnyException();
  }
}
