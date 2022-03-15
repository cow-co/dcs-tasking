package cowco.tasking.taskingbackend.rest.requests;

public class AssignmentRequest {
    private String player;
    private String aircraft;

    public AssignmentRequest(String player, String aircraft) {
        this.player = player;
        this.aircraft = aircraft;
    }

    public String getPlayer() {
        return player;
    }

    public String getAircraft() {
        return aircraft;
    }
}
