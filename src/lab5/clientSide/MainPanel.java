package lab5.clientSide;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JFrame {
    private GraphPanel graphPanel;
    private DataPanel dataPanel;

    public MainPanel() {
        setGraphPanel(new GraphPanel(50));
        setDataPanel(new DataPanel(getGraphPanel()));

        Container container = this.getContentPane();
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(getGraphPanel(), BorderLayout.CENTER);
        mainPanel.add(getDataPanel(), BorderLayout.WEST);
        container.add(mainPanel);
        container.setMinimumSize(new Dimension(200, 200));

        setTitle("Lab5");
        setSize(710, 520);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainPanel();
    }

    public DataPanel getDataPanel() {
        return dataPanel;
    }

    public void setDataPanel(DataPanel dataPanel) {
        this.dataPanel = dataPanel;
    }

    public GraphPanel getGraphPanel() {
        return graphPanel;
    }

    public void setGraphPanel(GraphPanel graphPanel) {
        this.graphPanel = graphPanel;
    }

}
