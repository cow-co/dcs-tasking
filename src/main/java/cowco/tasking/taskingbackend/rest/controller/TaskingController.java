package cowco.tasking.taskingbackend.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        TaskingEntity entityToCreate = new TaskingEntity();
        entityToCreate.fromTaskingRequest(taskingRequest);
        System.out.println(taskingRequest.getSummary());
        TaskingEntity createdTasking = taskingRepository.save(entityToCreate);
        System.out.println(createdTasking.getSummary());

        return new ResponseEntity<TaskingEntity>(createdTasking, HttpStatus.CREATED);
    }
}
