import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Modified version of https://github.com/egartley/beyond-origins/blob/indev/src/net/egartley/beyondorigins/Util.java
 */
class Util {

    // https://stackoverflow.com/a/13605411
    private static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }

    /**
     * Returns a resized image of the original with the supplied width and height
     *
     * @param image The original image to resize (won't be changed)
     * @param w     New width to resize to
     * @param h     New height to resize to
     * @return A resized version of the given buffered image
     */
    static BufferedImage resized(BufferedImage image, int w, int h) {
        return toBufferedImage(image.getScaledInstance(w, h, Image.SCALE_DEFAULT));
    }

    /**
     * Returns a random integer, using {@link java.util.concurrent.ThreadLocalRandom
     * ThreadLocalRandom}, between the supplied maximum and minimum values
     *
     * @param maximum The maximum value the random integer could be
     * @param minimum The minimum value the random integer could be
     * @return A random integer between the given maximum and minimum
     */
    static int randomInt(int maximum, int minimum) {
        // this is using ThreadLocalRandom because that is apparently more efficient
        return ThreadLocalRandom.current().nextInt(minimum, maximum);
    }

    /**
     * <p>
     * Returns a random integer between the supplied maximum and minimum values
     * (uses {@link #randomInt(int, int) randomInt(minimum, maximum)})
     * </p>
     *
     * <p>
     * If inclusive, then {@link #randomInt(int, int) randomInt(minimum, maximum +
     * 1)} will be used
     * </p>
     *
     * @param maximum   The maximum value the random integer could be
     * @param minimum   The minimum value the random integer could be
     * @param inclusive Whether or not the include the maximum as a possible value
     * @return A random integer between the given maximum and minimum
     */
    static int randomInt(int maximum, int minimum, boolean inclusive) {
        return inclusive ? randomInt(maximum + 1, minimum) : randomInt(maximum, minimum);
    }

    static boolean isClickInBounds(int cx, int cy, int x, int y, int width, int height) {
        return cx >= x && cx <= x + width && cy >= y && cy <= y + height;
    }

}