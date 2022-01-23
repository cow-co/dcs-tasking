package cowco.tasking.taskingbackend.rest.resourcerepresentation;

import cowco.tasking.taskingbackend.common.TaskingType;

// This holds the tasking data that is exposed via the REST API
// There may be some data that we do not want to expose (DB-specific fields perhaps)
public class TaskingDAO {
    private String summary;
    private String location;
    private TaskingType type;

    public TaskingDAO(String summary, String location, TaskingType type) {
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
