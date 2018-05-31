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
    private static short lineIndex = 0;
    private static boolean setFontMetrics = false;

    private static BufferedImage fieldTextImage;
    private static Font statusFont = new Font("Consolas", Font.BOLD, 14);
    private static Color positiveColor = new Color(0, 208, 21);
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
        if (parts.length != colors.length || parts.length == 0)
            return;

        short i = 0;
        int x = 896;
        int y = 80 + (int) (statusFont.getSize() * 1.375 * lineIndex);
        for (String text : parts) {
            graphics.setColor(colors[i]);
            graphics.drawString(text, x, y);
            x += fm.stringWidth(text);
            i++;
        }

        lineIndex++;
    }

    private static void drawStatusText(Graphics graphics) {
        graphics.setFont(statusFont);
        if (!setFontMetrics) {
            fm = graphics.getFontMetrics(statusFont);
            setFontMetrics = true;
        }

        // Field.showPoints
        if (Field.showPoints)
            drawStatusLine(graphics, new String[]{"Showing Points: ", "TRUE"}, new Color[]{Color.BLACK, positiveColor});
        else
            drawStatusLine(graphics, new String[]{"Showing Points: ", "FALSE"}, new Color[]{Color.BLACK, Color.RED});
        // number of points
        drawStatusLine(graphics, new String[]{"Number of Points: ", String.valueOf(Field.points.size())}, new Color[]{Color.BLACK, Color.BLACK});

        // calculation status
        lineIndex++;
        if (Field.isCalculating) {
            double percent = ((1.0 * Field.segments.size()) / (1.0 * Field.points.size()));
            drawStatusLine(graphics, new String[]{Field.segments.size() + " of " + Field.points.size()}, new Color[]{Color.BLACK});
            drawStatusLine(graphics, new String[]{String.format("%.2f", percent * 100.0) + "% complete"}, new Color[]{Color.BLACK});

            // progress bar
            graphics.setColor(Color.BLACK);
            graphics.drawRect(895, 169, 141, 13);
            graphics.setColor(ActionButton.enabledColor);
            graphics.fillRect(896, 170, (int) (140 * percent), 12);
        } else {
            drawStatusLine(graphics, new String[]{"Ready to calculate"}, new Color[]{Color.BLACK});
        }

        lineIndex = 0;
    }

    static void render(Graphics graphics) {
        if (Field.points.size() == 0)
            graphics.drawImage(fieldTextImage, fieldTextImageX, fieldTextImageY, fieldTextImage.getWidth(), fieldTextImage.getHeight(), null);

        for (ActionButton ab : buttons)
            ab.render(graphics);

        drawStatusText(graphics);
    }

    static void tick() {
        try {
            for (ActionButton ab : buttons)
                ab.tick();
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
    }

}