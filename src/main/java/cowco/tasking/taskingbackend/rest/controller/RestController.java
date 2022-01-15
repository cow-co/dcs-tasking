package cowco.tasking.taskingbackend.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cowco.tasking.taskingbackend.rest.resourcerepresentation.TaskingDAO;

@RestController
public class RestController {
    @GetMapping("/api/v1/taskings")
    public List<TaskingDAO> getTaskings() {
        return new ArrayList<TaskingDAO>();
    }
}
