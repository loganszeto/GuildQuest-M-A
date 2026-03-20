package Frontend;

import Backend.RealmSpace;

public class Settings {
    private RealmSpace currentRealm;
    private Theme theme;
    private TimeDisplayPreference tdp;
    private final ThemeFactory tf = new ThemeFactory();
    private final TDPFactory tdpf = new TDPFactory();

    public Settings() {
        currentRealm = null;
        theme = tf.getDefaultTheme();
        tdp = tdpf.getDefaultTDP();
    }

    public RealmSpace getCurrentRealm() {
        return currentRealm;
    }

    public void setCurrentRealm(RealmSpace currentRealm) {
        this.currentRealm = currentRealm;
    }

    public Theme getTheme() {
        return theme;
    }

    public TimeDisplayPreference getTdp() {
        return tdp;
    }

    public void updateSettings(int i) {
        if (i == 0) {
            theme = tf.nextTheme(theme);
        }
        if (i == 1) {
            tdp = tdpf.nextTDP(tdp);
        }
    }
}
