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

import org.json.JSONArray;
import org.json.JSONObject;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void testGetsPopulatedTaskingsList() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("serverName", "Hoggit");
        taskingJson.put("type", TaskingType.CAP);
        mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON));

        taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary 2");
        taskingJson.put("location", "Test Location 2");
        taskingJson.put("serverName", "Hoggit");
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
    public void testGetsPopulatedServersList() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("serverName", "Hoggit");
        taskingJson.put("type", TaskingType.CAP);
        mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON));

        taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary 2");
        taskingJson.put("location", "Test Location 2");
        taskingJson.put("serverName", "4YA");
        taskingJson.put("type", TaskingType.SEAD);
        mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON));

        MvcResult response = mockMvc.perform(get("/api/v1/servers")).andReturn();

        JSONObject json = new JSONObject(response.getResponse().getContentAsString());
        JSONArray servers = json.getJSONArray("servers");
        assertEquals(servers.length(), 2);
        assertTrue(servers.getString(0).equalsIgnoreCase("4YA")
                || servers.getString(0).equalsIgnoreCase("Hoggit"));
    }

    @Test
    public void testGetsPopulatedServersListWithDuplicate() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("serverName", "Hoggit");
        taskingJson.put("type", TaskingType.CAP);
        mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON));

        taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary 2");
        taskingJson.put("location", "Test Location 2");
        taskingJson.put("serverName", "4YA");
        taskingJson.put("type", TaskingType.SEAD);
        mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON));

        taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary 3");
        taskingJson.put("location", "Test Location 3");
        taskingJson.put("serverName", "4YA");
        taskingJson.put("type", TaskingType.SEAD);
        mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON));

        MvcResult response = mockMvc.perform(get("/api/v1/servers")).andReturn();

        JSONObject json = new JSONObject(response.getResponse().getContentAsString());
        JSONArray servers = json.getJSONArray("servers");
        assertEquals(servers.length(), 2);
    }

    @Test
    public void testCreatesSuccessfully() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("serverName", "Hoggit");
        taskingJson.put("type", TaskingType.CAP);
        mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(content().string(containsString("Test Summary")))
                .andExpect(content().string(containsString("Test Location")))
                .andExpect(content().string(containsString("CAP")));
    }

    @Test
    public void testFailsToCreateNoSummary() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", null);
        taskingJson.put("location", "Test Location");
        taskingJson.put("serverName", "Hoggit");
        taskingJson.put("type", TaskingType.SEAD);
        MvcResult response = mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400)).andReturn();

        JSONObject json = new JSONObject(response.getResponse().getContentAsString());
        JSONArray errors = json.getJSONArray("errors");
        assertEquals(errors.length(), 1);
    }

    @Test
    public void testFailsToCreateEmptySummary() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "     ");
        taskingJson.put("location", "Test Location");
        taskingJson.put("serverName", "Hoggit");
        taskingJson.put("type", TaskingType.SEAD);
        MvcResult response = mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400)).andReturn();

        JSONObject json = new JSONObject(response.getResponse().getContentAsString());
        JSONArray errors = json.getJSONArray("errors");
        assertEquals(errors.length(), 1);
    }

    @Test
    public void testFailsToCreateEmptyServerName() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary 2");
        taskingJson.put("location", "Test Location");
        taskingJson.put("serverName", "  ");
        taskingJson.put("type", TaskingType.SEAD);
        MvcResult response = mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400)).andReturn();

        JSONObject json = new JSONObject(response.getResponse().getContentAsString());
        JSONArray errors = json.getJSONArray("errors");
        assertEquals(errors.length(), 1);
    }

    @Test
    public void testUpdatesSuccessfully() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("serverName", "Hoggit");
        taskingJson.put("type", TaskingType.CAP);
        MvcResult created = mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        long id = ((Number) JsonPath.read(created.getResponse().getContentAsString(), "tasking.id"))
                .longValue();

        JSONObject updatedJson = new JSONObject();
        updatedJson.put("summary", "Test Summary");
        updatedJson.put("location", "Test Location");
        updatedJson.put("serverName", "Hoggit");
        updatedJson.put("type", TaskingType.SEAD);
        mockMvc.perform(post("/api/v1/taskings/" + id).content(updatedJson.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString("Test Summary")))
                .andExpect(content().string(containsString("Test Location")))
                .andExpect(content().string(containsString("SEAD")));
    }

    @Test
    public void testFailsToUpdateNoSummary() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("serverName", "Hoggit");
        taskingJson.put("type", TaskingType.CAP);
        MvcResult created = mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        long id = ((Number) JsonPath.read(created.getResponse().getContentAsString(), "tasking.id"))
                .longValue();

        JSONObject updatedJson = new JSONObject();
        updatedJson.put("summary", null);
        updatedJson.put("location", "Test Location");
        updatedJson.put("serverName", "Hoggit");
        updatedJson.put("type", TaskingType.SEAD);
        MvcResult response = mockMvc.perform(post("/api/v1/taskings/" + id).content(updatedJson.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400)).andReturn();

        JSONObject json = new JSONObject(response.getResponse().getContentAsString());
        JSONArray errors = json.getJSONArray("errors");
        assertEquals(errors.length(), 1);
    }

    @Test
    public void testFailsToUpdateEmptySummary() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("serverName", "Hoggit");
        taskingJson.put("type", TaskingType.CAP);
        MvcResult created = mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        long id = ((Number) JsonPath.read(created.getResponse().getContentAsString(), "tasking.id"))
                .longValue();

        JSONObject updatedJson = new JSONObject();
        updatedJson.put("summary", "       ");
        updatedJson.put("location", "Test Location");
        updatedJson.put("serverName", "Hoggit");
        updatedJson.put("type", TaskingType.SEAD);
        MvcResult response = mockMvc.perform(post("/api/v1/taskings/" + id).content(updatedJson.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400)).andReturn();

        JSONObject json = new JSONObject(response.getResponse().getContentAsString());
        JSONArray errors = json.getJSONArray("errors");
        assertEquals(errors.length(), 1);
    }

    @Test
    public void testFailsToUpdateEmptyServerName() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("serverName", "Hoggit");
        taskingJson.put("type", TaskingType.CAP);
        MvcResult created = mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        long id = ((Number) JsonPath.read(created.getResponse().getContentAsString(), "tasking.id"))
                .longValue();

        JSONObject updatedJson = new JSONObject();
        updatedJson.put("summary", "Test Summary 2");
        updatedJson.put("location", "Test Location");
        updatedJson.put("serverName", "  ");
        updatedJson.put("type", TaskingType.SEAD);
        MvcResult response = mockMvc.perform(post("/api/v1/taskings/" + id).content(updatedJson.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400)).andReturn();

        JSONObject json = new JSONObject(response.getResponse().getContentAsString());
        JSONArray errors = json.getJSONArray("errors");
        assertEquals(errors.length(), 1);
    }

    @Test
    public void testFailsToUpdateNotFound() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("serverName", "Hoggit");
        taskingJson.put("type", TaskingType.CAP);
        MvcResult created = mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        long id = ((Number) JsonPath.read(created.getResponse().getContentAsString(), "tasking.id"))
                .longValue();

        JSONObject updatedJson = new JSONObject();
        updatedJson.put("summary", "Test Summary");
        updatedJson.put("location", "Test Location");
        updatedJson.put("serverName", "Hoggit");
        updatedJson.put("type", TaskingType.SEAD);
        mockMvc.perform(post("/api/v1/taskings/" + (id + 10)).content(updatedJson.toString())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(404));
    }

    @Test
    public void testDeletesSuccessfully() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("serverName", "Hoggit");
        taskingJson.put("type", TaskingType.CAP);
        MvcResult created = mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        long id = ((Number) JsonPath.read(created.getResponse().getContentAsString(), "tasking.id"))
                .longValue();
        mockMvc.perform(delete("/api/v1/taskings/" + id)).andExpect(status().is(200));
    }

    @Test
    public void testDeleteFailsNotFound() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("serverName", "Hoggit");
        taskingJson.put("type", TaskingType.CAP);
        MvcResult created = mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        long id = ((Number) JsonPath.read(created.getResponse().getContentAsString(), "tasking.id"))
                .longValue();
        mockMvc.perform(delete("/api/v1/taskings/" + (id + 10))).andExpect(status().is(404));
    }

    @Test
    public void testAssignsUser() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("serverName", "Hoggit");
        taskingJson.put("type", TaskingType.CAP);
        MvcResult created = mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        long id = ((Number) JsonPath.read(created.getResponse().getContentAsString(), "tasking.id"))
                .longValue();

        JSONObject assignment = new JSONObject();
        assignment.put("player", "player1");
        assignment.put("aircraft", "F-16");
        MvcResult response = mockMvc.perform(post("/api/v1/taskings/" + id + "/assign").content(assignment.toString()))
                .andReturn();
        JSONObject json = new JSONObject(response.getResponse().getContentAsString());
        JSONObject tasking = json.getJSONObject("tasking");
        JSONArray tasked = tasking.getJSONArray("taskedPlayers");
        assertEquals(1, tasked.length());

        assignment.put("player", "player2");
        assignment.put("aircraft", "F-18");
        response = mockMvc.perform(post("/api/v1/taskings/" + id + "/assign").content(assignment.toString()))
                .andReturn();

        json = new JSONObject(response.getResponse().getContentAsString());
        tasking = json.getJSONObject("tasking");
        tasked = tasking.getJSONArray("taskedPlayers");
        assertEquals(2, tasked.length());
    }

    @Test
    public void testAssignsUserDuplicate() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("serverName", "Hoggit");
        taskingJson.put("type", TaskingType.CAP);
        MvcResult created = mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        long id = ((Number) JsonPath.read(created.getResponse().getContentAsString(), "tasking.id"))
                .longValue();

        JSONObject assignment = new JSONObject();
        assignment.put("player", "player1");
        assignment.put("aircraft", "F-16");
        mockMvc.perform(post("/api/v1/taskings/" + id + "/assign").content(assignment.toString()));
        MvcResult response = mockMvc.perform(post("/api/v1/taskings/" + id + "/assign").content(assignment.toString())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        JSONObject json = new JSONObject(response.getResponse().getContentAsString());
        JSONObject tasking = json.getJSONObject("tasking");
        JSONArray tasked = tasking.getJSONArray("taskedPlayers");
        assertEquals(1, tasked.length());
    }

    @Test
    public void testUnassignsUser() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("serverName", "Hoggit");
        taskingJson.put("type", TaskingType.CAP);
        MvcResult created = mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        long id = ((Number) JsonPath.read(created.getResponse().getContentAsString(), "tasking.id"))
                .longValue();

        JSONObject assignment = new JSONObject();
        assignment.put("player", "player1");
        assignment.put("aircraft", "F-16");
        MvcResult response = mockMvc.perform(post("/api/v1/taskings/" + id + "/assign").content(assignment.toString())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        response = mockMvc.perform(post("/api/v1/taskings/" + id + "/unassign/player1")).andReturn();

        JSONObject json = new JSONObject(response.getResponse().getContentAsString());
        JSONObject tasking = json.getJSONObject("tasking");
        JSONArray tasked = tasking.getJSONArray("taskedPlayers");
        assertEquals(0, tasked.length());
    }

    @Test
    public void testUnassignsNonassignedUser() throws Exception {
        JSONObject taskingJson = new JSONObject();
        taskingJson.put("summary", "Test Summary");
        taskingJson.put("location", "Test Location");
        taskingJson.put("serverName", "Hoggit");
        taskingJson.put("type", TaskingType.CAP);
        MvcResult created = mockMvc.perform(put("/api/v1/taskings").content(taskingJson.toString())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        long id = ((Number) JsonPath.read(created.getResponse().getContentAsString(), "tasking.id"))
                .longValue();

        JSONObject assignment = new JSONObject();
        assignment.put("player", "player1");
        assignment.put("aircraft", "F-16");
        MvcResult response = mockMvc
                .perform(post("/api/v1/taskings/" + id + "/assign").content(assignment.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        response = mockMvc.perform(post("/api/v1/taskings/" + id + "/unassign/player2")).andReturn();

        JSONObject json = new JSONObject(response.getResponse().getContentAsString());
        JSONObject tasking = json.getJSONObject("tasking");
        JSONArray tasked = tasking.getJSONArray("taskedPlayers");
        assertEquals(1, tasked.length());
    }
}
