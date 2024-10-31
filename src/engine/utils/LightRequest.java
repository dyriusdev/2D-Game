package engine.utils;

import engine.graphics.Light;

public class LightRequest {

    public Light light;
    public int xOffset, yOffset;

    public LightRequest(Light light, int xOffset, int yOffset) {
        this.light = light;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
}