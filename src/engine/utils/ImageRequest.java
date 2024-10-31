package engine.utils;

import engine.graphics.Image;

public class ImageRequest {

    public Image image;
    public int zDepth, xOffset, yOffset;

    public ImageRequest(Image image, int zDepth, int xOffset, int yOffset) {
        this.image = image;
        this.zDepth = zDepth;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
}