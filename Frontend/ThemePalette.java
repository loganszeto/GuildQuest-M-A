package Frontend;

import java.awt.Color;

public class ThemePalette {
    public final Color frameBg;
    public final Color panelBg;
    public final Color accentBg;
    public final Color titleFg;
    public final Color textFg;
    public final Color buttonBg;
    public final Color buttonFg;

    public ThemePalette(Color frameBg, Color panelBg, Color accentBg, Color titleFg, Color textFg, Color buttonBg, Color buttonFg) {
        this.frameBg = frameBg;
        this.panelBg = panelBg;
        this.accentBg = accentBg;
        this.titleFg = titleFg;
        this.textFg = textFg;
        this.buttonBg = buttonBg;
        this.buttonFg = buttonFg;
    }

    public static ThemePalette forTheme(Theme theme) {
        if (theme == null) {
            return classic();
        }
        switch (theme) {
            case DARK:
                return new ThemePalette(
                    new Color(18, 18, 18),
                    new Color(28, 28, 28),
                    new Color(45, 45, 45),
                    new Color(230, 230, 230),
                    new Color(220, 220, 220),
                    new Color(70, 70, 70),
                    Color.WHITE
                );
            case FOREST:
                return new ThemePalette(
                    new Color(22, 45, 34),
                    new Color(30, 65, 48),
                    new Color(47, 87, 67),
                    new Color(201, 242, 207),
                    new Color(228, 248, 232),
                    new Color(64, 108, 83),
                    Color.WHITE
                );
            case DESERT:
                return new ThemePalette(
                    new Color(77, 58, 39),
                    new Color(102, 78, 52),
                    new Color(128, 99, 66),
                    new Color(255, 232, 184),
                    new Color(255, 245, 220),
                    new Color(166, 124, 82),
                    Color.WHITE
                );
            case CLASSIC:
            default:
                return classic();
        }
    }

    private static ThemePalette classic() {
        return new ThemePalette(
            new Color(25, 25, 60),
            new Color(45, 45, 80),
            new Color(35, 35, 70),
            new Color(255, 215, 0),
            Color.WHITE,
            new Color(200, 50, 50),
            Color.WHITE
        );
    }
}
