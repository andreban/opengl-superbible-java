package openglsuperbible.glutils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Map;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

/**
 *
 * @author andreban
 */
public class SimpleGLBatch implements GLBatch {
    private int mode = GL11.GL_TRIANGLES;
    private int vertexBuffer = -1;
    private int colorBuffer = -1;
    private int normalBuffer = -1;
    private int textureBuffer = - 1;
    private int indexBuffer = -1;
    private int numElements = -1;
    
    /**
     * 
     * @param mode the gl mode of the batch. May be GL_TRIANGLE, GL_TRIANGLE_FAN,
     * GL_TRIGANGLE_STRIP, GL_LINE, GL_LINE_STRIP, GL_POINTS
     * @param vVertexData an array containing the vertex data.
     * @param vIndexData an array containing the index for the objects
     */
    public SimpleGLBatch(int mode, float[] vVertexData, short[] vIndexData) {
        this(mode, vVertexData, null, null, null, vIndexData);
    }
    
    /**
     * 
     * @param mode the gl mode of the batch. May be GL_TRIANGLES, GL_LINES, GL_POINTS
     * @param vVertexData vVertexData an array containing the vertex data.
     * @param vColorData an array containing the color data or null
     * @param vNormalData an array containing the normal data or null
     * @param vTextureData an array containing the texture data or null
     * @param vIndexData  the index to the array elements
     */
    public SimpleGLBatch(int mode, float[] vVertexData, float[] vColorData,
            float[] vNormalData, float[] vTextureData, short[] vIndexData) {
        this.mode = mode;
        numElements = vIndexData.length;
        FloatBuffer vertexData = BufferUtils.createFloatBuffer(vVertexData.length);
        vertexData.put(vVertexData);
        vertexData.flip();
        
        ShortBuffer indexData = BufferUtils.createShortBuffer(vIndexData.length);
        indexData.put(vIndexData);
        indexData.flip();
        
        vertexBuffer = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
 
        if (vColorData != null && vColorData.length > 0) {
            FloatBuffer colorData = BufferUtils.createFloatBuffer(vColorData.length);
            colorData.put(vColorData);
            colorData.flip();            
            colorBuffer = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorBuffer);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorData, GL15.GL_STATIC_DRAW);
        }
        
        if (vNormalData != null && vNormalData.length > 0) {
            FloatBuffer normalData = BufferUtils.createFloatBuffer(vNormalData.length);
            normalData.put(vNormalData);
            normalData.flip();            
            normalBuffer = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, normalBuffer);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normalData, GL15.GL_STATIC_DRAW);
        }
        
        if (vTextureData != null && vTextureData.length > 0) {
            FloatBuffer textureData = BufferUtils.createFloatBuffer(vTextureData.length);
            textureData.put(vTextureData);
            textureData.flip();
            textureBuffer = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textureBuffer);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textureData, GL15.GL_STATIC_DRAW);
        }
        
        indexBuffer = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW); 
                
    }
    
    @Override
    public void draw(Map<String, Integer> attributeLocations) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuffer);
        int inVertexLocation = attributeLocations.get("inVertex");
        GL20.glVertexAttribPointer(inVertexLocation, 4, GL11.GL_FLOAT, false, 4 * 4, 0);
        GL20.glEnableVertexAttribArray(inVertexLocation);
        checkerror();
        if (attributeLocations.containsKey("inNormal") && normalBuffer >= 0) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, normalBuffer);
            int normalLocation = attributeLocations.get("inNormal");
            GL20.glVertexAttribPointer(normalLocation, 3, GL11.GL_FLOAT, false, 3 * 4, 0);
            GL20.glEnableVertexAttribArray(normalLocation);
                    checkerror();
        }
        
        if (attributeLocations.containsKey("inColor") && colorBuffer >= 0) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorBuffer);
            int colorLocation = attributeLocations.get("inColor");
            GL20.glVertexAttribPointer(colorLocation, 4, GL11.GL_FLOAT, false, 4 * 4, 0);
            GL20.glEnableVertexAttribArray(colorLocation);
                    checkerror();
        }
        
        if (attributeLocations.containsKey("inTexCoord") && textureBuffer >= 0) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textureBuffer);
            int textureLocation = attributeLocations.get("inTexCoord");
            GL20.glVertexAttribPointer(textureLocation, 2, GL11.GL_FLOAT, false, 2 * 4, 0);
            GL20.glEnableVertexAttribArray(textureLocation);
                    checkerror();
        }

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);      
        
        //Draw triangle
        GL11.glDrawElements(mode, numElements, GL11.GL_UNSIGNED_SHORT, 0); 
        checkerror();
        
    }
    private void checkerror() {
        int error = GL11.glGetError();
        if (error != 0) {
            System.out.println("OpenGL error '" + error + "' occured!");
            throw new RuntimeException("OpenGL error '" + error + "' occured!");
        }                
    }
}
