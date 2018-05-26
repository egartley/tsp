import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class Mouse implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

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