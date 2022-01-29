package io.github.saksham4106.blocky.engine;

import io.github.saksham4106.blocky.engine.callbacks.InputEventHandler;
import io.github.saksham4106.blocky.engine.renderer.Shader;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryStack.stackPush;


public class Window {
    private int width, height;
    private long window;
    public boolean isFullscreen = false;
    static Shader shader = new Shader();

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


        gameLoop();
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
    }

    private void gameLoop(){
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        GL.createCapabilities();
        int vao, vbo, ebo;
//        float[] square = {
//            0.3f, 0.1f, 0.5f, 1,    0, 0.3f, 1,
//            0.1f, 0.4f, 0.2f, 1,    0.1f, 0, 1,
//            0.2f, 0.4f, 0.6f, 1,    0.4f, 0, 1,
//            0.3f, 0.6f, 0.8f, 1,    0.15f, -0.2f, 1,
//            0.1f, 0.5f, 0.7f, 1,    0.25f, -0.5f, 1,
//            0.2f, 0.4f, 0.1f, 1,   -0, -0.3f, 1,
//            0.1f, 0.4f, 0.6f, 1,   -0.25f, -0.5f, 1,
//            0.6f, 0.6f, 0.8f, 1,   -0.15f, -0.2f, 1,
//            0.7f, 0.3f, 0.1f, 1,   -0.4f, 0, 1
//        };

        float[] star = {
                0.1f, 0.1f, 0.5f, 1,    0, 0.3f, 1,
                0.1f, 0.2f, 0.7f, 1,    0.1f, 0, 1,
                0.2f, 0.3f, 0.6f, 1,    0.35f, 0, 1,
                0.1f, 0.3f, 0.8f, 1,    0.15f, -0.25f, 1,
                0.3f, 0.2f, 0.7f, 1,    0.25f, -0.55f, 1,
                0.2f, 0.3f, 0.5f, 1,   -0, -0.35f, 1,
                0.4f, 0.1f, 0.7f, 1,   -0.25f, -0.55f, 1,
                0.3f, 0.2f, 0.8f, 1,   -0.15f, -0.25f, 1,
                0.1f, 0.2f, 0.9f, 1,   -0.35f, 0, 1,
                0.2f, 0.4f, 0.6f, 1,   -0.1f, 0, 1
        };
        int[] elements = {
                1, 0, 9,
                9, 8, 7,
                7, 9, 1,
                1, 7, 3,
                3, 5, 7,
                7, 6, 5,
                5, 4, 3,
                3, 2, 1
        };

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(star.length);
        vertexBuffer.put(star).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);


        ebo = glGenBuffers();
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elements.length);
        elementBuffer.put(elements).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 4, GL_FLOAT, false, 7 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 7 * Float.BYTES, 4 * Float.BYTES);
        glEnableVertexAttribArray(1);

        shader.compile();

        shader.bind();

        while(!glfwWindowShouldClose(window)){
            glfwPollEvents();
            glClearColor(65 / 255f, 101.0f/ 255, 56.0f / 255, 1);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//            if(InputEventHandler.isKeyPressed(GLFW_KEY_F2)){
//                glDeleteBuffers(vbo);
//                glDeleteBuffers(ebo);
//                glDeleteVertexArrays(vao);
//            }


            glBindVertexArray(vao);
            glDrawElements(GL_TRIANGLES, 24, GL_UNSIGNED_INT, 0);
            //glDrawArrays(GL_TRIANGLES, 0, 6);


            glfwSwapBuffers(window);

        }
    }

    public void toggleFullscreen(){
        this.isFullscreen = !isFullscreen;
        glfwSetWindowMonitor(window, isFullscreen ? glfwGetPrimaryMonitor() : 0, 0, 0, width, height, GLFW_DONT_CARE);
    }
}
