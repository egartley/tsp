import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class Options {

    private static boolean isAlreadyShown = false;

    static void show() {
        if (isAlreadyShown)
            return;

        // build frame
        JFrame frame = new JFrame("Options");
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLayout(null);
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                isAlreadyShown = true;
            }

            @Override
            public void windowClosing(WindowEvent e) {
                isAlreadyShown = false;
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        frame.setAlwaysOnTop(true);

        // build controllers
        OptionController randomizeAmountController = new OptionController("Points while randomizing",
                new String[]{"Maximum", "3", "12", "18", "24", "32", "48", "52", "64", "72", "128", "200", "300", "500"}, 0, 0) {
            @Override
            void onComboBoxUpdate(String selected) {
                if (selected.equals("Maximum")) {
                    Field.randomizeAmount = Field.maximumPoints;
                } else {
                    Field.randomizeAmount = Integer.valueOf(selected);
                }
            }
        };
        randomizeAmountController.addComponentsToFrame(frame);
        if (Field.randomizeAmount == Field.maximumPoints)
            randomizeAmountController.synchronizeComboBoxSelection("Maximum");
        else
            randomizeAmountController.synchronizeComboBoxSelection(String.valueOf(Field.randomizeAmount));

        OptionController testController2 = new OptionController("Maximum number of points (" + Field.maximumPoints + ")",
                0, Field.MAX_POINTS, Field.maximumPoints, 1, 0, 250) {
            @Override
            void onSliderUpdate(int value) {
                Field.maximumPoints = value;
                Field.randomizeAmount = value;
                descriptionLabel.setText("Maximum number of points (" + Field.maximumPoints + ")");
            }
        };
        testController2.addComponentsToFrame(frame);

        frame.setVisible(true);
    }

}
