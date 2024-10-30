package engine.graphics;

import engine.Engine;
import engine.Settings;

import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class Renderer {

    private int pixelWidth, pixelHeight, ambientColor = 0xff232323, zDepth = 0;
    private int[] pixels, zBuffer, lightMap, lightBlock;
    private boolean processing = false;

    public Renderer(Engine engine) {
        pixelWidth = Settings.Width;
        pixelHeight = Settings.Height;

        pixels = ((DataBufferInt)engine.GetWindow().GetImage().getRaster().getDataBuffer()).getData();
        zBuffer = new int[pixels.length];
        lightMap = new int[pixels.length];
        lightBlock = new int[pixels.length];
    }

    public void Clear() {
        Arrays.fill(pixels, 0);
        Arrays.fill(zBuffer, 0);
        Arrays.fill(lightMap, ambientColor);
    }

    public void Process() {
        processing = true;



        processing = false;
    }


    public void DrawRect(int xOffset, int yOffset, int width, int height, int color) {}

    public void DrawFillRect(int xOffset, int yOffset, int width, int height, int color) {}

    public void DrawImage() {}

    public void DrawImageTile() {}

    public void DrawLight() {}

    public void DrawText() {}



    public int GetZDepth() { return zDepth; }

    public void SetZDepth(int value) { zDepth = value; }



    private void SetPixel(int x, int y, int value) {
        // extract alpha data from value
        int alpha = ((value >> 24) & 0xff);

        // Check if the xy position is out of bounds
        if ((x < 0 || x >= pixelWidth || y < 0 || y >= pixelHeight) || alpha == 0) { return; }

        // Pixel buffer "pointer"
        int index = x + y * pixelWidth;

        if (zBuffer[index] > zDepth) { return; }

        /*
        When the alpha value is 255 its means that the value is a solid color
        so can be just replaced on pixels buffer
        otherwise need to get the color in xy position and mix the actual color with the new one
         */
        if (alpha == 255) {
            pixels[index] = value;
        } else {
            int color = pixels[index];
            // Mixing RED-GREEN-BLUE (RGB)
            int newRed = ((color >> 16) & 0xff) - (int)((((color >> 16) & 0xff) - ((value >> 16) & 0xff)) * (alpha / 255f));
            int newGreen = ((color >> 8) & 0xff) - (int)((((color >> 8) & 0xff) - ((value >> 8) & 0xff)) * (alpha / 255f));
            int newBlue = (color & 0xff) - (int)(((color & 0xff) - (value & 0xff)) * (alpha / 255f));

            pixels[index] = (255 << 24 | newRed << 16 | newGreen << 8 | newBlue);
        }
    }

    private void SetLightMap(int x, int y, int value) {
        if ((x < 0 || x >= pixelWidth || y < 0 || y >= pixelHeight)) { return; }

        int baseColor = lightMap[x + y * pixelWidth];
        int maxRed = Math.max((baseColor >> 16) & 0xff, (value >> 16) & 0xff);
        int maxGreen = Math.max((baseColor >> 8) & 0xff, (value >> 8) & 0xff);
        int maxBlue =  Math.max(baseColor & 0xff, value & 0xff);
        lightMap[x + y * pixelWidth] = (maxRed << 16 | maxGreen << 8 | maxBlue);
    }

    private void SetLightBlockMap(int x, int y, int value) {
        if ((x < 0 || x >= pixelWidth || y < 0 || y >= pixelHeight)) { return; }
        if (zBuffer[x + y * pixelWidth] > zDepth) { return; }
        lightBlock[x + y * pixelWidth] = value;
    }


}