import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

class UI {

    static final byte CALCULATE_BUTTON = 4;
    static final byte RESET_BUTTON = 3;
    static final byte RANDOMIZE_BUTTON = 2;
    static final byte HELP_BUTTON = 1;
    static final byte OPTIONS_BUTTON = 0;

    private static int fieldTextImageX;
    private static int fieldTextImageY;
    private static short statusLineIndex = 0;
    private static boolean setFontMetrics = false;

    private static BufferedImage fieldTextImage;
    private static Font statusFont = new Font("Consolas", Font.BOLD, 14);
    private static Color positiveColor = new Color(0, 208, 21);
    private static Color[] black = new Color[]{Color.BLACK};
    private static FontMetrics fm;

    static ArrayList<ActionButton> buttons = new ArrayList<>();

    static void init(FontMetrics fm) {
        buttons.add(new ActionButton("Options", true, fm) {
            @Override
            void onClick() {
                Options.show();
            }
        });
        buttons.add(new ActionButton("Help", true, fm) {
            @Override
            void onClick() {

            }
        });
        buttons.add(new ActionButton("Randomize", true, fm, 1) {
            @Override
            void onClick() {
                Field.randomize();
            }
        });
        buttons.add(new ActionButton("Reset", false, fm) {
            @Override
            void onClick() {
                Field.reset();
            }
        });
        buttons.add(new ActionButton("Calculate", false, fm) {
            @Override
            void onClick() {
                if (!Field.isCalculating)
                    new Thread(new FieldWorker()).start();
            }
        });
        buttons.add(new ActionButton("Hide/Show Points", true, fm) {
            @Override
            void onClick() {
                Field.hideShowPoints();
            }
        });

        try {
            fieldTextImage = ImageIO.read(new File("src//field-text.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        fieldTextImageX = (Field.fieldWidth / 2 - fieldTextImage.getWidth() / 2) + 7;
        fieldTextImageY = Main.WINDOW_HEIGHT / 2 - fieldTextImage.getHeight() / 2;
    }

    /**
     * Sets an action button's {@link ActionButton#isEnabled isEnabled} within {@link #buttons} to the value of {@code enabled}. Nothing is performed if there is not a button with the given ID
     *
     * @param buttonID Identification for the button
     * @param enabled  Value for {@link ActionButton#isEnabled isEnabled}
     * @see #OPTIONS_BUTTON
     * @see #HELP_BUTTON
     * @see #RANDOMIZE_BUTTON
     * @see #RESET_BUTTON
     * @see #CALCULATE_BUTTON
     */
    static void setButtonIsEnabled(byte buttonID, boolean enabled) {
        if (buttonID != -1 && buttonID < buttons.size())
            buttons.get(buttonID).isEnabled = enabled;
    }

    static boolean getButtonIsEnabled(byte buttonID) {
        if (buttonID != -1 && buttonID < buttons.size())
            return buttons.get(buttonID).isEnabled;
        return false;
    }

    private static void drawStatusLine(Graphics graphics, String[] parts, Color[] colors) {
        // make sure params are valid
        if (parts.length != colors.length || parts.length == 0)
            return;

        short i = 0;
        // x is always the same (for now)
        int x = 896;
        // y is increased base on how many times this method has been called
        int y = 80 + (int) (statusFont.getSize() * 1.375 * statusLineIndex);
        for (String text : parts) {
            graphics.setColor(colors[i]);
            graphics.drawString(text, x, y);
            x += fm.stringWidth(text);
            i++;
        }

        // increment the index so that the next call will be below this one
        statusLineIndex++;
    }

    private static void drawStatusText(Graphics graphics) {
        graphics.setFont(statusFont);
        if (!setFontMetrics) {
            // only do this once, since it's not needed every call
            fm = graphics.getFontMetrics(statusFont);
            setFontMetrics = true;
        }

        // Field.showPoints
        if (Field.showPoints)
            drawStatusLine(graphics, new String[]{"Points visible: ", "TRUE"}, new Color[]{Color.BLACK, positiveColor});
        else
            drawStatusLine(graphics, new String[]{"Points visible: ", "FALSE"}, new Color[]{Color.BLACK, Color.RED});
        // number of points
        drawStatusLine(graphics, new String[]{"Number of Points: " + String.valueOf(Field.points.size())}, black);

        // calculation status
        statusLineIndex++;
        if (Field.isCalculating) {
            // actual text that describes the current calculation status
            drawStatusLine(graphics, new String[]{Field.segments.size() + " of " + Field.points.size()}, black);
            drawStatusLine(graphics, new String[]{"Calculating..."}, black);

            // progress bar
            graphics.setColor(Color.BLACK);
            graphics.drawRect(895, 169, 181, 13);
            graphics.setColor(ActionButton.enabledColor);
            graphics.fillRect(896, 170, (int) (180 * (1.0 * Field.segments.size() / (1.0 * Field.points.size()))), 12);
        } else {
            if (Field.points.size() <= Field.maximumPoints && Field.points.size() != 0) {
                // let user know they can calculate, assuming calculate is enabled
                drawStatusLine(graphics, new String[]{"Ready to calculate"}, black);
            } else if (Field.points.size() == 0) {
                // need to add points
                drawStatusLine(graphics, new String[]{"Start by adding points"}, black);
            }
            // last path distance
            statusLineIndex++;
            drawStatusLine(graphics, new String[]{"Most recent distance:"}, black);
            drawStatusLine(graphics, new String[]{Field.lastPathDistance + " meters"}, black);
        }

        // reset status line index so that they will stay be the same next call to render
        statusLineIndex = 0;
    }

    static void render(Graphics graphics) {
        // if no points, be lazy and show an image of instructional text
        if (Field.points.size() == 0)
            graphics.drawImage(fieldTextImage, fieldTextImageX, fieldTextImageY, fieldTextImage.getWidth(), fieldTextImage.getHeight(), null);

        // render "action buttons"
        for (ActionButton ab : buttons)
            ab.render(graphics);

        // draw status text (calc status, etc)
        drawStatusText(graphics);
    }

    static void tick() {
        try {
            // mainly checking for mouse hover
            for (ActionButton ab : buttons)
                ab.tick();
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
    }

}