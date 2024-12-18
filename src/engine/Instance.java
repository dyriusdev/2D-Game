package engine;

import engine.graphics.Renderer;

public abstract class Instance {

    public abstract void Update(Engine engine, float delta);

    public abstract void Render(Engine engine, Renderer renderer);
}