package com.memcyco.scheduler.tasks;

public record TaskParamDefinition(
    String name,
    ParamType type,
    boolean required
) {}

