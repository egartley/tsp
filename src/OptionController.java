import javax.swing.*;

class OptionController {

    static final byte BUTTON = 1;
    static final byte CHECKBOX = 2;
    static final byte SLIDER = 3;
    static final byte COMBO_BOX = 4;

    private static short width = 180;
    private static short height = 64;

    private byte componentID;

    JLabel descriptionLabel;
    JButton button;
    JCheckBox checkBox;
    JSlider slider;
    JComboBox comboBox;

    OptionController(String label, String componentLabel, byte componentID, int row, int column) {
        int x = column * width + 16 + (column * 24);
        int y = row * height + 16 + (row * 24);

        descriptionLabel = new JLabel(label);
        descriptionLabel.setSize(width, height / 2);
        descriptionLabel.setLocation(x, y);

        this.componentID = componentID;
        switch (componentID) {
            case BUTTON:
                button = new JButton(componentLabel);
                button.setSize(96, 24);
                button.setLocation(x, y + height / 2);
                button.setFocusable(false);
                button.addActionListener(e -> onUpdate());
                break;
            case CHECKBOX:
                checkBox = new JCheckBox(componentLabel);
                checkBox.setSize(width, height / 2);
                checkBox.setLocation(x, y + height / 2);
                checkBox.setFocusable(false);
                checkBox.addActionListener(e -> onUpdate());
                break;
            default:
                break;
        }
    }

    OptionController(String label, String[] listItems, int row, int column) {
        int x = column * width + 16 + (column * 24);
        int y = row * height + 16 + (row * 24);

        descriptionLabel = new JLabel(label);
        descriptionLabel.setSize(width, height / 2);
        descriptionLabel.setLocation(x, y);
        this.componentID = COMBO_BOX;

        comboBox = new JComboBox<>(listItems);
        comboBox.setFocusable(false);
        comboBox.setSize(128, 24);
        comboBox.setLocation(x, y + height / 2);
        comboBox.addActionListener(e -> onComboBoxUpdate((String) comboBox.getSelectedItem()));
    }

    OptionController(String label, int min, int max, int init, int row, int column, int tickSpacing) {
        int x = column * width + 16 + (column * 24);
        int y = row * height + 16 + (row * 24);

        descriptionLabel = new JLabel(label);
        descriptionLabel.setSize(width, height / 2);
        descriptionLabel.setLocation(x, y);
        this.componentID = SLIDER;

        slider = new JSlider(JSlider.HORIZONTAL, min, max, init);
        slider.addChangeListener(l -> onSliderUpdate(slider.getValue()));
        slider.setMajorTickSpacing(tickSpacing);
        slider.setMinorTickSpacing(50);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.setSize(300, (int)(height * 1.75));
        slider.setLocation(x, (int) (y + height / 2.75));
        slider.setFocusable(false);
    }

    void addComponentsToFrame(JFrame frame) {
        switch (componentID) {
            case BUTTON:
                frame.add(button);
                break;
            case CHECKBOX:
                frame.add(checkBox);
                break;
            case SLIDER:
                frame.add(slider);
                break;
            case COMBO_BOX:
                frame.add(comboBox);
                break;
            default:
                System.out.println("Could not add components to frame, componentID was unknown!");
                return;
        }
        frame.add(descriptionLabel);
    }

    void onUpdate() {}
    void onComboBoxUpdate(String selected) {}
    void onSliderUpdate(int value) {}

    void synchronizeComboBoxSelection(String selection) {
        comboBox.setSelectedItem(selection);
    }

}
