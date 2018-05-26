import java.awt.Color;
import java.awt.Graphics;

class Segment extends Entity {

    Point point1;
    Point point2;

    Segment(Point first, Point second) {
        point1 = first;
        point2 = second;
    }

    static double distance(Point first, Point second) {
        return Math.hypot(first.x - second.x, first.y - second.y);
    }

    static Point getOtherPoint(Point p) {
        if (p.parentSegment.point1.equals(p))
            return p.parentSegment.point2;
        return p.parentSegment.point1;
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.drawLine(point1.x + point1.width / 2, point1.y + point1.height
                / 2, point2.x + point2.width / 2, point2.y + point2.height / 2);
    }

    @Override
    public void tick() {

    }

}