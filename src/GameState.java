import java.awt.*;

public abstract class GameState {

    public static final int IN_GAME = 0;
    protected int identificationNumber;

    public abstract void render(Graphics graphics);

    public abstract void tick();

}
