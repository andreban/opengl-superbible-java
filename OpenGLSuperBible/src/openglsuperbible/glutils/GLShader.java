package openglsuperbible.glutils;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.GL20;

/**
 *
 * @author andreban
 */
public class GLShader {
    private Map<String, Integer> attributeLocations = new HashMap<String, Integer>();
    private Map<String, Integer> uniformLocations = new HashMap<String, Integer>();
    
    private int program = -1;
        
    public GLShader(String vertexShaderSource, String fragmentShaderSource) {
        //Vertex shader
        int vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertexShader, vertexShaderSource);
        GL20.glCompileShader(vertexShader);
 
        String vertexShaderErrorLog = GL20.glGetShaderInfoLog(vertexShader, 65536);
        if (vertexShaderErrorLog.length() != 0) {
            System.err.println("Vertex shader compile log: \n" + vertexShaderErrorLog);
        }
  
        //Fragment shader
        int fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragmentShader, fragmentShaderSource);
        GL20.glCompileShader(fragmentShader);
 
        String fragmentShaderErrorLog = GL20.glGetShaderInfoLog(fragmentShader, 65536);
        if (fragmentShaderErrorLog.length() != 0) {
            System.err.println("Fragment shader compile log: \n" + fragmentShaderErrorLog);
        }
 
        //Shader program
        program = GL20.glCreateProgram();
        GL20.glAttachShader(program, vertexShader);
        GL20.glAttachShader(program, fragmentShader);
            
        GL20.glLinkProgram(program);
        String log = GL20.glGetProgramInfoLog(program, 65536);
        if (log.length() != 0) {
            System.err.println("Program link log:\n" + log);
        }

        int numAttributes = GL20.glGetProgram(program, GL20.GL_ACTIVE_ATTRIBUTES);
        int maxAttributeLength = GL20.glGetProgram(program, GL20.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH);
        for (int i = 0; i < numAttributes; i++) {
            String name = GL20.glGetActiveAttrib(program, i, maxAttributeLength);
            int location = GL20.glGetAttribLocation(program, name);
            System.out.println(name + ":" + location);
            attributeLocations.put(name, location);
        }
        
        int numUniforms = GL20.glGetProgram(program, GL20.GL_ACTIVE_UNIFORMS);
        int maxUniformLength = GL20.glGetProgram(program, GL20.GL_ACTIVE_UNIFORM_MAX_LENGTH);
        for (int i = 0; i < numUniforms; i++) {
            String name = GL20.glGetActiveUniform(program, i, maxUniformLength);
            int location = GL20.glGetUniformLocation(program, name);
            uniformLocations.put(name, location);
            System.out.println(name + ":" + location);            
        }                      
    }
    
    public Map<String, Integer> getAttributeLocations() {
        return attributeLocations;
    }
    
    public Map<String, Integer> getUniformLocations() {
        return uniformLocations;
    }
    
    public void setUniformMatrix4(String uniformName, boolean traverse, FloatBuffer matrixdata) {
        int location = uniformLocations.get(uniformName);     
        GL20.glUniformMatrix4(location, traverse, matrixdata);        
    }
    
    public void setUniformMatrix3(String uniformName, boolean traverse, FloatBuffer matrixdata) {
        int location = uniformLocations.get(uniformName);     
        GL20.glUniformMatrix3(location, traverse, matrixdata);                
    }
    
    public void setUniform1i(String uniformName, int i) {
        int location = uniformLocations.get(uniformName);
        GL20.glUniform1i(location, i);
    }
    public void setUniform3(String uniformName, float v1, float v2, float v3) {
        int locaiton = uniformLocations.get(uniformName);
        GL20.glUniform3f(locaiton, v1, v2, v3);
    }
    
    public void setUniform4(String uniformName, float v1, float v2, float v3, float v4) {
        int location = uniformLocations.get(uniformName);
        GL20.glUniform4f(location, v1, v2, v3, v4);
    }
    public void useShader() {
        //Enable shader
        GL20.glUseProgram(program);        
    }
      
    public int getProgram() {
        return program;
    }
}
