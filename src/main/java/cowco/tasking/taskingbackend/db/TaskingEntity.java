package cowco.tasking.taskingbackend.db;

import java.util.List;
import java.util.Set;

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
    private String serverName;
    private Set<String> taskedPlayers;
    private TaskingType type;

    public TaskingEntity() {
    }

    public TaskingEntity(String summary, String location, String serverName, Set<String> taskedPlayers,
            TaskingType type) {
        this.summary = summary;
        this.location = location;
        this.serverName = serverName;
        this.taskedPlayers = taskedPlayers;
        this.type = type;
    }

    public void fromTaskingRequest(TaskingRequest taskingRequest) {
        this.summary = taskingRequest.getSummary();
        this.location = taskingRequest.getLocation();
        this.serverName = taskingRequest.getServerName();
        this.taskedPlayers = taskingRequest.getTaskedPlayers();
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

    public Set<String> getTaskedPlayers() {
        return taskedPlayers;
    }

    /**
     * Adds a player to the tasked-list, if that player is not already tasked
     * 
     * @param player
     */
    public void addTaskedPlayer(String player) {
        taskedPlayers.add(player);
    }

    public TaskingType getType() {
        return type;
    }
}
