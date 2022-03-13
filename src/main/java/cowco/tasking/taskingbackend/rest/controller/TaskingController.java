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

import cowco.tasking.taskingbackend.db.TaskingEntity;
import cowco.tasking.taskingbackend.db.TaskingRepository;
import cowco.tasking.taskingbackend.rest.requests.TaskingRequest;
import net.minidev.json.JSONObject;

@RestController
public class TaskingController {
    @Autowired
    private TaskingRepository taskingRepository;

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
     * TODO Implement validation
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

        if (taskingRepository.findById(taskingRequest.getId()).isPresent()) {
            errors.add("Tasking with that ID already exists!");
        }

        if (errors.size() == 0) {
            TaskingEntity entityToCreate = new TaskingEntity();
            entityToCreate.fromTaskingRequest(taskingRequest);
            TaskingEntity createdTasking = taskingRepository.save(entityToCreate);
            responseData.put("tasking", createdTasking);
            status = HttpStatus.CREATED;
        } else {
            responseData.put("errors", errors);
        }

        return new ResponseEntity<JSONObject>(responseData, status);
    }

    /**
     * Updates a tasking based on the requested details.
     * TODO Implement validation
     * 
     * @param taskingRequest The details of the update to be made
     * @return A response containing either the updated tasking, or details of an
     *         error
     */
    @PostMapping(value = "/api/v1/taskings", consumes = "application/json", produces = "application/json")
    public ResponseEntity<TaskingEntity> updateTasking(@RequestBody TaskingRequest taskingRequest) {
        ResponseEntity<TaskingEntity> response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        Optional<TaskingEntity> entityRecord = taskingRepository.findById(taskingRequest.getId());

        if (entityRecord.isPresent()) {
            TaskingEntity entityToUpdate = entityRecord.get();
            entityToUpdate.fromTaskingRequest(taskingRequest);
            TaskingEntity updatedTasking = taskingRepository.save(entityToUpdate);
            response = new ResponseEntity<>(updatedTasking, HttpStatus.OK);
        }

        return response;
    }

    /**
     * Deletes the given tasking.
     * 
     * @param taskingId The ID of the tasking to be deleted
     * @return A response containing either a success message or details of an error
     */
    @DeleteMapping(value = "/api/v1/taskings/{taskingId}", produces = "application/json")
    public ResponseEntity<String> deleteTasking(@PathVariable long taskingId) {
        ResponseEntity<String> response = new ResponseEntity<>("Tasking not found", HttpStatus.NOT_FOUND);
        Optional<TaskingEntity> entityRecord = taskingRepository.findById(taskingId);

        if (entityRecord.isPresent()) {
            taskingRepository.deleteById(taskingId);
            response = new ResponseEntity<>("Successfully deleted tasking", HttpStatus.OK);
        }

        return response;
    }
}
