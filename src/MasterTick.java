/**
 * Modified version of https://github.com/egartley/beyond-origins/blob/indev/src/net/egartley/beyondorigins/threads/MasterTick.java
 */
public class MasterTick implements Runnable {

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        double ns = 16666666.666666666;
        double delta = 0.0D;
        while (Main.running && Main.runTickThread) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1.0D) {
                tick();
                delta -= 1.0D;
                if (System.currentTimeMillis() - timer > 1000L) {
                    timer += 1000L;
                }
            }
            try {
                Thread.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void tick() {
        if (Main.currentGameState != null) {
            Main.currentGameState.tick();
        }
    }

}