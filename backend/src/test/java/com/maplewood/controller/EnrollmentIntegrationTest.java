package com.maplewood.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.springframework.test.context.ActiveProfiles;


import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class EnrollmentIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @Test
    void enrollingTwice_returns409() throws Exception {
    // First unenroll to guarantee clean state
    mvc.perform(delete("/api/enrollments/100/sections/1"))
            .andExpect(result -> {}); 

    var body = om.writeValueAsString(Map.of("studentId", 100, "sectionId", 1));

    mvc.perform(post("/api/enrollments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isNoContent());

    mvc.perform(post("/api/enrollments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isConflict());
    mvc.perform(delete("/api/enrollments/100/sections/1")) /* To handle cleanup */
            .andExpect(status().isNoContent());
    }
    
}