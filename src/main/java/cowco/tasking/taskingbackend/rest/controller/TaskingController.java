package cowco.tasking.taskingbackend.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cowco.tasking.taskingbackend.db.TaskingEntity;
import cowco.tasking.taskingbackend.db.TaskingRepository;
import cowco.tasking.taskingbackend.rest.requests.TaskingRequest;

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
    public ResponseEntity<TaskingEntity> createTasking(@RequestBody TaskingRequest taskingRequest) {
        // TODO Validation of: summary populated, ID does not already exist
        TaskingEntity entityToCreate = new TaskingEntity();
        entityToCreate.fromTaskingRequest(taskingRequest);
        System.out.println(taskingRequest.getSummary());
        TaskingEntity createdTasking = taskingRepository.save(entityToCreate);
        System.out.println(createdTasking.getSummary());

        return new ResponseEntity<TaskingEntity>(createdTasking, HttpStatus.CREATED);
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
}
