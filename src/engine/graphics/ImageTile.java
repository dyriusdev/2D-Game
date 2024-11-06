package engine.graphics;

public class ImageTile extends Image {

    private int tileWidth, tileHeight;

    public ImageTile(String path, int width, int height) {
        super(path);
        tileWidth = width;
        tileHeight = height;
    }

    public Image GetTile(int tileX, int tileY) {
        int[] pixels = new int[tileWidth * tileHeight];
        for (int y = 0; y < tileHeight; y++) {
            for (int x = 0; x < tileWidth; x++) {
                pixels[x + y * tileWidth] = GetPixels()[(x + tileX * tileWidth) + (y * tileY * tileHeight)] * GetWidth();
            }
        }
        return new Image(pixels, tileWidth, tileHeight);
    }

    public int GetTileWidth() { return tileWidth; }

    public void SetTileWidth(int value) { tileWidth = value; }

    public int GetTileHeight() { return tileHeight; }

    public void SetTileHeight(int value) { tileHeight = value; }
}