package Frontend;

public class TDPFactory {
    public TimeDisplayPreference getDefaultTDP() {
        return TimeDisplayPreference.HOUR_12;
    }

    public TimeDisplayPreference nextTDP(TimeDisplayPreference current) {
        TimeDisplayPreference[] all = TimeDisplayPreference.values();
        if (current == null) {
            return getDefaultTDP();
        }
        int next = (current.ordinal() + 1) % all.length;
        return all[next];
    }
}
