package io.github.saksham4106.blocky.engine.callbacks;

import static org.lwjgl.glfw.GLFW.*;

public class InputEventHandler {
    public static boolean[] keys = new boolean[GLFW_KEY_LAST];
    public static boolean[] mouseButtons = new boolean[GLFW_MOUSE_BUTTON_LAST];
    public static float xMousePos, yMousePos;
    public static float xScroll, yScroll;

    public static class KeyEventHandler{
        public static void keyCallback(long window, int key, int scancode, int action, int mods){
            if(key <=  GLFW_KEY_LAST && key >= 0 ){
                keys[key] = (action == GLFW_PRESS);
            }
        }
    }
    public static class CursorEventHandler{
        public static void cursorPositionCallback(long window, double xPos, double yPos){
            xMousePos = (float)xPos;
            yMousePos = (float)yPos;
        }
    }

    public static class MouseButtonEventHandler{
        public static void mouseButtonCallback(long window, int button, int action, int mods){
            if(button >= 0 && button <= GLFW_MOUSE_BUTTON_LAST){
                mouseButtons[button] = (action == GLFW_PRESS);
            }
        }
    }

    public static class ScrollEventHandler{
        public static void scrollCallback(long window, double xOffset, double yOffset){
            xScroll = (float) xOffset;
            yScroll = (float) yOffset;
        }
    }

    public static boolean isKeyPressed(int key) {
        if (key <= GLFW_KEY_LAST && key >= 0) {
            return keys[key];
        }
        return false;
    }

    public static boolean isMouseButtonPressed(int button){
        if(button >= 0 && button <= GLFW_MOUSE_BUTTON_LAST){
            return mouseButtons[button];
        }
        return false;
    }
}
