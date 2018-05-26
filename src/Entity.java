import java.awt.*;

/**
 * Slimmed-down version of https://github.com/egartley/beyond-origins/blob/indev/src/net/egartley/beyondorigins/objects/Entity.java
 */
abstract class Entity {

    public int x;
    public int y;
    public int width;
    public int height;

    public Entity() {

    }

    public Entity(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public abstract void render(Graphics graphics);

    public abstract void tick();

}