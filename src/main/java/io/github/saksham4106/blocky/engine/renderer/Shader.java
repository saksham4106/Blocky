package io.github.saksham4106.blocky.engine.renderer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    int shader;
    public void compile(int SHADER_TYPE, String shaderPath){
        try{
            String shaderSource = readShader(shaderPath);
            shader = glCreateShader(SHADER_TYPE);
            glShaderSource(shader, shaderSource);
            glCompileShader(shader);

            int success = glGetShaderi(shader, GL_COMPILE_STATUS);
            if(success == GL_FALSE){
                System.out.println("Shader Compilation at path " + shaderPath + " failed!");
                System.out.println(glGetShaderInfoLog(shader, glGetShaderi(shader, GL_INFO_LOG_LENGTH)));
                glDeleteShader(shader);
                shader = Integer.MIN_VALUE;
            }

        } catch( IOException e){
            System.out.println("Shader at path " + shaderPath  + " not found!");
        }

    }

    public void destroy(){
        if(shader != Integer.MIN_VALUE){
            glDeleteShader(shader);
            shader = Integer.MIN_VALUE;
        }
    }

    private String readShader(String filepath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filepath)));
    }
}
