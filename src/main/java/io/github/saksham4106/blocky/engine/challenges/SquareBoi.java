package io.github.saksham4106.blocky.engine.challenges;

import io.github.saksham4106.blocky.engine.callbacks.InputEventHandler;
import io.github.saksham4106.blocky.engine.renderer.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class SquareBoi  implements IChallengeRender{
    ShaderProgram shader = new ShaderProgram();
    Matrix4f projectionMatrix = new Matrix4f();
    Matrix4f viewMatrix = new Matrix4f();
    int vao, vbo, ebo;

    Vector3f  Pos = new Vector3f(0.0f, 0f, 1.0f);
    Vector3f  Color = new Vector3f(150.0f/255.0f,  100.0f / 255.f, 233.0f/ 255.f);
    float rotation = 0.0f;


    float[] squareVertices = {
            -0.5f,  0.5f, 0f,
             0.5f,  0.5f, 0f,
             0.5f, -0.5f, 0f,
            -0.5f, -0.5f, 0f
    };

    int[] elements = {
        0, 1, 2,
        2, 0, 3
    };

    @Override
    public void update(float dt) {
        shader.bind();
        Vector3f eye = new Vector3f(0.0f, 0.0f, 20.0f);
        Vector3f center = new Vector3f(0.0f, 0.0f, 0.0f);
        Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
        viewMatrix.lookAt(eye, center, up);

        shader.uploadMat4f("uView", viewMatrix);
        shader.uploadMat4f("uProjection", projectionMatrix);

        if(InputEventHandler.isKeyPressed(GLFW_KEY_S)){
             Pos.y -= dt * 1f;
        }
        if(InputEventHandler.isKeyPressed(GLFW_KEY_A)){
             Pos.x -= dt * 1f;
        }
        if(InputEventHandler.isKeyPressed(GLFW_KEY_W)){
            Pos.y += dt * 1f;
        }
        if(InputEventHandler.isKeyPressed(GLFW_KEY_D)){
            Pos.x += dt * 1f;
        }

        Matrix4f transform = new Matrix4f();
        transform.scale(1.0f);
        transform.translate(new Vector3f( Pos));
        transform.rotate(rotation , new Vector3f(0.0f, 0.0f, 1.0f));
        shader.uploadMat4f("uTransform", transform);
        shader.uploadVec3("uColor",  Color);
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
    }

    @Override
    public void init() {
        float aspect = 1366.0f / 768.0f;
        float projectionWidth = 10.0f;
        float projectionHeight = projectionWidth / aspect;

        float left = -projectionWidth / 2.0f;
        float right = projectionWidth / 2.0f;
        float bottom = -projectionHeight / 2.0f;
        float top = projectionHeight / 2.0f;
        float near = 0.01f;
        float far = 2000;
        projectionMatrix.ortho(left, right, bottom, top, near, far);

        shader.compileAndLinkShader("src/main/resources/assets/shaders/vertex/basic_vs.glsl",
                "src/main/resources/assets/shaders/fragment/basic_fs.glsl");

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(squareVertices.length);
        vertexBuffer.put(squareVertices).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        ebo = glGenBuffers();
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elements.length);
        elementBuffer.put(elements).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

    }

    @Override
    public void destroy() {
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
        glDeleteVertexArrays(vao);
        shader.unbind();
        shader.destroy();
    }
}
