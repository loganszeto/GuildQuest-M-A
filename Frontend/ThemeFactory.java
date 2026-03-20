package Frontend;

public class ThemeFactory {
    public Theme getDefaultTheme() {
        return Theme.CLASSIC;
    }

    public Theme nextTheme(Theme current) {
        Theme[] all = Theme.values();
        if (current == null) {
            return getDefaultTheme();
        }
        int next = (current.ordinal() + 1) % all.length;
        return all[next];
    }
}
