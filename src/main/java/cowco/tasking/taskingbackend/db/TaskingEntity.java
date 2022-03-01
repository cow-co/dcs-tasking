package cowco.tasking.taskingbackend.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import cowco.tasking.taskingbackend.common.TaskingType;

@Entity
public class TaskingEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;
    private String summary;
    private String location;
    private TaskingType type;

    public TaskingEntity(String summary, String location, TaskingType type) {
        this.summary = summary;
        this.location = location;
        this.type = type;
    }

}
