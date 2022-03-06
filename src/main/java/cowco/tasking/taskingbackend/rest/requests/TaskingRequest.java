package cowco.tasking.taskingbackend.rest.requests;

import cowco.tasking.taskingbackend.common.TaskingType;

public class TaskingRequest {
    private String summary;
    private String location;
    private TaskingType type;

    public TaskingRequest(String summary, String location, TaskingType type) {
        this.summary = summary;
        this.location = location;
        this.type = type;
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
