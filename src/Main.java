import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;

import javax.swing.*;

/**
 * Modified version of https://github.com/egartley/beyond-origins/blob/indev/src/net/egartley/beyondorigins/Game.java
 */
class Main extends Canvas implements Runnable {

    // SELF
    private static final long serialVersionUID = 7742965499744983797L;
    private static JFrame frame;
    private static Dimension windowDimension = new Dimension(1120, 673);
    private Graphics graphics;
    private BufferStrategy bufferStrategy;

    // CONSTANTS
    static final int WINDOW_WIDTH = windowDimension.width - 6;
    static final int WINDOW_HEIGHT = windowDimension.height - 29;

    // THREADS
    private static Thread masterRenderThread;
    private static Thread masterTickThread;

    // THREAD OBJECTS
    private static MasterTick tick = new MasterTick();

    // FLAGS
    static boolean running = false;
    static boolean runTickThread = true;
    static boolean initializedFontMetrics = false;

    private static Main self;

    // GAMESTATES
    static GameState currentGameState;

    private void init() {
        currentGameState = new InGameState();
        frame = new JFrame("Travelling Salesman");
        frame.setSize(windowDimension.width, windowDimension.height);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        this.addMouseListener(new Mouse());
        this.addMouseMotionListener(new Mouse());
        frame.add(self);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon("src//icon.png").getImage());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Could not set the look and feel!");
        }
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        self = new Main();
        self.setPreferredSize(windowDimension);
        self.setMaximumSize(windowDimension);
        self.setMinimumSize(windowDimension);
        self.start();
    }

    private synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        masterRenderThread = new Thread(this);
        masterRenderThread.setPriority(1);
        masterRenderThread.setName("Main-Render");

        restartMainTickThread();
        masterRenderThread.start();
    }

    private synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            masterRenderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        init();
        render();
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        double ns = 16666666.666666666;
        double delta = 0.0D;
        requestFocus();
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1.0D) {
                render();
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
        stop();
    }

    private synchronized void render() {
        createBufferStrategy(2);
        bufferStrategy = getBufferStrategy();
        graphics = bufferStrategy.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (!initializedFontMetrics) {
            UI.init(graphics.getFontMetrics(ActionButton.font));
            initializedFontMetrics = true;
        }
        currentGameState.render(graphics);
        graphics.dispose();
        bufferStrategy.show();
        bufferStrategy.dispose();
    }

    private static void restartMainTickThread() {
        runTickThread = true;
        masterTickThread = new Thread(tick);
        masterTickThread.setPriority(2);
        masterTickThread.setName("Main-Tick");
        masterTickThread.start();
    }

}