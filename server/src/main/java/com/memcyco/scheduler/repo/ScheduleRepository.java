package com.memcyco.scheduler.repo;

import com.memcyco.scheduler.model.ScheduleEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {

  List<ScheduleEntity> findAllByOrderByIdDesc();
}

