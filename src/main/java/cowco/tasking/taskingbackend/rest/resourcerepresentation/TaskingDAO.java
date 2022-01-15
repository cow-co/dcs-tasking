package cowco.tasking.taskingbackend.rest.resourcerepresentation;

import cowco.tasking.taskingbackend.common.TaskingType;

// This holds the tasking data that is exposed via the REST API
// There may be some data that we do not want to expose (DB-specific fields perhaps)
public class TaskingDAO {
    private String summary;
    private String location;
    private TaskingType type;
}
