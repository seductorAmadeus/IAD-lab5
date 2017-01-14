package lab5.clientSide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class DataPanel extends JPanel implements ActionListener, MouseListener {
    private GraphPanel graphPanel;
    private JComboBox<String> xCoordinatesComboBox;
    private ArrayList<JCheckBox> yCoordinatesArrayList;

    DataPanel(GraphPanel graphPanel) {
        setYCoordinatesArrayList(new ArrayList<>());
        this.graphPanel = graphPanel;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(230, 500));

        /* Configure localization for the two languages: Finnish and English*/
        graphPanel.setLocale(new Locale("en"));
        graphPanel.setResourceBundle(ResourceBundle.getBundle("lab5.resources.loc_data", new Locale("en")));
        graphPanel.changeLocaleLabels();

        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
        checkBoxPanel.add(graphPanel.getXLabel());
        checkBoxPanel.setMaximumSize(new Dimension(200, 100));
        for (int i = 0; i < 5; i++) {
            JCheckBox c = new JCheckBox(Integer.toString(20 * i));
            getYCoordinatesArrayList().add(c);
            checkBoxPanel.add(c);
        }

        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.setLayout(new BoxLayout(comboBoxPanel, BoxLayout.Y_AXIS));
        comboBoxPanel.add(graphPanel.getYLabel());
        setXCoordinatesComboBox(new JComboBox<>(new String[]{"0", "10", "20", "30", "40"}));
        comboBoxPanel.add(getXCoordinatesComboBox());

        JPanel radiusPanel = new JPanel();
        radiusPanel.setLayout(new BoxLayout(radiusPanel, BoxLayout.Y_AXIS));
        radiusPanel.add(graphPanel.getRadiusLabel());
        graphPanel.setRadiusSpinner(new JSpinner(new SpinnerNumberModel(50, 0, 200, 1)));
        graphPanel.spinnerEvent();
        radiusPanel.add(graphPanel.getRadiusSpinner());
        radiusPanel.add(Box.createVerticalStrut(50));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        JButton addNewPointsButton = new JButton(graphPanel.getAddNewPointsButtonLabel().getText());
        addNewPointsButton.addActionListener(this);
        addNewPointsButton.addMouseListener(this);
        addNewPointsButton.setMargin(new Insets(20, 30, 20, 30));
        graphPanel.setJButton(addNewPointsButton);
        JButton localeChangeButton = new JButton("EN/FI");
        localeChangeButton.addActionListener(e -> {
            addNewPointsButton.setText(graphPanel.getAddNewPointsButtonLabel().getText());
            graphPanel.changeLocale();
            repaint();
        });
        buttonPanel.add(localeChangeButton);
        graphPanel.setLabelWithCoordinates(new JLabel());
        buttonPanel.add(graphPanel.getLabelWithCoordinates());
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(addNewPointsButton);

        /* added all panels on the main panel. */
        this.add(comboBoxPanel);
        this.add(checkBoxPanel);
        this.add(radiusPanel);
        this.add(buttonPanel);
    }

    public void actionPerformed(ActionEvent e) {
        float x = new Float((String) getXCoordinatesComboBox().getSelectedItem());
        for (JCheckBox c : getYCoordinatesArrayList()) {
            float y = new Float(c.getText());
            if (c.isSelected()) {
                graphPanel.addNewPoint(x, y);
            }
        }
        repaint();
    }

    private JComboBox<String> getXCoordinatesComboBox() {
        return xCoordinatesComboBox;
    }

    private void setXCoordinatesComboBox(JComboBox<String> jComboBox) {
        this.xCoordinatesComboBox = jComboBox;
    }

    private ArrayList<JCheckBox> getYCoordinatesArrayList() {
        return yCoordinatesArrayList;
    }

    private void setYCoordinatesArrayList(ArrayList<JCheckBox> yCoordinatesArrayList) {
        this.yCoordinatesArrayList = yCoordinatesArrayList;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        graphPanel.repaint();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    }

}
