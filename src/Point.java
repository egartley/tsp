import java.awt.Color;
import java.awt.Graphics;

class Point extends Entity {

    /**
     * Whether or not this point is the "base", or starting point
     */
    boolean isBasePoint = false;
    /**
     * Whether or not this point is the "end", or last point
     */
    boolean isEndPoint = false;
    /**
     * Whether or not this point has already been "travelled" to
     */
    boolean isTravelled = false;

    Segment parentSegment;

    private Color color;

    Point(int x, int y) {
        super(10, 10);
        this.x = x;
        this.y = y;
        color = new Color(Util.randomInt(255, 0), Util.randomInt(255, 0),
                Util.randomInt(255, 0));
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(color);
        graphics.fillRect(x, y, width, height);
        graphics.setColor(Color.BLACK);
        graphics.drawRect(x - 1, y - 1, width + 1, height + 1);
        if (isBasePoint || isEndPoint) {
            graphics.drawRect(x - 3, y - 3, width + 5, height + 5);
        }
    }

    @Override
    public void tick() {

    }

    boolean equals(Point other) {
        if (other == null)
            return false;
        return x == other.x && y == other.y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}