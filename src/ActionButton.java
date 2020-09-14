import java.awt.*;
import java.awt.event.MouseEvent;

class ActionButton {

    boolean isEnabled;

    static Color enabledColor = new Color(0, 145, 17);
    static Font font = new Font("Arial", Font.PLAIN, 18);

    private int x;
    private int y;
    private int baseX = 218;
    private int baseY;
    private int baseYMultiplier = 44;
    private int baseStringX;
    private int baseStringY = 23;
    private int stringX;
    private int stringY;
    private final short width = 180;
    private final short height = 32;
    private static byte declarationIndex = 0;

    private String text;
    private final Color disabledColor = Color.DARK_GRAY;
    private final Color enabledAccentColor = enabledColor.darker();
    private final Color disabledAccentColor = disabledColor.darker();
    private final Color hoverColor = enabledColor.brighter();
    private final Color textColor = Color.WHITE;
    private Color currentColor = new Color(0, 170, 17);

    ActionButton(String text, boolean isEnabledByDefault, FontMetrics fm) {
        this(text, isEnabledByDefault, fm, 0);
    }

    ActionButton(String text, boolean isEnabledByDefault, FontMetrics fm, int offset) {
        this.text = text;
        isEnabled = isEnabledByDefault;
        // auto-calculate initial coordinates based on order of declaration
        baseY = (declarationIndex += (offset + 1)) * baseYMultiplier;
        int stringWidth = fm.stringWidth(text);
        baseStringX = (width / 2) - (stringWidth / 2);
        tick();
    }

    void checkClick(MouseEvent e) {
        if (isClickInBounds(e.getX(), e.getY()) && isEnabled) {
            onClick();
        }
    }

    void onClick() {

    }

    void render(Graphics graphics) {
        // accent/bottom border
        if (isEnabled) {
            graphics.setColor(enabledAccentColor);
        } else {
            graphics.setColor(disabledAccentColor);
        }
        graphics.fillRect(x, y + height, width, 3);
        // background
        if (isEnabled) {
            graphics.setColor(currentColor);
        } else {
            graphics.setColor(disabledColor);
        }
        graphics.fillRect(x, y, width, height);
        // text
        graphics.setFont(font);
        graphics.setColor(textColor);
        graphics.drawString(text, stringX, stringY);
    }

    void tick() {
        // update position (for if/when window is re-sized)
        x = Main.WINDOW_WIDTH - baseX;
        y = Main.WINDOW_HEIGHT - baseYMultiplier - baseY;
        stringX = x + baseStringX;
        stringY = y + baseStringY;
        // emulate hover effect
        if (isClickInBounds(Mouse.x, Mouse.y)) {
            currentColor = hoverColor;
        } else {
            currentColor = enabledColor;
        }
    }

    private boolean isClickInBounds(int cx, int cy) {
        return Util.isClickInBounds(cx, cy, x, y, width, height);
    }
}
