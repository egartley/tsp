import java.awt.*;

/**
 * Modified version of https://github.com/egartley/beyond-origins/blob/master/src/net/egartley/beyondorigins/gamestates/InGameState.java
 */
class InGameState extends GameState {

    private boolean setButtonFont = false;

    InGameState() {
        identificationNumber = GameState.IN_GAME;
    }

    @Override
    public void render(Graphics graphics) {
        if (!setButtonFont) {
            graphics.setFont(ActionButton.font);
            setButtonFont = true;
        }
        Field.render(graphics);
        UI.render(graphics);
    }

    @Override
    public void tick() {
        UI.tick();
        Field.tick();
    }

}