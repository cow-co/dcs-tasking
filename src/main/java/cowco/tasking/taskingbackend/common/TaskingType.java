package cowco.tasking.taskingbackend.common;

public enum TaskingType {

    SEAD("SEAD"),
    CAP("CAP"),
    CAS("CAS");

    private String name;

    private TaskingType(String name) {
        this.name = name;
    }
}
