package cowco.tasking.taskingbackend.rest.requests;

import cowco.tasking.taskingbackend.common.TaskingType;

public class TaskingRequest {
    private String summary;
    private String location;
    private TaskingType type;

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
