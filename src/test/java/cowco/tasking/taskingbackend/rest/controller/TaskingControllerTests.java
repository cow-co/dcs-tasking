package cowco.tasking.taskingbackend.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import cowco.tasking.taskingbackend.common.TaskingType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;

import org.json.JSONObject;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.core.StringContains.containsString;

@ActiveProfiles(profiles = { "test" })
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class TaskingControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetsEmptyList() throws Exception {
        mockMvc.perform(get("/api/v1/taskings")).andExpect(status().isOk()).andExpect(content().string("[]"));
    }

    @Test
    public void testGetsPopulatedList() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("type", TaskingType.CAP);
        mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON));

        taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary 2");
        taskingJson.put("location", "Test Location 2");
        taskingJson.put("type", TaskingType.SEAD);
        mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(get("/api/v1/taskings")).andExpect(status().isOk())
                .andExpect(content().string(containsString("Test Summary")))
                .andExpect(content().string(containsString("Test Location 2")))
                .andExpect(content().string(containsString("CAP")))
                .andExpect(content().string(containsString("SEAD")));
    }

    @Test
    public void testCreatesSuccessfully() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("type", TaskingType.CAP);
        mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(content().string(containsString("Test Summary")))
                .andExpect(content().string(containsString("Test Location")))
                .andExpect(content().string(containsString("CAP")));
    }

    @Test
    public void testUpdatesSuccessfully() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("type", TaskingType.CAP);
        MvcResult created = mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        long id = ((Number) JsonPath.read(created.getResponse().getContentAsString(), "$.id")).longValue();

        JSONObject updatedJson = new JSONObject();
        updatedJson.put("id", id);
        updatedJson.put("summary", "Test Summary");
        updatedJson.put("location", "Test Location");
        updatedJson.put("type", TaskingType.SEAD);
        mockMvc.perform(post("/api/v1/taskings").content(updatedJson.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString("Test Summary")))
                .andExpect(content().string(containsString("Test Location")))
                .andExpect(content().string(containsString("SEAD")));
    }

    @Test
    public void testFailsToUpdateNotFound() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("type", TaskingType.CAP);
        MvcResult created = mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        long id = ((Number) JsonPath.read(created.getResponse().getContentAsString(), "$.id")).longValue();

        JSONObject updatedJson = new JSONObject();
        updatedJson.put("id", id + 10);
        updatedJson.put("summary", "Test Summary");
        updatedJson.put("location", "Test Location");
        updatedJson.put("type", TaskingType.SEAD);
        mockMvc.perform(post("/api/v1/taskings").content(updatedJson.toString())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(404));
    }

    @Test
    public void testDeletesSuccessfully() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("type", TaskingType.CAP);
        MvcResult created = mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        long id = ((Number) JsonPath.read(created.getResponse().getContentAsString(), "$.id")).longValue();
        mockMvc.perform(delete("/api/v1/taskings/" + id)).andExpect(status().is(200));
    }

    @Test
    public void testDeleteFailsNotFound() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("type", TaskingType.CAP);
        MvcResult created = mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        long id = ((Number) JsonPath.read(created.getResponse().getContentAsString(), "$.id")).longValue();
        mockMvc.perform(delete("/api/v1/taskings/" + (id + 10))).andExpect(status().is(404));
    }
}
