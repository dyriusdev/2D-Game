package engine;

import engine.graphics.Renderer;
import engine.graphics.Window;
import engine.utils.Input;

public class Engine implements Runnable {

    private final Thread engineThread;
    private final Game game;

    private boolean isRunning = false;

    private Window window;
    private Renderer renderer;
    private Input input;

    public Engine(Game game) {
        this.game = game;
        engineThread = new Thread(this);
    }

    public void Start() {
        window = new Window();
        renderer = new Renderer(this);
        input = new Input(this);

        engineThread.start();
    }

    public void Stop() { isRunning = false; }

    public Window GetWindow() { return window; }

    public Input GetInput() { return input; }

    @Override
    public void run() {
        isRunning = true;

        double firstTime = 0, lastTime = System.nanoTime() / 1000000000d, passedTime = 0, unprocessedTime = 0, frameTime = 0;
        int frames = 0, fps = 0;

        while (isRunning) {
            boolean canRender = false;
            firstTime = System.nanoTime() / 1000000000d;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;
            frameTime += passedTime;

            double updateCap = 1 / 60f;
            while (unprocessedTime >= updateCap) {
                unprocessedTime -= updateCap;
                canRender = true;

                game.Update(this, (float) updateCap);

                input.Update();

                if (frameTime >= 1) {
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                }
            }

            if (canRender) {
                game.Render(this, renderer);
                window.Update();
                frames++;
            } else {
                synchronized (this) {
                    try { wait(2); }
                    catch (InterruptedException e) { System.err.println(e.getMessage()); }
                }
            }
        }
    }
}