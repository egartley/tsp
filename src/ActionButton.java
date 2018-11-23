import java.awt.*;
import java.awt.event.MouseEvent;

class ActionButton {

    /**
     * Whether or not this button is able to be clicked
     */
    boolean isEnabled;

    private static byte declarationIndex = 0;

    private short width = 180;
    private short height = 32;
    private int x;
    private int y;
    private int baseX = 218;
    private int baseY;
    private int baseYMultiplier = 44;
    private int baseStringX;
    private int baseStringY = 23;
    private int stringX;
    private int stringY;
    private int stringWidth;

    private String text;
    private Color disabledColor = Color.DARK_GRAY;
    private Color enabledAccentColor = enabledColor.darker();
    private Color disabledAccentColor = disabledColor.darker();
    private Color hoverColor = enabledColor.brighter();
    private Color textColor = Color.WHITE;
    private Color currentColor = new Color(0, 170, 17);

    static Color enabledColor = new Color(0, 145, 17);
    static Font font = new Font("Arial", Font.PLAIN, 18);

    ActionButton(String text, boolean isEnabledByDefault, FontMetrics fm) {
        this(text, isEnabledByDefault, fm, 0);
    }

    ActionButton(String text, boolean isEnabledByDefault, FontMetrics fm, int offset) {
        this.text = text;
        isEnabled = isEnabledByDefault;

        // auto-calculate initial coordinates based on order of declaration
        baseY = (declarationIndex += (offset + 1)) * baseYMultiplier;
        stringWidth = fm.stringWidth(text);
        baseStringX = (width / 2) - (stringWidth / 2);
        tick();
    }

    /**
     * Should be called whenever the user clicks/releases the mouse
     */
    void checkClick(MouseEvent e) {
        if (isClickInBounds(e.getX(), e.getY()) && isEnabled)
            onClick();
    }

    /**
     * Called when the button is clicked and enabled
     */
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