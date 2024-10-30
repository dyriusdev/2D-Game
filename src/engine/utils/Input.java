package engine.utils;

import engine.Engine;
import engine.Settings;

import java.awt.event.*;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {


    private final int NumKeys = 256, NumButtons = 5;
    private final boolean[] keys = new boolean[NumKeys], keysLast = new boolean[NumKeys],
            buttons = new boolean[NumButtons], buttonsLast = new boolean[NumButtons];

    private int mouseX, mouseY, scroll;

    public Input(Engine engine) {
        mouseX = 0;
        mouseY = 0;
        scroll = 0;

        engine.GetWindow().GetCanvas().addKeyListener(this);
        engine.GetWindow().GetCanvas().addMouseListener(this);
        engine.GetWindow().GetCanvas().addMouseMotionListener(this);
        engine.GetWindow().GetCanvas().addMouseWheelListener(this);
    }

    public void Update() {
        scroll = 0;

        System.arraycopy(keys, 0, keysLast, 0, NumKeys);
        System.arraycopy(buttons, 0, buttonsLast, 0, NumButtons);
    }

    public boolean IsKey(int code) { return keys[code]; }

    public boolean IsKeyUp(int code) { return !keys[code] && keysLast[code]; }

    public boolean IsKeyDown(int code) { return keys[code] && !keysLast[code]; }

    public boolean IsButton(int code) { return buttons[code]; }

    public boolean IsButtonUp(int code) { return !buttons[code] && buttonsLast[code]; }

    public boolean IsButtonDown(int code) { return buttons[code] && !buttonsLast[code]; }



    @Override
    public void keyTyped(KeyEvent keyEvent) {}

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        keys[keyEvent.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        keys[keyEvent.getKeyCode()] = false;
    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {}

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        buttons[mouseEvent.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        buttons[mouseEvent.getButton()] = false;
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}


    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        mouseX = (int)(mouseEvent.getX() / Settings.Scale);
        mouseY = (int)(mouseEvent.getY() / Settings.Scale);
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        mouseX = (int)(mouseEvent.getX() / Settings.Scale);
        mouseY = (int)(mouseEvent.getY() / Settings.Scale);
    }


    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        scroll = mouseWheelEvent.getWheelRotation();
    }

    public int GetMouseX() { return mouseX; }

    public int GetMouseY() { return mouseY; }

    public int GetScroll() { return scroll; }
}