package Frontend;

public enum Theme {
    CLASSIC("Classic"),
    DARK("Dark"),
    FOREST("Forest"),
    DESERT("Desert");

    private final String label;

    Theme(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
