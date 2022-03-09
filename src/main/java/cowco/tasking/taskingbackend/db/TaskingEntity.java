package cowco.tasking.taskingbackend.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import cowco.tasking.taskingbackend.common.TaskingType;
import cowco.tasking.taskingbackend.rest.requests.TaskingRequest;

@Entity
public class TaskingEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;
    private String summary;
    private String location;
    private TaskingType type;

    public TaskingEntity() {
    }

    public TaskingEntity(String summary, String location, TaskingType type) {
        this.summary = summary;
        this.location = location;
        this.type = type;
    }

    public void fromTaskingRequest(TaskingRequest taskingRequest) {
        this.summary = taskingRequest.getSummary();
        this.location = taskingRequest.getLocation();
        this.type = taskingRequest.getType();
    }

    public String getSummary() {
        return summary;
    }

    public long getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public TaskingType getType() {
        return type;
    }
}
