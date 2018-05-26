import java.awt.*;

/**
 * Modified version of https://github.com/egartley/beyond-origins/blob/indev/src/net/egartley/beyondorigins/objects/GameState.java
 */
public abstract class GameState {

    /**
     * The ID number used while actually playing the game
     *
     * @see GameState
     */
    public static final int IN_GAME = 0;
    /**
     * Unique integer used to identify different game states
     */
    protected int identificationNumber;

    public abstract void render(Graphics graphics);

    public abstract void tick();

}