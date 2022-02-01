package io.github.saksham4106.blocky.engine.renderer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.io.File;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
    int programID;

    public void compileAndLinkShader(String vertexShaderPath, String fragmentShadePath){
        Shader vertexShader = new Shader();
        vertexShader.compile(GL_VERTEX_SHADER, vertexShaderPath);
        Shader fragmentShader = new Shader();
        fragmentShader.compile(GL_FRAGMENT_SHADER, fragmentShadePath);

        programID = glCreateProgram();
        glAttachShader(programID, vertexShader.shader);
        glAttachShader(programID, fragmentShader.shader);
        glLinkProgram(programID);

        if(glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE){
            System.out.println("Shader Linking Failed!");
            System.out.println(glGetProgramInfoLog(programID, glGetProgrami(programID, GL_INFO_LOG_LENGTH)));
            vertexShader.destroy();
            fragmentShader.destroy();
            glDeleteProgram(programID);
            programID = Integer.MIN_VALUE;
            return;
        }

        glDetachShader(programID, vertexShader.shader);
        glDetachShader(programID, fragmentShader.shader);
        vertexShader.destroy();
        fragmentShader.destroy();
    }

    public void destroy(){
        if(programID != Integer.MIN_VALUE){
            glDeleteProgram(programID);
            programID = Integer.MIN_VALUE;
        }
    }

    public void bind(){
        glUseProgram(programID);
    }

    public void unbind(){
        glUseProgram(0);
    }


    public void uploadMat4f(String uName, Matrix4f mat){
        int varLocation = glGetUniformLocation(programID, uName);
        glUseProgram(programID);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadVec3(String uName, Vector3f vec){
        int varLocation = glGetUniformLocation(programID, uName);
        glUseProgram(programID);
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

}
