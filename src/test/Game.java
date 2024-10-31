package test;

import engine.Engine;
import engine.Instance;
import engine.graphics.Image;
import engine.graphics.Light;
import engine.graphics.Renderer;

public class Game extends Instance {

    private final Image test1, test2;
    private final Light light;

    public Game() {
        test1 = new Image("assets/test1.png");
        test2 = new Image("assets/test2.png");

        light = new Light(50, 0xff00ffff);
    }

    @Override
    public void Update(Engine engine, float delta) {}

    @Override
    public void Render(Engine engine, Renderer renderer) {
        renderer.SetZDepth(0);

        renderer.DrawImage(test1, 0, 0);
        renderer.DrawImage(test2, 128, 128);

        renderer.AddLight(light, engine.GetInput().GetMouseX(), engine.GetInput().GetMouseY());
    }
}