package Backend;

/**
 * Timed Raid uses relic collection rules with a strict time window.
 * Players must collect all relics before time expires.
 */
public class TimedRaidGameBackend extends RelicHuntGameBackend {
    private static final int DEFAULT_TIME_LIMIT_SECONDS = 60;

    private int timeLimitSeconds = DEFAULT_TIME_LIMIT_SECONDS;
    private long startTimeMs = -1L;
    private boolean timedOut = false;
    /** Realm-local time mapping (default identity = wall clock). */
    private LocalTimeRule localTimeRule = IdentityLocalTimeRule.INSTANCE;

    @Override
    public void start() {
        super.start();
        timedOut = false;
        startTimeMs = System.currentTimeMillis();
    }

    @Override
    public void reset() {
        super.reset();
        timedOut = false;
        startTimeMs = -1L;
    }

    @Override
    public void advanceTurn() {
        super.advanceTurn();
        updateTimerState();
    }

    @Override
    public int isLost() {
        updateTimerState();
        if (timedOut) {
            return 0;
        }
        return super.isLost();
    }

    public void setLocalTimeRule(LocalTimeRule rule) {
        this.localTimeRule = rule != null ? rule : IdentityLocalTimeRule.INSTANCE;
    }

    public LocalTimeRule getLocalTimeRule() {
        return localTimeRule;
    }

    public int getTimeRemainingSeconds() {
        if (startTimeMs < 0L) {
            return timeLimitSeconds;
        }
        long now = System.currentTimeMillis();
        long realmElapsedMs = localTimeRule.convert(now) - localTimeRule.convert(startTimeMs);
        if (realmElapsedMs < 0L) {
            realmElapsedMs = 0L;
        }
        int remaining = timeLimitSeconds - (int) (realmElapsedMs / 1000L);
        return Math.max(0, remaining);
    }

    public boolean isTimedOut() {
        updateTimerState();
        return timedOut;
    }

    private void updateTimerState() {
        if (!isActive()) {
            return;
        }
        if (!timedOut && getTimeRemainingSeconds() <= 0) {
            timedOut = true;
        }
    }

    @Override
    public String getName() {
        return "Timed Raid";
    }

    @Override
    public String getDescription() {
        return "Collect all relics before the raid timer expires.";
    }

    @Override
    public boolean supportsCompetitive() {
        return false;
    }
}
