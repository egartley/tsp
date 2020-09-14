import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class UI {

    static final byte CALCULATE_BUTTON = 4;
    static final byte RESET_BUTTON = 3;
    static final byte RANDOMIZE_BUTTON = 2;
    static final byte HELP_BUTTON = 1;
    static final byte OPTIONS_BUTTON = 0;

    private static FontMetrics fm;
    private static BufferedImage fieldTextImage;
    private static short statusLineIndex = 0;
    private static boolean setFontMetrics = false;
    private static final Font statusFont = new Font("Arial", Font.BOLD, 15);
    private static final Color positiveColor = new Color(0, 208, 21);
    private static final Color negativeColor = Color.RED;

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
                Field.calculate();
            }
        });
        buttons.add(new ActionButton("Hide/Show Points", true, fm) {
            @Override
            void onClick() {
                Field.togglePointVisibility();
            }
        });
        try {
            fieldTextImage = ImageIO.read(new File("src//field-text.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void setButtonIsEnabled(byte buttonID, boolean enabled) {
        if (buttonID != -1 && buttonID < buttons.size()) {
            buttons.get(buttonID).isEnabled = enabled;
        }
    }

    static boolean getButtonIsEnabled(byte buttonID) {
        return buttonID != -1 && buttonID < buttons.size() && buttons.get(buttonID).isEnabled;
    }

    private static void drawStatusLine(Graphics graphics, String text) {
        drawStatusLine(graphics, new String[]{text}, new Color[]{Color.BLACK});
    }

    private static void drawStatusLine(Graphics graphics, String[] parts, Color[] colors) {
        // make sure params are valid
        if (parts.length != colors.length || parts.length == 0) {
            return;
        }
        short i = 0;
        int x = Main.WINDOW_WIDTH - 218;
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
        // whether or not points are showing
        drawStatusLine(graphics, new String[]{"Visibility: ", (Field.showPoints) ? "Shown" : "Hidden"}, new Color[]{Color.BLACK, (Field.showPoints) ? positiveColor : negativeColor});
        // number of points
        drawStatusLine(graphics, "Number of points: " + Field.points.size());
        // calculation status
        statusLineIndex++;
        if (Field.isCalculating) {
            // actual text that describes the current calculation status
            drawStatusLine(graphics, Field.segments.size() + " of " + Field.points.size());
            drawStatusLine(graphics, "Calculating...");
            // progress bar
            graphics.setColor(Color.BLACK);
            graphics.drawRect(Main.WINDOW_WIDTH - 218, 169, 181, 13);
            graphics.setColor(ActionButton.enabledColor);
            graphics.fillRect(Main.WINDOW_WIDTH - 217, 170, (int) (180 * ((1.0 * Field.segments.size()) / (1.0 * Field.points.size()))), 12);
        } else {
            if (Field.points.size() <= Field.maximumPoints && Field.points.size() != 0) {
                // let user know they can calculate, assuming calculate is enabled
                drawStatusLine(graphics, "Ready to calculate");
            } else if (Field.points.size() == 0) {
                // need to add points
                drawStatusLine(graphics, "Start by adding points");
            }
            // last path distance
            statusLineIndex++;
            drawStatusLine(graphics, "Distance: " + ((Field.lastPathDistance == 0.0) ? "None" : Field.lastPathDistance));
        }
        // reset status line index so that they will stay be the same
        // next call to render
        statusLineIndex = 0;
    }

    static void render(Graphics graphics) {
        // if no points, be lazy and show an image of instructional text
        if (Field.points.size() == 0) {
            graphics.drawImage(fieldTextImage,
                    (Field.fieldWidth / 2 - fieldTextImage.getWidth() / 2) + 7,
                    Main.WINDOW_HEIGHT / 2 - fieldTextImage.getHeight() / 2,
                    fieldTextImage.getWidth(), fieldTextImage.getHeight(), null);
        }
        // render "action buttons"
        for (ActionButton ab : buttons) {
            ab.render(graphics);
        }
        // draw status text (calc status, etc.)
        drawStatusText(graphics);
    }

    static void tick() {
        try {
            for (ActionButton ab : buttons) {
                ab.tick();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
