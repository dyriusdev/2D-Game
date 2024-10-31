package engine.graphics;

public class Light {

    public static final int None = 0, Full = 1;

    private final int[] map;

    private int radius, diameter, color;

    public Light(int radius, int color) {
        this.radius = radius;
        diameter = radius * 2;
        this.color = color;
        map = new int[diameter * diameter];

        for (int y = 0; y < diameter; y++) {
            for (int x = 0; x < diameter; x++) {
                double distance = Math.sqrt((x - radius) * (x - radius) + (y - radius) * (y - radius));
                if (distance < radius) {
                    double power = 1 - (distance / radius);
                    map[x + y * diameter] = ((int)(((color >> 16) & 0xff) * power) << 16 | (int)(((color >> 8) & 0xff) * power) << 8| (int)((color & 0xff) * power));
                } else {
                    map[x + y * diameter] = 0;
                }
            }
        }
    }

    public int GetLightValue(int x, int y) {
        if (x < 0 || x >= diameter || y < 0 || y >= diameter) { return 0; }
        return map[x + y * diameter];
    }

    public int GetRadius() { return radius; }

    public void SetRadius(int radius) { this.radius = radius; }

    public int GetDiameter() { return diameter; }

    public void SetDiameter(int diameter) { this.diameter = diameter; }

    public int GetColor() { return color; }

    public void SetColor(int color) { this.color = color; }

    public int[] GetLightMap() { return map; }
}