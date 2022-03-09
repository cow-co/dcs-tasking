package cowco.tasking.taskingbackend.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import cowco.tasking.taskingbackend.common.TaskingType;
import cowco.tasking.taskingbackend.rest.requests.TaskingRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.core.StringContains.containsString;

@ActiveProfiles(profiles = { "test" })
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TaskingControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetsEmptyList() throws Exception {
        mockMvc.perform(get("/api/v1/taskings")).andExpect(status().isOk()).andExpect(content().string("[]"));
    }

    @Test
    public void testCreatesSuccessfully() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("type", TaskingType.CAP);
        mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(content().string(containsString("Test Summary")))
                .andExpect(content().string(containsString("Test Location")))
                .andExpect(content().string(containsString("CAP")));
    }
}
