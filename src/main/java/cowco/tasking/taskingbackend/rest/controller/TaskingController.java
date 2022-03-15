package cowco.tasking.taskingbackend.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cowco.tasking.taskingbackend.common.ServersCache;
import cowco.tasking.taskingbackend.db.TaskingEntity;
import cowco.tasking.taskingbackend.db.TaskingRepository;
import cowco.tasking.taskingbackend.rest.requests.TaskingRequest;
import net.minidev.json.JSONObject;

// TODO Perhaps find a way to reduce the duplication in the response/error code? Make a Response class for this?
@RestController
public class TaskingController {
    @Autowired
    private TaskingRepository taskingRepository;
    @Autowired
    private ServersCache serversCache;

    /**
     * Retrieves a list of the taskings in the database.
     * TODO Allow a filter/sorting to be passed in (optionally)
     * 
     * @return The taskings that exist in the database
     */
    @GetMapping(value = "/api/v1/taskings", produces = "application/json")
    public List<TaskingEntity> getTaskings() {
        List<TaskingEntity> taskings = new ArrayList<>();
        taskingRepository.findAll().forEach(taskings::add);
        return taskings;
    }

    /**
     * Creates a tasking based on the requested details.
     * 
     * @param taskingRequest The details of the tasking to be created
     * @return A response containing either the created tasking, or details of an
     *         error
     */
    @PutMapping(value = "/api/v1/taskings", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSONObject> createTasking(@RequestBody TaskingRequest taskingRequest) {
        JSONObject responseData = new JSONObject();
        List<String> errors = new ArrayList<>();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (taskingRequest.getSummary() == null || taskingRequest.getSummary().trim().isEmpty()) {
            errors.add("Summary not populated!");
        }

        if (taskingRequest.getServerName() == null || taskingRequest.getServerName().trim().isEmpty()) {
            errors.add("Server name not populated!");
            status = HttpStatus.BAD_REQUEST;
        }

        if (errors.size() == 0) {
            TaskingEntity entityToCreate = new TaskingEntity();
            entityToCreate.fromTaskingRequest(taskingRequest);
            TaskingEntity createdTasking = taskingRepository.save(entityToCreate);
            serversCache.addServer(taskingRequest.getServerName());

            responseData.put("tasking", createdTasking);
            status = HttpStatus.CREATED;
        }

        responseData.put("errors", errors);

        return new ResponseEntity<JSONObject>(responseData, status);
    }

    /**
     * Updates a tasking based on the requested details.
     * 
     * @param taskingRequest The details of the update to be made
     * @return A response containing either the updated tasking, or details of an
     *         error
     */
    @PostMapping(value = "/api/v1/taskings/{taskingId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSONObject> updateTasking(@PathVariable long taskingId,
            @RequestBody TaskingRequest taskingRequest) {
        JSONObject responseData = new JSONObject();
        List<String> errors = new ArrayList<>();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (taskingRequest.getSummary() == null || taskingRequest.getSummary().trim().isEmpty()) {
            errors.add("Summary not populated!");
        }

        if (taskingRequest.getServerName() == null || taskingRequest.getServerName().trim().isEmpty()) {
            errors.add("Server name not populated!");
        }

        if (errors.size() == 0) {
            Optional<TaskingEntity> entityRecord = taskingRepository.findById(taskingId);

            if (entityRecord.isPresent()) {
                TaskingEntity entityToUpdate = entityRecord.get();
                entityToUpdate.fromTaskingRequest(taskingRequest);
                TaskingEntity updatedTasking = taskingRepository.save(entityToUpdate);
                serversCache.addServer(taskingRequest.getServerName());

                status = HttpStatus.OK;
                responseData.put("tasking", updatedTasking);
            } else {
                errors.add("Tasking with ID " + taskingId + " was not found!");
                status = HttpStatus.NOT_FOUND;
            }
        }

        responseData.put("errors", errors);

        return new ResponseEntity<JSONObject>(responseData, status);
    }

    /**
     * Updates a tasking based on the requested details.
     * 
     * @param taskingRequest The details of the update to be made
     * @return A response containing either the updated tasking, or details of an
     *         error
     */
    @PostMapping(value = "/api/v1/taskings/{taskingId}/{player}", produces = "application/json")
    public ResponseEntity<JSONObject> taskPlayer(@PathVariable long taskingId, @PathVariable String player) {
        JSONObject responseData = new JSONObject();
        List<String> errors = new ArrayList<>();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Optional<TaskingEntity> entityRecord = taskingRepository.findById(taskingId);

        if (entityRecord.isPresent()) {
            TaskingEntity entityToUpdate = entityRecord.get();
            entityToUpdate.addTaskedPlayer(player);
            TaskingEntity updatedTasking = taskingRepository.save(entityToUpdate);
            serversCache.addServer(taskingRequest.getServerName());

            status = HttpStatus.OK;
            responseData.put("tasking", updatedTasking);
        } else {
            errors.add("Tasking with ID " + taskingId + " was not found!");
            status = HttpStatus.NOT_FOUND;
        }

        responseData.put("errors", errors);

        return new ResponseEntity<JSONObject>(responseData, status);
    }

    /**
     * Deletes the given tasking.
     * 
     * @param taskingId The ID of the tasking to be deleted
     * @return A response containing either a success message or details of an error
     */
    @DeleteMapping(value = "/api/v1/taskings/{taskingId}", produces = "application/json")
    public ResponseEntity<JSONObject> deleteTasking(@PathVariable long taskingId) {
        JSONObject responseData = new JSONObject();
        List<String> errors = new ArrayList<>();
        HttpStatus status;
        Optional<TaskingEntity> entityRecord = taskingRepository.findById(taskingId);

        if (entityRecord.isPresent()) {
            taskingRepository.deleteById(taskingId);
            status = HttpStatus.OK;
        } else {
            errors.add("Tasking with ID " + taskingId + " was not found!");
            status = HttpStatus.NOT_FOUND;
        }

        responseData.put("errors", errors);

        return new ResponseEntity<JSONObject>(responseData, status);
    }

    /**
     * Since I'm using free versions of database services, I'm going to be careful
     * with how much data I store in the DB - so I won't have a separate "servers"
     * table.
     * Instead, I'll just create a list each time it's needed.
     * Slightly janky, but cuts the risk of incurring costs.
     */
    @GetMapping(value = "/api/v1/servers", produces = "application/json")
    public ResponseEntity<JSONObject> getServers() {
        serversCache.initialise();
        JSONObject responseData = new JSONObject();
        responseData.put("servers", serversCache.getServers());
        return new ResponseEntity<JSONObject>(responseData, HttpStatus.OK);
    }
}
