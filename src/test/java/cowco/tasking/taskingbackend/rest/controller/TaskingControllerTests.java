package cowco.tasking.taskingbackend.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import cowco.tasking.taskingbackend.common.TaskingType;
import cowco.tasking.taskingbackend.rest.requests.TaskingRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
        TaskingRequest request = new TaskingRequest("Test Summary", "Test Location", TaskingType.CAP);
        mockMvc.perform(put("/api/v1/taskings", request)).andExpect(status().is(201))
                .andExpect(content().string(containsString("Test Summary")));
    }
}
