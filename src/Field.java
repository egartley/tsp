import java.awt.*;
import java.util.ArrayList;

class Field {

    final static int MAX_POINTS = 2000;
    final static int MIN_POINTS = 500;

    static int maximumPoints = MAX_POINTS / 2;
    static int randomizeAmount = maximumPoints;
    static short fieldWidth = 850;
    static double lastPathDistance = Double.NaN;
    static boolean showPoints = true;
    static boolean calculateAfterRandomize;
    static boolean isCalculated;
    static boolean isCalculating;

    static Point draggedPoint = null;
    static ArrayList<Point> points = new ArrayList<>();
    static ArrayList<Segment> segments = new ArrayList<>();

    /**
     * Ensures that the base and end points are set correctly
     */
    private static void updatePointBooleans() {
        if (points.size() == 1) {
            // only one point, so it is therefore both the base and end point
            points.get(0).isBasePoint = true;
            points.get(0).isEndPoint = true;
        } else {
            // set the previous point to the end point
            points.get(points.size() - 1).isEndPoint = true;
            // the point before that is no longer the end
            points.get(points.size() - 2).isEndPoint = false;
        }
    }

    static void onDragEnd() {
        draggedPoint.showCoordinates = false;
        draggedPoint = null;
    }

    static void onDrag(int x, int y) {
        // get point
        if (draggedPoint == null) {
            for (Point p : points)
                if (Util.isClickInBounds(x, y, p.x, p.y, p.width, p.height)) {
                    draggedPoint = p;
                    draggedPoint.showCoordinates = true;
                    break;
                }
        }
        // else, already have a dragged point

        // move point to new mouse x and y
        if (draggedPoint != null) {
            // keep point within field boundaries
            if (Util.isClickInBounds(x - Point.SIZE / 2 + 10, y - Point.SIZE / 2, 8, 8,
                    fieldWidth, Main.WINDOW_HEIGHT - 25)) {
                // still needs work
                draggedPoint.x = x - Point.SIZE / 2;
                draggedPoint.y = y - Point.SIZE / 2;

                // update segment
                if (segments.size() > 0)
                    if (draggedPoint.parentSegment != null)
                        draggedPoint.parentSegment = new Segment(draggedPoint, Segment.getOtherPoint(draggedPoint));
            }
        }
        // else, something is wrong
    }

    static void onClick(int x, int y) {
        // correct for field margin/padding
        x -= 5;
        y -= 5;
        if (points.size() < maximumPoints && !isCalculating) {
            if (x > 7 && x < fieldWidth - 2 && y > 8 && y < Main.WINDOW_HEIGHT - 20) {
                // within field boundaries
                boolean exists = false;
                for (Point p : points)
                    exists = p.x == x && p.y == y;
                // if there isn't already a point at the clicked x/y, then add it
                if (!exists)
                    points.add(new Point(x, y));

                updatePointBooleans();
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

    static synchronized void calculateShortestPath() {
        Point current = null;
        isCalculating = true;
        // reset lastPathDistance from any previous calculation
        lastPathDistance = 0;

        // disable calculate button while actually calculating
        UI.setButtonIsEnabled(UI.CALCULATE_BUTTON, false);

        if (segments.size() > 0)
            segments.clear();

        // get base point
        boolean setBase = false;
        for (Point p : points) {
            // set all points to not previously travelled so that they are able to be used in recalculation with additional points
            p.isTravelled = false;
            if (p.isBasePoint && !setBase) {
                current = p;
                // only one point should be the "base"
                setBase = true;
            }
        }

        // iterate through all the other points, finding the shortest distance
        if (current != null) {
            // there is a base point, so continue
            short index = 0;
            double shortestDistance;
            Point base = null;

            while (index < points.size()) {
                double distance = 0;
                Point closest = null;
                shortestDistance = Double.MAX_VALUE;

                // find the closest point to current
                for (Point p : points) {
                    if (p.isBasePoint)
                        base = p;

                    if (p.equals(current) || p.isTravelled || p.isBasePoint || p.isEndPoint)
                        continue;

                    double prev = shortestDistance;
                    // store calculation in local variable so only compute the distance once (for when adding to lastPathDistance)
                    double calc = Segment.distance(p, current);
                    shortestDistance = Math.min(shortestDistance, calc);

                    if (prev != shortestDistance) {
                        closest = p;
                        distance = calc;
                    }
                }
                if (closest != null) {
                    lastPathDistance += distance;
                    Segment segment = new Segment(current, closest);
                    current.parentSegment = segment;
                    closest.parentSegment = segment;
                    segments.add(segment);
                    closest.isTravelled = true;
                    // "travel"
                    current = closest;
                } else {
                    // could not find a closest point, it was null, assume at last point
                    Point end = null;
                    for (Point p : points)
                        if (p.isEndPoint)
                            end = p;
                    // add the last segments
                    if (end != null) {
                        segments.add(new Segment(current, end));
                        segments.add(new Segment(end, base));
                    }
                    // done!
                    break;
                }
                index++;
            }

            isCalculated = true;
            isCalculating = false;

            // hundredth place
            lastPathDistance = Double.valueOf(String.format("%.2f", lastPathDistance));

            // re-enable calculate button
            UI.setButtonIsEnabled(UI.CALCULATE_BUTTON, true);
        }
    }

    static void reset() {
        // clear points and segments
        points.clear();
        segments.clear();

        // no longer calculated, ready to be calculated again
        isCalculated = false;
        // disable buttons that would (maybe) break the application if clicked
        UI.setButtonIsEnabled(UI.CALCULATE_BUTTON, false);
        UI.setButtonIsEnabled(UI.RESET_BUTTON, false);
    }

    static void randomize() {
        // prevent concurrent modification, or at least minimize it
        if (isCalculating)
            return;

        // get rid of anything already in the field
        points.clear();
        segments.clear();

        for (int i = 0; i < randomizeAmount; i++) {
            // create and then add random point
            points.add(new Point(Util.randomInt(fieldWidth - 8, 16), Util.randomInt(Main.WINDOW_HEIGHT - 28, 16)));

            updatePointBooleans();
        }

        // points were just created, no yet calculated
        isCalculated = false;
        if (calculateAfterRandomize) {
            // however if option is set to calculate after randomizing, then do it
            new Thread(new FieldWorker()).start();
        }
        // enable the calculate and reset buttons
        UI.setButtonIsEnabled(UI.RESET_BUTTON, true);
        UI.setButtonIsEnabled(UI.CALCULATE_BUTTON, true);
    }

    static void hideShowPoints() {
        showPoints = !showPoints;
    }

    static void render(Graphics graphics) {
        // field border (#BuildTheWall)
        graphics.setColor(Color.BLACK);
        graphics.drawRect(8, 8, fieldWidth, Main.WINDOW_HEIGHT - 17);

        try {
            if (isCalculated && !isCalculating)
                for (Segment segment : segments)
                    segment.render(graphics);

            if (showPoints)
                for (Point point : points)
                    point.render(graphics);
        } catch (Exception e) {
            // ignore, probably just concurrent modification because of the overlapping of render and tick threads
        }
    }

    static void tick() {
        try {
            for (Point p : points)
                p.tick();
        } catch (Exception e) {
            // ignore, probably just concurrent modification because of the overlapping of render and tick threads
        }
    }

}