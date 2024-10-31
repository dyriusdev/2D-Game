package engine.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image {

    private int width, height, lightBlockMode = Light.None;
    private int[] pixels;
    private boolean isAlpha = false;

    public Image(int[] pixels, int width, int height) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
    }

    public Image(String path) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        width = image.getWidth();
        height = image.getHeight();
        pixels = image.getRGB(0, 0, width, height, null, 0, width);
        image.flush();
    }

    public int GetLightBlockMode() { return lightBlockMode; }

    public int GetWidth() { return width; }

    public void SetWidth(int value) { width = value; }

    public int GetHeight() { return height; }

    public void SetHeight(int value) { height = value; }

    public int[] GetPixels() { return pixels; }

    public void SetPixels(int[] value) { pixels = value; }

    public boolean IsAlpha() { return isAlpha; }

    public void SetAlpha(boolean value) { isAlpha = value; }
}