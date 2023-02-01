package ru.kpfu.itis.gnt.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import static ru.kpfu.itis.gnt.GUI.MessengerAppFrame.*;

public class ChatPanel extends JPanel {

    private JPanel textPanel;
    private JScrollPane scrollPane;


    public ChatPanel() {
        this.setLayout(new BorderLayout());
        createMessageArea();
        this.add(scrollPane);
        this.setVisible(true);
    }

    private void createMessageArea() {
        textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textPanel.setBackground(SECONDARY_BLUE_LIGHT);
        scrollPane = new JScrollPane(textPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    public void addMessageLabel(String message) {
        JLabel label = new JLabel(message);
        textPanel.add(label);
        textPanel.revalidate();
        textPanel.repaint();
    }

    public void addImage(InputStream inputStream) throws IOException {
        InputStream is = new BufferedInputStream(inputStream);
        Image image = ImageIO.read(is);
        JLabel label = new JLabel(new ImageIcon(image));
        textPanel.add(label);
        textPanel.revalidate();
        textPanel.repaint();
    }

}
