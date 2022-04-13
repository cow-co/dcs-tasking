package cowco.tasking.taskingbackend.common;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cowco.tasking.taskingbackend.db.Tasking;
import cowco.tasking.taskingbackend.db.TaskingRepository;

/**
 * Stores an in-memory list of all the servers - re-created from
 * the taskings if it doesn't exist yet.
 */
@Component
public class ServersCache {
    @Autowired
    private TaskingRepository taskingRepository;
    private Set<String> servers = new HashSet<>();

    public void initialise() {
        for (Tasking tasking : taskingRepository.findAll()) {
            servers.add(tasking.getServerName());
        }
    }

    /**
     * Adds a server to the cache, if it does not already exist. If the server is
     * already
     * in the cache, this method has no effect.
     * 
     * @param server The name of the server to be added
     */
    public void addServer(String server) {
        servers.add(server);
    }

    public Set<String> getServers() {
        return servers;
    }
}
