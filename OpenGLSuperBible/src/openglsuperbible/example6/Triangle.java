package openglsuperbible.example6;

import openglsuperbible.example2.*;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import openglsuperbible.glutils.GLBatch;
import openglsuperbible.glutils.GLShader;
import openglsuperbible.glutils.GLShaderFactory;
import openglsuperbible.glutils.Math3D;
import openglsuperbible.glutils.SimpleGLBatch;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.PixelFormat;

/**
 *
 * @author andreban
 */
public class Triangle {
    public static final Logger LOGGER = Logger.getLogger(Triangle.class.getName());      
    public static final int DISPLAY_HEIGHT = 480;
    public static final int DISPLAY_WIDTH = 640;
     
    private GLBatch triangleBatch;
    private GLShader shader;    
    
    static {
        try {
            LOGGER.addHandler(new FileHandler("errors.log",true));
        } catch(IOException ex) {
            LOGGER.log(Level.WARNING,ex.toString(),ex);
        }
    }  
    
    public static void main(String[] args) {
        Triangle main = null;
        try {
          System.out.println("Keys:");
          System.out.println("down  - Shrink");
          System.out.println("up    - Grow");
          System.out.println("left  - Rotate left");
          System.out.println("right - Rotate right");
          System.out.println("esc   - Exit");
          main = new Triangle();
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
        triangleBatch = new SimpleGLBatch(GL11.GL_TRIANGLES,
                new float[]{ 0.0f, 0.5f, 0.0f, 1.0f, 
                            -0.5f, -0.5f, 0.0f, 1.0f, 
                             0.5f, -0.5f, 0.0f, 1.0f},
                new short[]{0, 1, 2});          
    } 
    
    public void resizeGL() {
        glViewport(0,0,Display.getWidth() ,Display.getHeight());
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

    float angle = 0.0f;
    public void render() {
        angle += 1f;
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        shader.useShader();        
        shader.setUniform4("vColor", 1.0f, 0.0f, 0.0f, 1.0f);
        
        float[] modelViewMatrix = new float[16];        
        float[] translationMatrix = new float[16];
        float[] rotationMatrix = new float[16];        
        
        Math3D.translationMatrix44f(translationMatrix, 0.0f, 0.0f, 0.0f);        
        Math3D.rotationMatrix44(rotationMatrix, (float)Math.toRadians(angle), 0.0f, 0.0f, 0.0f);
        
        Math3D.matrixMultiply44(modelViewMatrix, translationMatrix, rotationMatrix);
        
        FloatBuffer buff = BufferUtils.createFloatBuffer(16);
        buff.put(modelViewMatrix);
        buff.flip();
        shader.setUniformMatrix4("mvpMatrix", false, buff);
        triangleBatch.draw(shader.getAttributeLocations());
        Display.update();
    }    
}
