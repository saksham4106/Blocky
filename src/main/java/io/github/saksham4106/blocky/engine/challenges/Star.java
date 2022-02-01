package io.github.saksham4106.blocky.engine.challenges;

import io.github.saksham4106.blocky.engine.renderer.ShaderProgram;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Star implements IChallengeRender {
    int vao, vbo, ebo;
    ShaderProgram shaderProgram = new ShaderProgram();
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
    @Override
    public void update(float dt){
        shaderProgram.bind();
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, 24, GL_UNSIGNED_INT, 0);
    }

    @Override
    public void init() {
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
        shaderProgram.compileAndLinkShader("src/main/resources/assets/shaders/vertex/star_vs.glsl",
                "src/main/resources/assets/shaders/fragment/star_fs.glsl");
    }
    @Override
    public void destroy(){
        shaderProgram.destroy();
        glDeleteBuffers(ebo);
        glDeleteBuffers(vbo);
        glDeleteVertexArrays(vao);
    }
}
