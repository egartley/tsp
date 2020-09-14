import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

class Main extends Canvas implements Runnable {

    // Some code copied from https://github.com/egartley/beyond-origins/

    private static JFrame frame;
    private static Thread masterThread;
    private static Main self;
    private static GameState currentGameState;
    private static final long serialVersionUID = 7742965499744983797L;
    private static final Dimension windowDimension = new Dimension(1120, 673);

    static boolean running = false;
    static boolean initializedFontMetrics = false;
    static int WINDOW_WIDTH = windowDimension.width - 6;
    static int WINDOW_HEIGHT = windowDimension.height - 29;

    private void init() {
        currentGameState = new InGameState();
        frame = new JFrame("Travelling Salesman");
        frame.setSize(windowDimension.width, windowDimension.height);
        frame.setMinimumSize(windowDimension);
        frame.setPreferredSize(windowDimension);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
        masterThread = new Thread(this);
        masterThread.setName("Master");
        masterThread.start();
    }

    private synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            masterThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        init();
        tick();
        render();
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        double ns = 16666666.66666666;
        double delta = 0.0D;
        requestFocus();
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1.0D) {
                tick();
                render();
                delta -= 1.0D;
                if (System.currentTimeMillis() - timer > 1000L) {
                    timer += 1000L;
                }
            }
        }
        stop();
    }

    private synchronized void render() {
        createBufferStrategy(2);
        BufferStrategy bufferStrategy = getBufferStrategy();
        Graphics graphics = bufferStrategy.getDrawGraphics();
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

    private synchronized void tick() {
        WINDOW_WIDTH = frame.getWidth() - 6;
        WINDOW_HEIGHT = frame.getHeight() - 29;
        if (currentGameState != null) {
            currentGameState.tick();
        }
    }

}
