package io.github.saksham4106.blocky.engine;

import io.github.saksham4106.blocky.engine.callbacks.InputEventHandler;
import io.github.saksham4106.blocky.engine.challenges.SquareBoi;
import io.github.saksham4106.blocky.engine.challenges.Star;
import io.github.saksham4106.blocky.engine.renderer.ShaderProgram;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;


public class Window {

    Star star = new Star();
    SquareBoi squareBoi = new SquareBoi();


    private int width, height;
    private long window;
    public boolean isFullscreen = false;

    public Window(int width, int height){
        this.width = width;
        this.height = height;
    }
    public void run(){
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit()){
            throw new IllegalStateException("GLFW WINDOW NOT INITIALIZED");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);

        window = glfwCreateWindow(width, height, "smh", 0,  0);
        if(window == 0){
            throw new RuntimeException("FAILED TO CREATE GLFW WINDOW");
        }

        glfwSetKeyCallback(window, InputEventHandler.KeyEventHandler::keyCallback);
        glfwSetCursorPosCallback(window, InputEventHandler.CursorEventHandler::cursorPositionCallback);
        glfwSetMouseButtonCallback(window, InputEventHandler.MouseButtonEventHandler::mouseButtonCallback);
        glfwSetScrollCallback(window, InputEventHandler.ScrollEventHandler::scrollCallback);

        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            assert vidmode != null;
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        GL.createCapabilities();

        star.init();
        squareBoi.init();

        gameLoop();
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
    }

    private void gameLoop(){
        float start = (float) glfwGetTime();
        while(!glfwWindowShouldClose(window)){
            if(InputEventHandler.isKeyPressed(GLFW_KEY_F11)){
                toggleFullscreen();
            }

            glfwPollEvents();
            glClearColor(65 / 255f, 101.0f/ 255, 56.0f / 255, 1);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            float current = (float)glfwGetTime();
            float dt = current - start;
            star.update(dt);
            squareBoi.update(dt);
            //glDrawArrays(GL_TRIANGLES, 0, 6);
            glfwSwapBuffers(window);
            start = current;
        }
        star.destroy();
        squareBoi.destroy();

    }

    public void toggleFullscreen(){
        this.isFullscreen = !isFullscreen;
        glfwSetWindowMonitor(window, isFullscreen ? glfwGetPrimaryMonitor() : 0, 0, 0, width, height, GLFW_DONT_CARE);
    }
}
