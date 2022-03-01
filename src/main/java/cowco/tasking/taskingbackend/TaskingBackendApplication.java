package cowco.tasking.taskingbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class TaskingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskingBackendApplication.class, args);
	}

}
