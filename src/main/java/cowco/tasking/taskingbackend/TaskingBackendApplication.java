package cowco.tasking.taskingbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class TaskingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskingBackendApplication.class, args);
	}

}
