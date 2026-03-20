package Frontend;

public enum TimeDisplayPreference {
    HOUR_12("12-hour"),
    HOUR_24("24-hour"),
    ELAPSED("Elapsed timer");

    private final String label;

    TimeDisplayPreference(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
