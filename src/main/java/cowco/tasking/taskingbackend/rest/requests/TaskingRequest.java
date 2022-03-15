package cowco.tasking.taskingbackend.rest.requests;

import java.util.Set;

import cowco.tasking.taskingbackend.common.TaskingType;

public class TaskingRequest {
    private String summary;
    private String location;
    private String serverName;
    private TaskingType type;

    public TaskingRequest() {

    }

    public TaskingRequest(String summary, String location, String serverName, TaskingType type) {
        this.summary = summary;
        this.location = location;
        this.serverName = serverName;
        this.type = type;
    }

    public String getSummary() {
        return summary;
    }

    public String getLocation() {
        return location;
    }

    public String getServerName() {
        return serverName;
    }

    public TaskingType getType() {
        return type;
    }
}
