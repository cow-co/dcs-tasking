package cowco.tasking.taskingbackend.db;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import cowco.tasking.taskingbackend.common.TaskingType;
import cowco.tasking.taskingbackend.rest.requests.TaskingRequest;

@Entity
@Table(name = "taskings")
public class TaskingEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private long id;
    private String summary;
    private String location;
    private String serverName;

    @ElementCollection
    @CollectionTable(name = "tasking_assignment_mapping", joinColumns = {
            @JoinColumn(name = "tasking_id", referencedColumnName = "id") })
    @MapKeyColumn(name = "player_name")
    @Column(name = "assignment")
    private Map<String, String> assignments;
    private TaskingType type;

    public TaskingEntity() {
    }

    public TaskingEntity(String summary, String location, String serverName, Map<String, String> taskedPlayers,
            TaskingType type) {
        this.summary = summary;
        this.location = location;
        this.serverName = serverName;
        this.assignments = taskedPlayers;
        this.type = type;
    }

    public void fromTaskingRequest(TaskingRequest taskingRequest) {
        this.summary = taskingRequest.getSummary();
        this.location = taskingRequest.getLocation();
        this.serverName = taskingRequest.getServerName();
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

    public String getServerName() {
        return serverName;
    }

    public Map<String, String> getTaskedPlayers() {
        return assignments;
    }

    /**
     * Adds a player and their aircraft to the tasked-list, if that player is not
     * already tasked
     */
    public void addTaskedPlayer(String player, String aircraft) {
        assignments.put(player, aircraft);
    }

    public void removeTaskedPlayer(String player) {
        assignments.remove(player);
    }

    public TaskingType getType() {
        return type;
    }
}
