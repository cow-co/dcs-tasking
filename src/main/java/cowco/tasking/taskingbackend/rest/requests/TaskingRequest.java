package cowco.tasking.taskingbackend.rest.requests;

import cowco.tasking.taskingbackend.common.TaskingType;

public class TaskingRequest {
    private long id = -1; // Initialise to a vaLue that will never be given by the auto-generation
    private String summary;
    private String location;
    private TaskingType type;

    public TaskingRequest() {

    }

    public TaskingRequest(long id, String summary, String location, TaskingType type) {
        this.id = id;
        this.summary = summary;
        this.location = location;
        this.type = type;
    }

    public TaskingRequest(String summary, String location, TaskingType type) {
        this.summary = summary;
        this.location = location;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public String getSummary() {
        return summary;
    }

    public String getLocation() {
        return location;
    }

    public TaskingType getType() {
        return type;
    }
}
