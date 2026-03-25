package com.memcyco.scheduler.api;

import com.memcyco.scheduler.tasks.TaskParamDefinition;
import java.util.List;

public record TaskDto(
    String key,
    String displayName,
    List<TaskParamDefinition> params
) {}

