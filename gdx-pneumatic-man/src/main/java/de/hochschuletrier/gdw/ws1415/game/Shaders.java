package de.hochschuletrier.gdw.ws1415.game;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Shaders {
    
//    public static final ShaderProgram HEAT_SHADER = new ShaderProgram(
//            Gdx.files.internal("src/main/resources/data/BasicShader.vert"),
//            Gdx.files.internal("src/main/resources/data/BlurShader.frag"));
//    public static final ShaderProgram TEST_SHADER = new ShaderProgram(
//            Gdx.files.internal("src/main/resources/data/BasicShader.vert"),
//            Gdx.files.internal("src/main/resources/data/BlurShader.frag"));
    
    private static String basicVertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
            + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
            + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
            + "uniform mat4 u_projTrans;\n" //
            + "varying vec4 v_color;\n" //
            + "varying vec2 v_texCoords;\n" //
            + "\n" //
            + "void main()\n" //
            + "{\n" //
            + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
            + "   v_color.a = v_color.a * (255.0/254.0);\n" //
            + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
            + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
            + "}\n";
    
        private static String basicFragmentShader = "#ifdef GL_ES\n" //
            + "#define LOWP lowp\n" //
            + "precision mediump float;\n" //
            + "#else\n" //
            + "#define LOWP \n" //
            + "#endif\n" //
            + "varying LOWP vec4 v_color;\n" //
            + "varying vec2 v_texCoords;\n" //
            + "uniform sampler2D u_texture;\n" //
            + "uniform vec3 motion;\n" //
            + "void main()\n"//
            + "{\n" //
            + "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" //
            + "}";
        
        private static String blurFragmentShader = "#ifdef GL_ES\n" //
                + "#define LOWP lowp\n" //
                + "precision mediump float;\n" //
                + "#else\n" //
                + "#define LOWP \n" //
                + "#endif\n" //
                + "varying LOWP vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "uniform sampler2D u_texture;\n" //
                + "uniform vec3 u_motion;\n" //
                + "void main()\n"//
                + "{\n" //
                + "  vec2 offset = normalize(u_motion.xy) / 250.0;\n" //
                + "  vec4 color = texture2D(u_texture, v_texCoords) * 2.0;\n" //
                + "  color += texture2D(u_texture, v_texCoords + offset) * 1.0;\n" //
                + "  color += texture2D(u_texture, v_texCoords - offset) * 1.0;\n" //
                + "  gl_FragColor = color / 4.0 * v_color;\n" //
                + "  if (u_motion.x == 0.0 && u_motion.y == 0.0)\n" //
                + "    gl_FragColor = texture2D(u_texture, v_texCoords) * v_color;\n" //
                + "}";
        
        private static String SWFragmentShader = "#ifdef GL_ES\n" //
                + "#define LOWP lowp\n" //
                + "precision mediump float;\n" //
                + "#else\n" //
                + "#define LOWP \n" //
                + "#endif\n" //
                + "varying LOWP vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "uniform sampler2D u_texture;\n" //
                + "uniform vec3 u_motion;\n" //
                + "void main()\n"//
                + "{\n" //
                + "  vec4 color = texture2D(u_texture, v_texCoords);\n" //
                + "  float sum = (color.r + color.g + color.b) / 3.0;\n" //
                + "  gl_FragColor = vec4(sum, sum, sum, color.a);\n" //
                + "}";
        
        private static String pixelFragmentShader = "#ifdef GL_ES\n" //
                + "#define LOWP lowp\n" //
                + "precision mediump float;\n" //
                + "#else\n" //
                + "#define LOWP \n" //
                + "#endif\n" //
                + "varying LOWP vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "uniform sampler2D u_texture;\n" //
                + "uniform vec3 u_motion;\n" //
                + "void main()\n"//
                + "{\n" //
                + "  int x = int(v_texCoords.x * 100.0);\n" //
                + "  int y = int(v_texCoords.y * 100.0);\n" //
                + "  vec4 color = texture2D(u_texture, vec2(float(x)/100.0, float(y)/100.0));\n" //
                + "  gl_FragColor = color * v_color;\n" //
                + "}";
        
        
    public static final ShaderProgram BLUR_SHADER = new ShaderProgram(
            basicVertexShader, blurFragmentShader);
    
    public static final ShaderProgram BLACK_WITHE_SHADER = new ShaderProgram(
            basicVertexShader, SWFragmentShader);
    
    public static final ShaderProgram PIXEL_SHADER = new ShaderProgram(
            basicVertexShader, pixelFragmentShader);
}