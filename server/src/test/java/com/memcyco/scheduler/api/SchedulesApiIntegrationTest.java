package com.memcyco.scheduler.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memcyco.scheduler.model.ScheduleType;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
class SchedulesApiIntegrationTest {

  @Autowired private MockMvc mvc;
  @Autowired private ObjectMapper objectMapper;

  @Test
  void create_rejects_unknown_task_key() throws Exception {
    var req = new UpsertScheduleRequest(
        "x", true, "NOT_A_TASK", ScheduleType.INTERVAL, null, 1, "MINUTES",
        null, null, null, "{}");
    mvc.perform(post("/api/schedules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void create_and_get_by_id_and_update() throws Exception {
    var create = new UpsertScheduleRequest(
        "My job", true, "LOG", ScheduleType.INTERVAL, null, 2, "HOURS",
        null, null, null, "{\"message\":\"ping\"}");

    String json = mvc.perform(post("/api/schedules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(create)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("My job"))
        .andReturn().getResponse().getContentAsString();

    long id = objectMapper.readTree(json).get("id").asLong();

    mvc.perform(get("/api/schedules/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.taskKey").value("LOG"));

    var update = new UpsertScheduleRequest(
        "Renamed", false, "LOG", ScheduleType.ONCE,
        Instant.parse("2035-06-01T12:00:00Z"),
        null, null, null, null, null, "{\"message\":\"pong\"}");

    mvc.perform(put("/api/schedules/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(update)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Renamed"))
        .andExpect(jsonPath("$.enabled").value(false));
  }
}
