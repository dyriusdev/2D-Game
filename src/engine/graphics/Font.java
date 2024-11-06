package engine.graphics;

public class Font {

    public static final Font Standard = new Font("assets/fonts/default.png");

    private Image fontImage;
    private int[] offsets = new int[59], widths = new int[59];

    public Font(String path) {
        fontImage = new Image(path);

        int unicode = 0;
        for (int i = 0; i < fontImage.GetWidth(); i++) {
            if (fontImage.GetPixels()[i] == 0xff0000ff) {
                offsets[unicode] = i;
            }

            if (fontImage.GetPixels()[i] == 0xffffff00) {
                widths[unicode] = i - offsets[unicode];
                unicode++;
            }
        }
    }

    public Image GetImage() { return fontImage; }

    public void SetImage(Image value) { fontImage = value; }

    public int[] GetOffsets() { return offsets; }

    public void SetOffsets(int[] value) { offsets = value; }

    public int[] GetWidths() { return widths; }

    public void SetWidths(int[] value) { widths = value; }
}