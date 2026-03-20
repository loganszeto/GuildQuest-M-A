package Backend;

/**
 * Converts a world (wall-clock) instant to a realm-local time coordinate.
 * Reused from teammate Nathaneil Heit's {@code LocalTimeRule} design (interface with {@code convert(long)}).
 */
public interface LocalTimeRule {

    /**
     * @param worldMillis epoch millis or other monotonic baseline in "world" time
     * @return corresponding local/realm time in the same units (typically millis)
     */
    long convert(long worldMillis);
}
