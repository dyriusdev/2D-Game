package test;

import engine.Engine;

public class Run {

    public static void main(String[] args) {
        Engine engine = new Engine(new Game());
        engine.Start();
    }
}
