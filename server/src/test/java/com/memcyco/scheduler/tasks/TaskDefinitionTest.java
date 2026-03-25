package com.memcyco.scheduler.tasks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class TaskDefinitionTest {

  @Test
  void fromKey_resolves_enum_name() {
    assertThat(TaskDefinition.fromKey("LOG")).isEqualTo(TaskDefinition.LOG);
    assertThat(TaskDefinition.fromKey(" DUMMY_EMAIL ")).isEqualTo(TaskDefinition.DUMMY_EMAIL);
  }

  @Test
  void fromKey_rejects_blank() {
    assertThatThrownBy(() -> TaskDefinition.fromKey(" "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("taskKey");
  }

  @Test
  void fromKey_rejects_unknown() {
    assertThatThrownBy(() -> TaskDefinition.fromKey("NOPE"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Unknown taskKey");
  }
}
