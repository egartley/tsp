import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class Options {

    private static boolean isAlreadyShown = false;

    static void show() {
        if (isAlreadyShown) {
            return;
        }
        JFrame frame = new JFrame("Options");
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLayout(null);
        frame.setIconImage(new ImageIcon("src//gear.png").getImage());
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
            public void windowClosed(WindowEvent e) { }
            @Override
            public void windowIconified(WindowEvent e) { }
            @Override
            public void windowDeiconified(WindowEvent e) { }
            @Override
            public void windowActivated(WindowEvent e) { }
            @Override
            public void windowDeactivated(WindowEvent e) { }
        });
        frame.setAlwaysOnTop(true);
        // build controllers
        String[] choices = new String[]{"Maximum", "3", "12", "18", "24", "32", "48", "52", "64", "72", "128", "200", "300", "500"};
        OptionController randomizeAmountController = new OptionController("Points while randomizing", choices, 0, 0) {
            @Override
            void onComboBoxUpdate(String selected) {
                if (selected.equals("Maximum")) {
                    Field.randomizeAmount = Field.maximumPoints;
                } else {
                    Field.randomizeAmount = Integer.parseInt(selected);
                }
            }
        };
        randomizeAmountController.addComponentsToFrame(frame);
        randomizeAmountController.synchronizeComboBoxSelection((Field.randomizeAmount == Field.maximumPoints) ? "Maximum" : String.valueOf(Field.randomizeAmount));

        OptionController maximumPointsController = new OptionController("Maximum number of points (" + Field.maximumPoints + ")", Field.MIN_POINTS, Field.MAX_POINTS, Field.maximumPoints, 1, 0, 250) {
            @Override
            void onSliderUpdate(int value) {
                Field.maximumPoints = value;
                Field.randomizeAmount = value;
                descriptionLabel.setText("Maximum number of points (" + Field.maximumPoints + ")");
            }
        };
        maximumPointsController.addComponentsToFrame(frame);

        OptionController calculateAfterRandomizeController = new OptionController("Calculate after randomizing", "No", OptionController.CHECKBOX, 0, 1) {
            @Override
            void onUpdate() {
                if (checkBox.getText().equals("No"))
                    checkBox.setText("Yes");
                else
                    checkBox.setText("No");
                Field.calculateAfterRandomize = checkBox.isSelected();
            }
        };
        calculateAfterRandomizeController.addComponentsToFrame(frame);
        if (Field.calculateAfterRandomize) {
            // false by default, but if previously set to true
            calculateAfterRandomizeController.checkBox.setText("Yes");
            calculateAfterRandomizeController.checkBox.setSelected(true);
        }
        // additional components
        JButton okayButton = new JButton("OK");
        okayButton.setSize(96, 29);
        okayButton.setLocation(frame.getWidth() - 120, frame.getHeight() - 72);
        okayButton.setFocusable(false);
        okayButton.addActionListener(e -> {
            frame.dispose();
            isAlreadyShown = false;
        });
        frame.add(okayButton);
        frame.setVisible(true);
    }

}
