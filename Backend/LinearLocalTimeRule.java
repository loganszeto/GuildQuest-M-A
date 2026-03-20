package Backend;

/**
 * Linear transform {@code realm = worldMillis * scale + offsetMillis}.
 * Differences {@code convert(t2) - convert(t1)} scale elapsed time by {@code scale}, so raids can run
 * "faster" or "slower" in realm time without changing the wall-clock {@link System#currentTimeMillis()}.
 */
public final class LinearLocalTimeRule implements LocalTimeRule {

    private final double scale;
    private final long offsetMillis;

    public LinearLocalTimeRule(double scale, long offsetMillis) {
        if (scale <= 0 || Double.isNaN(scale) || Double.isInfinite(scale)) {
            throw new IllegalArgumentException("scale must be a positive finite number");
        }
        this.scale = scale;
        this.offsetMillis = offsetMillis;
    }

    @Override
    public long convert(long worldMillis) {
        return (long) (worldMillis * scale + offsetMillis);
    }
}
