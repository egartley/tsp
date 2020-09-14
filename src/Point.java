import java.awt.*;

/**
 * Cartesian coordinated point that can be visually represented
 */
class Point extends Entity {

    static final int SIZE = 10;

    /**
     * Whether or not this point is the first point. In the context of TSP, it is the "origin city"
     */
    boolean isBasePoint = false;
    /**
     * Whether or not this point is the last point. In the context of TSP, it is the "destination city"
     */
    boolean isEndPoint = false;
    /**
     * Whether or not this point has already been "travelled" to
     */
    boolean isTravelled = false;
    boolean showCoordinates = false;

    private boolean setCoordinateFontMetrics = false;

    /**
     * The {@link Segment} that this point is attached to
     */
    Segment parentSegment;

    private Color color;
    private String coordinateString;
    private FontMetrics coordinateFontMetrics;
    private final Color borderColor = Color.BLACK;
    private final Color coordinateColor = Color.BLACK;
    private final Font coordinateFont = new Font("Consolas", Font.PLAIN, 12);

    /**
     * Creates a new point with a random color at the specified coordinates
     *
     * @param x The x coordinate
     * @param y The y coordinate
     */
    Point(int x, int y) {
        super(SIZE, SIZE);
        this.x = x;
        this.y = y;
        color = new Color(Util.randomInt(255, 0), Util.randomInt(255, 0), Util.randomInt(255, 0));
        coordinateString = x + ", " + y;
    }

    @Override
    public void render(Graphics graphics) {
        // actually draw the point
        graphics.setColor(color);
        graphics.fillRect(x, y, width, height);
        // surround with 1px black border to make it easier to see, especially if the random color is lighter
        graphics.setColor(borderColor);
        graphics.drawRect(x - 1, y - 1, width + 1, height + 1);
        // visual indication if this is either the base or end point
        if (isBasePoint || isEndPoint) {
            graphics.drawRect(x - 3, y - 3, width + 5, height + 5);
        }
        // display coordinates on hover
        if (showCoordinates) {
            graphics.setColor(coordinateColor);
            if (!setCoordinateFontMetrics) {
                coordinateFontMetrics = graphics.getFontMetrics(coordinateFont);
                setCoordinateFontMetrics = true;
            }
            coordinateString = this.toString();
            graphics.fillRect(Mouse.x + 4, Mouse.y - 18, coordinateFontMetrics.stringWidth(coordinateString) + 8, 19);
            graphics.setColor(Color.WHITE);
            graphics.setFont(coordinateFont);
            graphics.drawString(coordinateString, Mouse.x + 8, Mouse.y - 4);
        }
    }

    @Override
    public void tick() {
        showCoordinates = Util.isClickInBounds(Mouse.x, Mouse.y, x - 5, y - 5, width + 5, height + 5);
    }

    boolean equalCoordinates(Point other) {
        return other != null && x == other.x && y == other.y;
    }

    @Override
    public String toString() {
        return "(x: " + x + ", y: " + y + ")";
    }

}
