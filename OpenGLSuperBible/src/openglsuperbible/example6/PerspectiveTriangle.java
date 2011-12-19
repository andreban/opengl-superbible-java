package openglsuperbible.example6;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import openglsuperbible.glutils.GLBatch;
import openglsuperbible.glutils.GLFrustrum;
import openglsuperbible.glutils.GLShader;
import openglsuperbible.glutils.GLShaderFactory;
import openglsuperbible.glutils.Math3D;
import openglsuperbible.glutils.MatrixStack;
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
public class PerspectiveTriangle {
    public static final Logger LOGGER = Logger.getLogger(PerspectiveTriangle.class.getName());      
    public static final int DISPLAY_HEIGHT = 480;
    public static final int DISPLAY_WIDTH = 640;
     
    private GLFrustrum frustrum;
    private MatrixStack modelViewMatrix;
    private MatrixStack projectionMatrix;
    
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
        PerspectiveTriangle main = null;
        try {
          System.out.println("Keys:");
          System.out.println("down  - Shrink");
          System.out.println("up    - Grow");
          System.out.println("left  - Rotate left");
          System.out.println("right - Rotate right");
          System.out.println("esc   - Exit");
          main = new PerspectiveTriangle();
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
        
        frustrum = new GLFrustrum();
        modelViewMatrix = new MatrixStack();
    } 
    
    public void resizeGL() {
        glViewport(0,0,Display.getWidth() ,Display.getHeight());
        frustrum.setPerspective(45f, Display.getWidth()/ Display.getHeight(), 1.0f, 10.0f);                
        projectionMatrix = new MatrixStack(frustrum.getProjectionMatrix());        
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
    private float[] mvpMatrix = new float[16];    
    private FloatBuffer buff = BufferUtils.createFloatBuffer(16);    
    public void render() {
        angle += 1f;
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        shader.useShader();        

        modelViewMatrix.push();
        shader.setUniform4("vColor", 1.0f, 0.0f, 0.0f, 1.0f);        
        modelViewMatrix.translate(0f, 0f, -2f);
        Math3D.matrixMultiply44(mvpMatrix, projectionMatrix.getMatrix(), modelViewMatrix.getMatrix());
        buff.position(0);
        buff.put(mvpMatrix);
        buff.flip();
        shader.setUniformMatrix4("mvpMatrix", false, buff);
        triangleBatch.draw(shader.getAttributeLocations());
        modelViewMatrix.pop();              
        
        Display.update();
    }    
}
