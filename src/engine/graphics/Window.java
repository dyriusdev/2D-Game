package engine.graphics;

import engine.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Window {

    private final JFrame frame;
    private final BufferedImage image;
    private final Canvas canvas;
    private final BufferStrategy strategy;
    private final Graphics graphics;

    public Window() {
        Dimension dimension = new Dimension((int)(Settings.Width * Settings.Scale), (int)(Settings.Height * Settings.Scale));

        image = new BufferedImage(Settings.Width, Settings.Height, BufferedImage.TYPE_INT_RGB);
        canvas = new Canvas();
        canvas.setBackground(Color.black);
        canvas.setPreferredSize(dimension);
        canvas.setMaximumSize(dimension);
        canvas.setMinimumSize(dimension);

        frame = new JFrame(Settings.Title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(canvas, BorderLayout.CENTER);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        strategy = canvas.getBufferStrategy();
        graphics = strategy.getDrawGraphics();
    }

    public void Update() {
        graphics.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
        strategy.show();
    }

    public JFrame GetFrame() { return frame; }

    public Canvas GetCanvas() { return canvas; }

    public BufferedImage GetImage() { return image; }
}