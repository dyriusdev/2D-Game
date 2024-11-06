package engine.graphics;

import engine.Engine;
import engine.Settings;
import engine.utils.ImageRequest;
import engine.utils.LightRequest;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Renderer {

    private final Font font = Font.Standard;

    private final ArrayList<ImageRequest> imageRequests = new ArrayList<>();
    private final ArrayList<LightRequest> lightRequests = new ArrayList<>();

    private final int pixelWidth, pixelHeight;
    private final int[] pixels, zBuffer, lightMap, lightBlock;
    private int ambientColor = 0xff232323, zDepth = 0;
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

        // Sort image request based on zDepth
        imageRequests.sort(Comparator.comparingInt(request0 -> request0.zDepth));

        // Draw requested images
        for (ImageRequest request : imageRequests) {
            SetZDepth(request.zDepth);
            DrawImage(request.image, request.xOffset, request.yOffset);
        }

        // Draw Lights
        for (LightRequest request : lightRequests) {
            DrawLightRequest(request.light, request.xOffset, request.yOffset);
        }

        // Finish image rendering and mixing light ambient with texture
        for (int i = 0; i < pixels.length; i++) {
            float r = ((lightMap[i] >> 16) & 0xff) / 255f;
            float g = ((lightMap[i] >> 8) & 0xff) / 255f;
            float b = (lightMap[i] & 0xff) / 255f;

            pixels[i] = ((int)(((pixels[i] >> 16) & 0xff) * r) << 16 | (int)(((pixels[i] >> 8) & 0xff) * g) << 8| (int)((pixels[i] & 0xff) * b));
        }

        imageRequests.clear();
        lightRequests.clear();
        processing = false;
    }


    public void DrawRect(int xOffset, int yOffset, int width, int height, int color) {
        for (int y = 0; y <= height; y++) {
            SetPixel(xOffset, y + yOffset, color);
            SetPixel(xOffset + width, y + yOffset, color);
        }

        for (int x = 0; x <= width; x++) {
            SetPixel(x + xOffset, yOffset, color);
            SetPixel(x + xOffset , yOffset + height, color);
        }
    }

    public void DrawFillRect(int xOffset, int yOffset, int width, int height, int color) {
        // Check if is out of bounds
        if (xOffset < -width || yOffset < -height || xOffset >= pixelWidth || yOffset >= pixelHeight) { return;  }

        int newX = 0, newY = 0, newWidth = width, newHeight = height;

        // Clipping
        if (xOffset < 0) { newX -= xOffset; }
        if (yOffset < 0) { newY -= yOffset; }
        if (newWidth + xOffset >= pixelWidth) { newWidth -= newWidth + xOffset - pixelWidth; }
        if (newHeight + yOffset >= pixelHeight) { newHeight -= newHeight + yOffset - pixelHeight; }

        for (int y = newY; y <= newHeight; y++) {
            for (int x = newX; x <= newWidth; x++) {
                SetPixel(x + xOffset, y + yOffset, color);
            }
        }
    }

    public void DrawImage(Image image, int xOffset, int yOffset) {
        if (image.IsAlpha() && !processing) {
            imageRequests.add(new ImageRequest(image, zDepth, xOffset, yOffset));
            return;
        }

        // Check if the image is out of bounds
        if (xOffset < -image.GetWidth() || yOffset < -image.GetHeight() || xOffset >= pixelWidth || yOffset >= pixelHeight) { return;  }

        int newX = 0, newY = 0, newWidth = image.GetWidth(), newHeight = image.GetHeight();

        //Clipping image
        if (xOffset < 0) { newX -= xOffset; }
        if (yOffset < 0) { newY -= yOffset; }
        if (newWidth + xOffset >= pixelWidth) { newWidth -= newWidth + xOffset - pixelWidth; }
        if (newHeight + yOffset >= pixelHeight) { newHeight -= newHeight + yOffset - pixelHeight; }

        // Get image pixels
        for (int y = newY; y < newHeight; y++) {
            for (int x = newX; x < newWidth; x++) {
                SetPixel(x + xOffset, y + yOffset, image.GetPixels()[x + y * image.GetWidth()]);
                SetLightBlockMap(x + xOffset, y + yOffset, image.GetLightBlockMode());
            }
        }
    }

    public void DrawImageTile(ImageTile image, int xOffset, int yOffset, int tileX, int tileY) {
        if (image.IsAlpha() && !processing) {
            imageRequests.add(new ImageRequest(image.GetTile(tileX, tileY), zDepth, xOffset, yOffset));
            return;
        }

        // Check if the image is out of bounds
        if (xOffset < -image.GetTileWidth() || yOffset < -image.GetTileHeight() || xOffset >= pixelWidth || yOffset >= pixelHeight) { return;  }

        int newX = 0, newY = 0, newWidth = image.GetTileWidth(), newHeight = image.GetTileHeight();

        // Clipping image
        if (xOffset < 0) { newX -= xOffset; }
        if (yOffset < 0) { newY -= yOffset; }
        if (newWidth + xOffset >= pixelWidth) { newWidth -= newWidth + xOffset - pixelWidth; }
        if (newHeight + yOffset >= pixelHeight) { newHeight -= newHeight + yOffset - pixelHeight; }

        for (int y = newY; y < newHeight; y++) {
            for (int x = newX; x < newWidth; x++) {
                SetPixel(x + xOffset, y + yOffset, image.GetPixels()[(x + tileX * image.GetTileWidth()) + (y + tileY * image.GetTileHeight()) * image.GetWidth()]);
                SetLightBlockMap(x + xOffset, y + yOffset, image.GetLightBlockMode());
            }
        }
    }

    public void DrawText(String text, int xOffset, int yOffset, int color) {
        text = text.toUpperCase();
        int offset = 0;
        for (int i = 0; i < text.length(); i++) {
            int unicode = text.codePointAt(i) - 32;
            for (int y = 0; y < font.GetImage().GetHeight(); y++) {
                for (int x = 0; x < font.GetWidths()[unicode]; x++) {
                    if (font.GetImage().GetPixels()[(x + font.GetOffsets()[unicode]) + y * font.GetImage().GetWidth()] == 0xffffffff) {
                        SetPixel(x + xOffset + offset, y + yOffset, color);
                    }
                }
            }
            offset += font.GetWidths()[unicode];
        }
    }



    public void AddLight(Light light, int xOffset, int yOffset) {
        lightRequests.add(new LightRequest(light, xOffset, yOffset));
    }



    private void SetPixel(int x, int y, int value) {
        // Extract alpha data from value
        int alpha = ((value >> 24) & 0xff);

        // Check if is out of bounds
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



    private void DrawLightRequest(Light light, int xOffset, int yOffset) {
        for (int i = 0; i <= light.GetDiameter(); i++) {
            DrawLightLine(light, light.GetRadius(), light.GetRadius(), i, 0, xOffset, yOffset);
            DrawLightLine(light, light.GetRadius(), light.GetRadius(), i, light.GetDiameter(), xOffset, yOffset);
            DrawLightLine(light, light.GetRadius(), light.GetRadius(), 0, i, xOffset, yOffset);
            DrawLightLine(light, light.GetRadius(), light.GetRadius(), light.GetDiameter(), i, xOffset, yOffset);
        }
    }

    private void DrawLightLine(Light light, int x0, int y0, int x1, int y1, int xOffset, int yOffset) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        int err2;

        while (true) {
            int screenX = x0 - light.GetRadius() + xOffset;
            int screenY = y0 - light.GetRadius() + yOffset;

            int color = light.GetLightValue(x0, y0);
            if (color == 0) { return; }

            if (screenX < 0 || screenX >= pixelWidth || screenY < 0 || screenY >= pixelHeight) { return; }
            if (lightBlock[screenX + screenY * pixelWidth] == Light.Full) { return; }

            SetLightMap(screenX, screenY, light.GetLightValue(x0, y0));

            if (x0 == x1 && y0 == y1) {
                break;
            }
            err2 = 2 * err;

            if (err2 > -1 * dy) {
                err -= dy;
                x0 += sx;
            }

            if (err2 < dx) {
                err += dx;
                y0 += sy;
            }
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



    public void SetZDepth(int value) { zDepth = value; }

    public int GetAmbientColor() { return ambientColor; }

    public void SetAmbientColor(int value) { ambientColor = value; }
}