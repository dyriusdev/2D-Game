package engine.graphics;

import engine.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Window {

    private final BufferedImage image;
    private final Canvas canvas;

    public Window() {
        Dimension dimension = new Dimension((int)(Settings.Width * Settings.Scale), (int)(Settings.Height * Settings.Scale));

        image = new BufferedImage(Settings.Width, Settings.Height, BufferedImage.TYPE_INT_RGB);
        canvas = new Canvas();
        canvas.setBackground(Color.black);
        canvas.setPreferredSize(dimension);
        canvas.setMaximumSize(dimension);
        canvas.setMinimumSize(dimension);

        JFrame frame = new JFrame(Settings.Title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(canvas, BorderLayout.CENTER);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
    }

    public void Update() {
        // Fixing error of black window
        BufferStrategy strategy = canvas.getBufferStrategy();
        if (strategy == null) {
            canvas.createBufferStrategy(2);
            strategy = canvas.getBufferStrategy();
        }

        Graphics graphics = strategy.getDrawGraphics();
        graphics.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
        strategy.show();
    }

    public Canvas GetCanvas() { return canvas; }

    public BufferedImage GetImage() { return image; }
}