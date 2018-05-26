import java.awt.*;
import java.awt.event.MouseEvent;

class ActionButton {

    boolean isEnabled;

    private static boolean setFont = false;

    private static byte declarationIndex = 0;

    private short width = 180;
    private short height = 32;
    private int x;
    private int y;
    private int sx;
    private int sy;

    private String text;
    private Color enabledColor = new Color(0, 145, 17);
    private Color disabledColor = Color.DARK_GRAY;
    private Color enabledAccentColor = enabledColor.darker();
    private Color disabledAccentColor = disabledColor.darker();
    private Color textColor = Color.WHITE;

    static Font font = new Font("Arial", Font.PLAIN, 18);

    ActionButton(String text, boolean defaultEnabled, FontMetrics fm) {
        this(text, defaultEnabled, fm, 0);
    }

    ActionButton(String text, boolean defaultEnabled, FontMetrics fm, int offset) {
        this.text = text;
        isEnabled = defaultEnabled;

        // auto-calc coordinates based on order of declaration
        x = 896;
        sx = x + ((width / 2) - (fm.stringWidth(text) / 2));
        y = 600 - ((declarationIndex += (offset + 1)) * 44);
        sy = y + 23;
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
        // meant to be overridden in each declaration
    }

    void render(Graphics graphics) {
        // "drop shadow"/accent/bottom border
        if (isEnabled) {
            graphics.setColor(enabledAccentColor);
        } else {
            graphics.setColor(disabledAccentColor);
        }
        graphics.fillRect(x, y + height, width, 3);
        // background
        if (isEnabled) {
            graphics.setColor(enabledColor);
        } else {
            graphics.setColor(disabledColor);
        }
        graphics.fillRect(x, y, width, height);


        // text
        graphics.setFont(font);
        graphics.setColor(textColor);
        graphics.drawString(text, sx, sy);
    }

    private boolean isClickInBounds(int cx, int cy) {
        return cx >= x && cx <= x + width && cy >= y && cy <= y + height;
    }
}