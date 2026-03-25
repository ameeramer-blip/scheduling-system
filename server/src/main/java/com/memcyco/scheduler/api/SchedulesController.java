package com.memcyco.scheduler.api;

import com.memcyco.scheduler.service.ScheduleMapper;
import com.memcyco.scheduler.service.ScheduleService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedules")
public class SchedulesController {
  private final ScheduleService scheduleService;

  public SchedulesController(ScheduleService scheduleService) {
    this.scheduleService = scheduleService;
  }

  @GetMapping
  public List<ScheduleDto> list() {
    return scheduleService.list().stream().map(ScheduleMapper::toDto).toList();
  }

  @GetMapping("/{id}")
  public ScheduleDto get(@PathVariable Long id) {
    return ScheduleMapper.toDto(scheduleService.get(id));
  }

  @PostMapping
  public ScheduleDto create(@Valid @RequestBody UpsertScheduleRequest req) {
    return ScheduleMapper.toDto(scheduleService.create(req));
  }

  @PutMapping("/{id}")
  public ScheduleDto update(@PathVariable Long id, @Valid @RequestBody UpsertScheduleRequest req) {
    return ScheduleMapper.toDto(scheduleService.update(id, req));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    scheduleService.delete(id);
  }
}

