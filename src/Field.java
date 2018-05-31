import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

class Field {

    final static int MAX_POINTS = 2000;

    static int maximumPoints = MAX_POINTS;
    static int randomizeAmount = maximumPoints;
    static short fieldWidth = 850;
    static boolean showPoints = true;
    static boolean isCalculated;
    static boolean isCalculating;

    static ArrayList<Point> points = new ArrayList<>();
    static ArrayList<Segment> segments = new ArrayList<>();

    static void onClick(int x, int y) {
        // correct for field margin/padding
        x -= 5;
        y -= 5;
        if (points.size() < maximumPoints && !isCalculated) {
            if (x > 7 && x < fieldWidth - 2 && y > 8 && y < Main.WINDOW_HEIGHT - 20) {
                // within field boundaries
                boolean exists = false;
                for (Point p : points)
                    exists = p.x == x && p.y == y;
                // if there isn't already a point at the clicked x/y, then add it
                if (!exists)
                    points.add(new Point(x, y));

                // update base/end booleans
                if (points.size() == 1) {
                    points.get(0).isBasePoint = true;
                    points.get(0).isEndPoint = true;
                } else {
                    points.get(points.size() - 1).isEndPoint = true;
                    points.get(points.size() - 2).isEndPoint = false;
                }
            }
        }

        // check button enabled/disabled
        if (points.size() > 0) {
            if (!UI.getButtonIsEnabled(UI.RESET_BUTTON))
                UI.setButtonIsEnabled(UI.RESET_BUTTON, true);
            if (!UI.getButtonIsEnabled(UI.CALCULATE_BUTTON) && !isCalculated)
                UI.setButtonIsEnabled(UI.CALCULATE_BUTTON, true);
        }
    }

    static synchronized void calculate() {
        Point current = null;
        isCalculating = true;

        if (segments.size() > 0)
            segments.clear();

        // get base point
        for (Point p : points) {
            if (p.isBasePoint) {
                current = p;
                // break since only one point (should) be the "base"
                break;
            }
        }

        // iterate through all the other points, finding the shortest distance
        if (current != null) {
            // there is a base point, so continue
            short index = 0;
            double shortestDistance;

            while (index < points.size()) {
                Point closest = null;
                shortestDistance = Double.MAX_VALUE;

                // find the closest point to current
                for (Point p : points) {
                    if (p.equals(current) || p.isTravelled || p.isBasePoint || p.isEndPoint)
                        continue;

                    double prev = shortestDistance;
                    shortestDistance = Math.min(shortestDistance, Segment.distance(p, current));
                    if (prev != shortestDistance)
                        closest = p;
                }
                if (closest != null) {
                    segments.add(new Segment(current, closest));
                    closest.isTravelled = true;
                    // "travel"
                    current = closest;
                } else {
                    // could not find a closest point, assume we're done
                    Point end = null;
                    for (Point p : points)
                        if (p.isEndPoint)
                            end = p;
                    if (end != null)
                        segments.add(new Segment(current, end));
                    break;
                }
                index++;
            }

            isCalculated = true;
            isCalculating = false;

            UI.setButtonIsEnabled(UI.CALCULATE_BUTTON, false);
        }
    }

    static void reset() {
        points.clear();
        segments.clear();

        isCalculated = false;
        UI.setButtonIsEnabled(UI.CALCULATE_BUTTON, false);
        UI.setButtonIsEnabled(UI.RESET_BUTTON, false);
    }

    static void randomize() {
        points.clear();
        segments.clear();

        for (int i = 0; i < randomizeAmount; i++) {
            points.add(new Point(Util.randomInt(fieldWidth - 8, 16), Util.randomInt(Main.WINDOW_HEIGHT - 28, 16)));

            if (points.size() == 1) {
                points.get(0).isBasePoint = true;
                points.get(0).isEndPoint = true;
            } else {
                points.get(points.size() - 1).isEndPoint = true;
                points.get(points.size() - 2).isEndPoint = false;
            }
        }

        isCalculated = false;
        UI.setButtonIsEnabled(UI.RESET_BUTTON, true);
        UI.setButtonIsEnabled(UI.CALCULATE_BUTTON, true);
    }

    static void hideShowPoints() {
        showPoints = !showPoints;
        if (isCalculated)
            UI.setButtonIsEnabled(UI.CALCULATE_BUTTON, false);
    }

    static void render(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.drawRect(8, 8, fieldWidth, Main.WINDOW_HEIGHT - 17);

        try {
            if (isCalculated)
                for (Segment segment : segments)
                    segment.render(graphics);

            if (showPoints)
                for (Point point : points)
                    point.render(graphics);
        } catch (ConcurrentModificationException e) {
            // non-fatal exception, ignore
        }
    }

}