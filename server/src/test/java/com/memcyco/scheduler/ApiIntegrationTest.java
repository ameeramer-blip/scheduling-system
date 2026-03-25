package com.memcyco.scheduler;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memcyco.scheduler.api.UpsertScheduleRequest;
import com.memcyco.scheduler.model.ScheduleType;
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
class ApiIntegrationTest {

  @Autowired private MockMvc mvc;
  @Autowired private ObjectMapper objectMapper;

  @Test
  void tasks_are_listed() throws Exception {
    mvc.perform(get("/api/tasks"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].key").exists())
        .andExpect(jsonPath("$[0].params").isArray());
  }

  @Test
  void get_unknown_schedule_returns_404() throws Exception {
    mvc.perform(get("/api/schedules/999999"))
        .andExpect(status().isNotFound());
  }

  @Test
  void can_crud_schedule_interval() throws Exception {
    var req = new UpsertScheduleRequest(
        "Test schedule",
        true,
        "LOG",
        ScheduleType.INTERVAL,
        null,
        1,
        "MINUTES",
        null,
        null,
        null,
        "{\"message\":\"hello\"}"
    );

    String createdJson = mvc.perform(post("/api/schedules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.taskKey").value("LOG"))
        .andReturn()
        .getResponse()
        .getContentAsString();

    long id = objectMapper.readTree(createdJson).get("id").asLong();

    mvc.perform(get("/api/schedules"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(id));

    mvc.perform(delete("/api/schedules/" + id))
        .andExpect(status().isNoContent());
  }
}

