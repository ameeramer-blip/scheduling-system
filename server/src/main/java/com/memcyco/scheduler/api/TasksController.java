package com.memcyco.scheduler.api;

import com.memcyco.scheduler.tasks.TaskDefinition;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TasksController {

  @GetMapping
  public List<TaskDto> listTasks() {
    return Arrays.stream(TaskDefinition.values())
        .map(t -> new TaskDto(t.name(), t.displayName(), t.params()))
        .toList();
  }
}

