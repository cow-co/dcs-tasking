package cowco.tasking.taskingbackend.rest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestController {
    @GetMapping("/api/v1/taskings")
    public List<Tasking> getTaskings() {

    }
}
