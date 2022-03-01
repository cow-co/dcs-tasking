package cowco.tasking.taskingbackend.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cowco.tasking.taskingbackend.db.TaskingEntity;
import cowco.tasking.taskingbackend.db.TaskingRepository;

@RestController
public class TaskingController {
    @Autowired
    private TaskingRepository taskingRepository;

    @GetMapping("/api/v1/taskings")
    public List<TaskingEntity> getTaskings() {
        return new ArrayList<TaskingEntity>();
    }
}
