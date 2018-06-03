import java.awt.Color;
import java.awt.Graphics;

/**
 * Cartesian coordinated point that can be visually represented
 */
class Point extends Entity {

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

    /**
     * The {@link Segment} that this point is attached to
     */
    Segment parentSegment;

    /**
     * Set randomly in constructor
     */
    private Color color;

    /**
     * Creates a new point with a random color at the specified coordinates
     *
     * @param x The x coordinate
     * @param y The y coordinate
     */
    Point(int x, int y) {
        super(10, 10);
        this.x = x;
        this.y = y;
        color = new Color(Util.randomInt(255, 0), Util.randomInt(255, 0),
                Util.randomInt(255, 0));
    }

    @Override
    public void render(Graphics graphics) {
        // actually draw the point
        graphics.setColor(color);
        graphics.fillRect(x, y, width, height);
        // surround with black border to make it easier to see, especially if the random color is lighter
        graphics.setColor(Color.BLACK);
        graphics.drawRect(x - 1, y - 1, width + 1, height + 1);
        // visual indication that this is either the base or end point
        if (isBasePoint || isEndPoint)
            graphics.drawRect(x - 3, y - 3, width + 5, height + 5);
    }

    @Override
    public void tick() {

    }

    /**
     * @param other The other point to compare this point to
     * @return Whether or not this point has the same x and y coordinates as the given point
     */
    boolean equals(Point other) {
        return other != null && x == other.x && y == other.y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}