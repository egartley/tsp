import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

class Mouse implements MouseListener, MouseMotionListener {

    static int x;
    static int y;

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Field.onClick(e.getX(), e.getY());
        for (ActionButton ab : UI.buttons)
            ab.checkClick(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

}