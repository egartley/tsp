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

    private static int fieldTextImageX;
    private static int fieldTextImageY;

    private static BufferedImage fieldTextImage;

    static ArrayList<ActionButton> buttons = new ArrayList<>();

    static void init(FontMetrics fm) {
        buttons.add(new ActionButton("Options", true, fm) {
            @Override
            void onClick() {

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

    static void setButtonIsEnabled(byte button, boolean enabled) {
        if (button != -1 && button < buttons.size())
            buttons.get(button).isEnabled = enabled;
    }

    static boolean getButtonIsEnabled(byte button) {
        if (button != -1 && button < buttons.size())
            return buttons.get(button).isEnabled;
        return false;
    }

    static void render(Graphics graphics) {
        if (Field.points.size() == 0)
            graphics.drawImage(fieldTextImage, fieldTextImageX, fieldTextImageY, fieldTextImage.getWidth(), fieldTextImage.getHeight(), null);

        for (ActionButton ab : buttons)
            ab.render(graphics);
    }

}