package cowco.tasking.taskingbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class TaskingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskingBackendApplication.class, args);
    }

}
