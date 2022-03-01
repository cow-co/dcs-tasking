package cowco.tasking.taskingbackend.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cowco.tasking.taskingbackend.db.TaskingEntity;

@RestController
public class TaskingController {
    @GetMapping("/api/v1/taskings")
    public List<TaskingEntity> getTaskings() {
        return new ArrayList<TaskingEntity>();
    }
}
