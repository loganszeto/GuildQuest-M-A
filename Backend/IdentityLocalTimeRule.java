package Backend;

/** 1:1 mapping — default so existing timed gameplay matches wall clock. */
public final class IdentityLocalTimeRule implements LocalTimeRule {

    public static final IdentityLocalTimeRule INSTANCE = new IdentityLocalTimeRule();

    private IdentityLocalTimeRule() {}

    @Override
    public long convert(long worldMillis) {
        return worldMillis;
    }
}
