package lab5.clientSide;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

public class GraphPanel extends JPanel implements MouseListener, ChangeListener {
    private final static int SIZE_OF_POINT = 4;
    private float radius;
    private JLabel labelWithCoordinates;
    private JSpinner radiusSpinner;
    private JButton addButton;
    private int onScreenR;
    private ArrayList<Point> vectorOfPoints;
    private Map<Integer, StateOfPoints> points;
    private boolean radiusChanged;
    private Color colorOfThePlotArea = Color.black;
    private int countOfPointInArea = 0;
    private boolean spinnerIsChanged;
    private volatile boolean stateCursor = true; // true ==  active cursor on the graph
    private volatile boolean animationWasNotRun = true;
    private JLabel xLabel, yLabel, radiusLabel, addNewPointsButtonLabel;
    private ResourceBundle i18n;
    private Client client;

    public GraphPanel(float _r) {
        GraphPanel graphPanel1 = this;
        setRadius(_r);
        this.addMouseListener(this);
        vectorOfPoints = new ArrayList<>();
        radiusChanged = false;
        points = new HashMap<>();
        client = new Client();
        System.out.println("New thread has been created..\n");
        new Thread(() -> {
            while (true) {
                System.out.println("Receiving new data...");
                ServerAnswer ans = client.getFromTheServer();
                points.get(ans.pointCounter).setState((ans.stateOfPointsIs) ? State.POINT_HIT : State.POINT_MISS);
                graphPanel1.repaint();
            }
        }).start();
    }

    public JLabel getAddNewPointsButtonLabel() {
        return addNewPointsButtonLabel;
    }

