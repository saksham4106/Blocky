package io.github.saksham4106.blocky.engine.renderer;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    String vertexShaderSource = "#version 330 core\n" +
            "layout (location = 0) in vec4 aColor;\n" +
            "layout (location = 1) in vec3 aPosition;\n" +
            "out vec4 fColor;\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPosition, 1.0);\n" +
            "}";

    String fragmentShaderSource = "#version 330\n" +
            "out vec4 FragColor;\n" +
            "in vec4 fColor;\n" +
            "void main()\n" +
            "{\n" +
            "    FragColor = fColor;\n" +
            "}";
    int programId;

    public void compile(){
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);

        System.out.println(glGetShaderInfoLog(vertexShader));

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);

        System.out.println(glGetShaderInfoLog(fragmentShader));

        programId = glCreateProgram();
        glAttachShader(programId, vertexShader);
        glAttachShader(programId, fragmentShader);
        glLinkProgram(programId);

        System.out.println(glGetProgramInfoLog(programId));
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    public void bind(){
        glUseProgram(programId);
    }
}
