package openglsuperbible.example7;

import openglsuperbible.example6.*;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import openglsuperbible.glutils.GLBatch;
import openglsuperbible.glutils.GLBatchFactory;
import openglsuperbible.glutils.GLFrustrum;
import openglsuperbible.glutils.GLShader;
import openglsuperbible.glutils.GLShaderFactory;
import openglsuperbible.glutils.GeometryTransform;
import openglsuperbible.glutils.Math3D;
import openglsuperbible.glutils.MatrixStack;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.PixelFormat;

/**
 *
 * @author andreban
 */
public class Perspective {
    public static final Logger LOGGER = Logger.getLogger(Perspective.class.getName());      
    public static final int DISPLAY_HEIGHT = 480;
    public static final int DISPLAY_WIDTH = 640;
     
    private GLFrustrum frustrum;
    private GeometryTransform geometryTransform;
    private MatrixStack modelViewMatrix;
    private MatrixStack projectionMatrix;
    
    private GLBatch sideWall;
    private GLBatch topWall;
    
    private GLShader shader;    
    
    static {
        try {
            LOGGER.addHandler(new FileHandler("errors.log",true));
        } catch(IOException ex) {
            LOGGER.log(Level.WARNING,ex.toString(),ex);
        }
    }  
    
    public static void main(String[] args) {
        Perspective main = null;
        try {
          System.out.println("Keys:");
          System.out.println("down  - Shrink");
          System.out.println("up    - Grow");
          System.out.println("left  - Rotate left");
          System.out.println("right - Rotate right");
          System.out.println("esc   - Exit");
          main = new Perspective();
          main.create();
          main.run();
        }
        catch(Exception ex) {
          LOGGER.log(Level.SEVERE,ex.toString(),ex);
        }
        finally {
          if(main != null) {
            main.destroy();
          }
        }        
    }
    
    public void create() throws LWJGLException {
        //Display 
        Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH,DISPLAY_HEIGHT));
        Display.setFullscreen(false);
        Display.setTitle("Hello LWJGL World!");
        Display.setResizable(true);
        Display.create(new PixelFormat(), new ContextAttribs(3, 2).withProfileCompatibility(true));        

        //Keyboard
        Keyboard.create();

        //Mouse
        Mouse.setGrabbed(false);
        Mouse.create();
           
        //OpenGL
        initGL();
        resizeGL();
     }
    
    public void destroy() {
        //Methods already check if created before destroying.
        Mouse.destroy();
        Keyboard.destroy();
//        Display.destroy();
    }        
    
    public void initGL() {
        glClearColor(0.0f,0.0f,0.0f,0.0f);
                
        shader = GLShaderFactory.getFlatShader();       
        
        sideWall = GLBatchFactory.makeCube(0.2f, 0.8f, 1.0f);
        topWall = GLBatchFactory.makeCube(0.8f, 0.2f, 1.0f);      
        
        frustrum = new GLFrustrum();
        modelViewMatrix = new MatrixStack();
    } 
    
    public void resizeGL() {
        glViewport(0,0,Display.getWidth() ,Display.getHeight());
        frustrum.setPerspective(45f, Display.getWidth()/ Display.getHeight(), 1.0f, 10.0f);                
        projectionMatrix = new MatrixStack(frustrum.getProjectionMatrix());        
        geometryTransform = new GeometryTransform(modelViewMatrix, projectionMatrix);
    }    
  
    public void run() {
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            if (Display.wasResized()) resizeGL();            
            if (Display.isVisible()) {
                render();
            } else {
                if (Display.isDirty()) {
                    render();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
            }
//            Display.update();
            Display.sync(60);
        }
    }

    public void update() {
    }

    private FloatBuffer buff = BufferUtils.createFloatBuffer(16);    
    public void render() {       
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        shader.useShader();        

        modelViewMatrix.push();
        modelViewMatrix.translate(-0.5f, 0, -2);
        shader.setUniform4("vColor", 1.0f, 0.0f, 0.0f, 1.0f);                
        geometryTransform.fillModelViewProjectionBuffer(buff);        
        shader.setUniformMatrix4("mvpMatrix", false, buff);
        sideWall.draw(shader.getAttributeLocations());
        modelViewMatrix.pop();
        
        modelViewMatrix.push();
        modelViewMatrix.translate(+0.5f, 0, -2);
        shader.setUniform4("vColor", 0.0f, 1.0f, 0.0f, 1.0f);                
        geometryTransform.fillModelViewProjectionBuffer(buff);        
        shader.setUniformMatrix4("mvpMatrix", false, buff);
        sideWall.draw(shader.getAttributeLocations());
        modelViewMatrix.pop();
        
        modelViewMatrix.push();
        modelViewMatrix.translate(0, -0.3f, -2);
        shader.setUniform4("vColor", 1.0f, 0.0f, 1.0f, 1.0f);                
        geometryTransform.fillModelViewProjectionBuffer(buff);        
        shader.setUniformMatrix4("mvpMatrix", false, buff);
        topWall.draw(shader.getAttributeLocations());
        modelViewMatrix.pop();
        
        modelViewMatrix.push();
        modelViewMatrix.translate(0, 0.3f, -2);
        shader.setUniform4("vColor", 1.0f, 1.0f, 0.0f, 1.0f);                
        geometryTransform.fillModelViewProjectionBuffer(buff);        
        shader.setUniformMatrix4("mvpMatrix", false, buff);
        topWall.draw(shader.getAttributeLocations());
        modelViewMatrix.pop();           
                            
        Display.update();
    }    
}