    public void setResourceBundle(ResourceBundle i18n) {
        this.i18n = i18n;
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private void changeLocalization() {
        xLabel.setText((String) i18n.getObject("change_x_point"));
        yLabel.setText((String) i18n.getObject("change_y_point"));
        radiusLabel.setText((String) i18n.getObject("change_radius"));
        if (getLocale().getLanguage().equals("en")) {
            setLocale(new Locale("fi"));
            setResourceBundle(ResourceBundle.getBundle("lab5.resources.loc_data", getLocale()));
            addNewPointsButtonLabel.setText((String) i18n.getObject("change_label_with_coordinates"));
            setLocale(new Locale("en"));
        } else {
            setLocale(new Locale("en"));
            setResourceBundle(ResourceBundle.getBundle("lab5.resources.loc_data", getLocale()));
            addNewPointsButtonLabel.setText((String) i18n.getObject("change_label_with_coordinates"));
            setLocale(new Locale("fi"));
        }
        setResourceBundle(ResourceBundle.getBundle("lab5.resources.loc_data", getLocale()));
    }

    private boolean getStateCursor() {
        return stateCursor;
    }

    public JLabel getRadiusLabel() {
        return radiusLabel;
    }

    @Override
    protected void paintComponent(Graphics graph) {
        Graphics2D graphic = (Graphics2D) graph;
        super.paintComponent(graphic);
        Color backgroundColor = new Color(0xFF, 248, 116);
        Dimension size = this.getSize();
        onScreenR = Math.min(size.width, size.height) / 3;

        super.paintComponent(graphic);
        this.setBackground(backgroundColor);

        changeLocalization();

        drawGraph(graphic, size);

        if (radiusChanged) {
            points.clear();
            StateOfPoints.setIndex(0);
            vectorOfPoints.forEach(this::addMark);
            radiusChanged = false;
        }

        for (int i = 0; i < vectorOfPoints.size(); i++) {
            if (points.get(i).getState() == State.POINT_STATE_UNKNOWN) {
                client.sendPacket(points.get(i), radius);
                graphic.setColor(Color.GRAY);
                graphic.fillArc((int) ((vectorOfPoints.get(i).getX()) * (onScreenR / getRadius()) + size.width / 2 - (long) SIZE_OF_POINT / 2), (int) ((-vectorOfPoints.get(i).getY()) * (onScreenR / getRadius()) + size.height / 2 - (long) SIZE_OF_POINT / 2), (int) (long) SIZE_OF_POINT, (int) (long) SIZE_OF_POINT, 0, 360);
                getLabelWithCoordinates().setText(String.format("(%.3f; %.3f)\n", vectorOfPoints.get(i).getX(), vectorOfPoints.get(i).getY()));
            }
            if (points.get(i).getState() == State.POINT_HIT) {
                graphic.setColor(Color.GREEN);
                graphic.fillArc((int) ((vectorOfPoints.get(i).getX()) * (onScreenR / getRadius()) + size.width / 2 - (long) SIZE_OF_POINT / 2), (int) ((-vectorOfPoints.get(i).getY()) * (onScreenR / getRadius()) + size.height / 2 - (long) SIZE_OF_POINT / 2), (int) (long) SIZE_OF_POINT, (int) (long) SIZE_OF_POINT, 0, 360);
                getLabelWithCoordinates().setText(String.format("(%.3f; %.3f)\n", vectorOfPoints.get(i).getX(), vectorOfPoints.get(i).getY()));
            }
            if (points.get(i).getState() == State.POINT_MISS) {
                graphic.setColor(Color.RED);
                graphic.fillArc((int) ((vectorOfPoints.get(i).getX()) * (onScreenR / getRadius()) + size.width / 2 - (long) SIZE_OF_POINT / 2), (int) ((-vectorOfPoints.get(i).getY()) * (onScreenR / getRadius()) + size.height / 2 - (long) SIZE_OF_POINT / 2), (int) (long) SIZE_OF_POINT, (int) (long) SIZE_OF_POINT, 0, 360);
                getLabelWithCoordinates().setText(String.format("(%.3f; %.3f)\n", vectorOfPoints.get(i).getX(), vectorOfPoints.get(i).getY()));
            }
        }

        if (countOfPointInArea < getCountOfPointsInArea() && (spinnerIsChanged)) {
            countOfPointInArea = getCountOfPointsInArea();
            spinnerIsChanged = false;
            if (animationWasNotRun) runAnimation();
        }

    }

    private synchronized int getCountOfPointsInArea() {
        int count = 0;
        for (int i = 0; i < vectorOfPoints.size(); i++) {
            if (points.get(i).getState() == State.POINT_HIT) {
                count++;
            }
        }
        return count;
    }

    public void spinnerEvent() {
        getRadiusSpinner().addChangeListener(this);
    }

    public void addNewPoint(float x, float y) {
        radiusChanged = false;
        Point point = new Point(x, y);
        StateOfPoints stateOfPoints = new StateOfPoints(point, radius, State.POINT_STATE_UNKNOWN);
        vectorOfPoints.add(point);
        points.put(stateOfPoints.getId(), stateOfPoints);
        repaint();
    }

    public JLabel getXLabel() {
        return xLabel;
    }

    public JLabel getYLabel() {
        return yLabel;
    }

    public void changeLocaleLabels() {
        this.xLabel = new JLabel((String) i18n.getObject("change_x_point"));
        this.yLabel = new JLabel((String) i18n.getObject("change_y_point"));
        this.radiusLabel = new JLabel((String) i18n.getObject("change_radius"));
        this.addNewPointsButtonLabel = new JLabel((String) i18n.getObject("change_label_with_coordinates"));
    }

    public void changeLocale() {
        if (getLocale().equals(new Locale("en"))) {
            setLocale(new Locale("fi"));
            ResourceBundle i18n11 = ResourceBundle.getBundle("lab5.resources.loc_data", getLocale());
            setResourceBundle(i18n11);
        } else {
            setLocale(new Locale("en"));
            ResourceBundle i18n11 = ResourceBundle.getBundle("lab5.resources.loc_data", getLocale());
            setResourceBundle(i18n11);
        }
        repaint();
    }

    private void drawGraph(Graphics2D graphic, Dimension size) {
        int[] xPoints = new int[]{78, 238, 238};
        int[] yPoints = new int[]{245, 245, 84};

        graphic.setColor(colorOfThePlotArea);
        graphic.fillArc(size.width / 2 - onScreenR, size.height / 2 - onScreenR, onScreenR * 2, onScreenR * 2, -90, -90);
        graphic.fillRect(size.width / 2 + onScreenR / 4 - 39, size.height - onScreenR * 2 - 9, onScreenR, onScreenR / 2);
        graphic.fillPolygon(xPoints, yPoints, 3);
        graphic.setColor(Color.RED);

        graphic.drawLine(size.width / 2, size.height, size.width / 2, 0);
        graphic.drawLine(size.width / 2, 0, size.width / 2 - 4, 10);
        graphic.drawString("0", size.width / 2 + 2, size.height / 2 - 14);
        graphic.drawString("X", size.width - 10, size.height / 2 - 14);
        graphic.drawString("Y", size.width / 2 + 10, 10);
        graphic.drawLine(size.width / 2, 0, size.width / 2 + 4, 10);
        graphic.drawLine(0, size.height / 2, size.width, size.height / 2);
        graphic.drawLine(size.width - 10, size.height / 2 - 4, size.width, size.height / 2);
        graphic.drawLine(size.width - 10, size.height / 2 + 4, size.width, size.height / 2);
        graphic.drawLine(size.width / 2 - 4, size.height / 2 - onScreenR, size.width / 2 + 4, size.height / 2 - onScreenR);
        graphic.drawString("-R", size.width / 2 + 8, size.height / 2 + onScreenR + 5);
        graphic.drawLine(size.width / 2 - 4, size.height / 2 - onScreenR / 2, size.width / 2 + 4, size.height / 2 - onScreenR / 2);
        graphic.drawString("-R/2", size.width / 2 + 8, size.height / 2 + onScreenR / 2 + 5);
        graphic.drawLine(size.width / 2 - 4, size.height / 2 + onScreenR / 2, size.width / 2 + 4, size.height / 2 + onScreenR / 2);
        graphic.drawString("R/2", size.width / 2 + 8, size.height / 2 - onScreenR / 2 + 5);
        graphic.drawLine(size.width / 2 - 4, size.height / 2 + onScreenR, size.width / 2 + 4, size.height / 2 + onScreenR);
        graphic.drawString("R", size.width / 2 + 8, size.height / 2 - onScreenR + 5);
        graphic.drawLine(size.width / 2 - onScreenR, size.height / 2 - 4, size.width / 2 - onScreenR, size.height / 2 + 4);
        graphic.drawString("-R", size.width / 2 - onScreenR - 5, size.height / 2 - 10);
        graphic.drawLine(size.width / 2 - onScreenR / 2, size.height / 2 - 4, size.width / 2 - onScreenR / 2, size.height / 2 + 4);
        graphic.drawString("-R/2", size.width / 2 - onScreenR / 2 - 5, size.height / 2 - 10);
        graphic.drawLine(size.width / 2 + onScreenR / 2, size.height / 2 - 4, size.width / 2 + onScreenR / 2, size.height / 2 + 4);
        graphic.drawString("R/2", size.width / 2 + onScreenR / 2 - 5, size.height / 2 - 10);
        graphic.drawLine(size.width / 2 + onScreenR, size.height / 2 - 4, size.width / 2 + onScreenR, size.height / 2 + 4);
        graphic.drawString("R", size.width / 2 + onScreenR - 5, size.height / 2 - 10);
    }

    private synchronized void runAnimation() {
        animationWasNotRun = false;
        if (!animationWasNotRun) {
            Thread animation = new Thread(new Runnable() {
                @Override
                public void run() {
                    getRadiusSpinner().setEnabled(false);
                    getJButton().setEnabled(false);
                    try {
                        stateCursor = false;
                        for (int i = 0, j = 0, k = 0;
                             (i != 255) && (j != 245) && (k != 255);
                             i = (i + 1 <= 255) ? i + 1 : 255, j = (j + 1 <= 245) ? j + 1 : 245, k = (k + 1 <= 255) ? k + 1 : 255) {
                            colorOfThePlotArea = new Color(i, j, k);
                            repaint();
                            Thread.sleep(10);
                        }
                        for (int i = 255; i >= 220; i--) {
                            colorOfThePlotArea = new Color(i, 245, 255);
                            repaint();
                            Thread.sleep(30);
                        }
                        for (int i = 220, j = 255, k = 255;
                             (i >= 0) || (j >= 0) || (k >= 0);
                             i = (i - 1 >= 0) ? i - 1 : 0, j = (j - 1 >= 0) ? j - 1 : 0, k = (k - 1 >= 0) ? k - 1 : 0) {
                            colorOfThePlotArea = new Color(i, j, k);
                            repaint();
                            Thread.sleep(10);
                            if (i == 0 && k == 0 && j == 0) {
                                break;
                            }
                        }
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    } finally {
                        getRadiusSpinner().setEnabled(true);
                        getJButton().setEnabled(true);
                        stateCursor = true;
                    }
                    synchronized (this) {
                        repaint();
                    }
                }
            });
            animation.setDaemon(true);
            animation.start();
        }
        animationWasNotRun = true;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSpinner s = (JSpinner) e.getSource();
        countOfPointInArea = getCountOfPointsInArea();
        synchronized (this) {
            setRadius((Integer) s.getValue());
            radiusChanged = true;
            spinnerIsChanged = true;
            repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (getStateCursor()) {
            spinnerIsChanged = false;
            Point temp = new Point((e.getX()) - this.getSize().width / 2, (e.getY()) - this.getSize().height / 2);
            temp.setX(temp.getX() * (getRadius() / (float) onScreenR));
            temp.setY(-temp.getY() * (getRadius() / (float) onScreenR));
            vectorOfPoints.add(temp);
            addMark(temp);
            radiusChanged = false;
            repaint();
        }
    }

    private float getRadius() {
        return radius;
    }

    private void setRadius(float radius) {
        this.radius = radius;
    }

    public JLabel getLabelWithCoordinates() {
        return labelWithCoordinates;
    }

    public void setLabelWithCoordinates(JLabel labelWithCoordinates) {
        this.labelWithCoordinates = labelWithCoordinates;
    }

    public JSpinner getRadiusSpinner() {
        return radiusSpinner;
    }

    public void setRadiusSpinner(JSpinner radiusSpinner) {
        this.radiusSpinner = radiusSpinner;
        JTextField tempTextField = ((JSpinner.DefaultEditor) radiusSpinner.getEditor()).getTextField();
        tempTextField.setEditable(false);
    }

    private JButton getJButton() {
        return addButton;
    }

    public void setJButton(JButton jButton) {
        this.addButton = jButton;
    }

    private void addMark(Point mrk) {
        clearMarks();
        StateOfPoints mw = new StateOfPoints(mrk, radius, State.POINT_STATE_UNKNOWN);
        points.put(mw.getId(), mw);
    }

    private void clearMarks() {
        for (StateOfPoints mw : points.values()) {
            mw.setState(State.POINT_STATE_UNKNOWN);
        }
    }

}
