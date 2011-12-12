package openglsuperbible.example5;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import openglsuperbible.glutils.GLBatch;
import openglsuperbible.glutils.GLBatchFactory;
import openglsuperbible.glutils.GLShader;
import openglsuperbible.glutils.GLShaderFactory;
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
public class Example5 {
    public static final Logger LOGGER = Logger.getLogger(Example5.class.getName());      
    public static final int DISPLAY_HEIGHT = 480;
    public static final int DISPLAY_WIDTH = 640;
     
    private GLBatch cubeBatch;
    private GLBatch sphereBatch;
    private GLBatch cylinderBatch;
    private GLBatch diskBatch;
    private GLBatch torusBatch;
    
    private GLShader shader;    
    
    static {
        try {
            LOGGER.addHandler(new FileHandler("errors.log",true));
        } catch(IOException ex) {
            LOGGER.log(Level.WARNING,ex.toString(),ex);
        }
    }  
    
    public static void main(String[] args) {
        Example5 main = null;
        try {
          System.out.println("Keys:");
          System.out.println("down  - Shrink");
          System.out.println("up    - Grow");
          System.out.println("left  - Rotate left");
          System.out.println("right - Rotate right");
          System.out.println("esc   - Exit");
          main = new Example5();
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
        cubeBatch = GLBatchFactory.makeCube(0.3f, 0.3f, 0.3f);
        sphereBatch = GLBatchFactory.makeSphere(0.15f, 20, 20);
        cylinderBatch = GLBatchFactory.makeCylinder(0.15f, 0.075f, 0.3f, 20, 10);        
        diskBatch = GLBatchFactory.makeDisk(0.075f, 0.25f, 20, 20);
        torusBatch = GLBatchFactory.makeTorus(0.25f, 0.075f, 36, 36);
    } 
    
    public void resizeGL() {
        glViewport(0,0,DISPLAY_WIDTH ,DISPLAY_HEIGHT);
    }    
  
    public void run() {
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
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
    private MatrixStack matrixStack = new MatrixStack();
    private FloatBuffer buff = BufferUtils.createFloatBuffer(16);    
    public void render() {
        angle += 1f;
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        shader.useShader();        
        
        matrixStack.push();
        shader.setUniform4("vColor", 1.0f, 0.0f, 0.0f, 1.0f);        
        matrixStack.translate(-0.5f, 0.5f, 0);
        matrixStack.rotate(angle, 1.0f, 1.0f, 1.0f);        
        matrixStack.fillBuffer(buff);
        shader.setUniformMatrix4("mvpMatrix", false, buff);
        cubeBatch.draw(shader.getAttributeLocations());
        matrixStack.pop();
        
        matrixStack.push();
        shader.setUniform4("vColor", 0.0f, 0.0f, 1.0f, 1.0f);        
        matrixStack.translate(0.5f, 0.5f, 0);
        matrixStack.rotate(angle, 1.0f, 1.0f, 1.0f);        
        matrixStack.fillBuffer(buff);
        shader.setUniformMatrix4("mvpMatrix", false, buff);
        sphereBatch.draw(shader.getAttributeLocations());
        matrixStack.pop();
        
        matrixStack.push();
        shader.setUniform4("vColor", 0.0f, 1.0f, 0.0f, 1.0f);        
        matrixStack.translate(0.0f, 0.0f, 0);
        matrixStack.rotate(angle, 1.0f, 1.0f, 1.0f);        
        matrixStack.fillBuffer(buff);
        shader.setUniformMatrix4("mvpMatrix", false, buff);
        cylinderBatch.draw(shader.getAttributeLocations());
        matrixStack.pop();     
        
        matrixStack.push();
        shader.setUniform4("vColor", 1.0f, 1.0f, 0.0f, 1.0f);        
        matrixStack.translate(-0.5f, -0.5f, 0);
        matrixStack.rotate(angle, 1.0f, 1.0f, 1.0f);        
        matrixStack.fillBuffer(buff);
        shader.setUniformMatrix4("mvpMatrix", false, buff);
        diskBatch.draw(shader.getAttributeLocations());
        matrixStack.pop();       
        
        matrixStack.push();
        shader.setUniform4("vColor", 0.0f, 1.0f, 1.0f, 1.0f);        
        matrixStack.translate(0.5f, -0.5f, 0);
        matrixStack.rotate(angle, 1.0f, 1.0f, 1.0f);        
        matrixStack.fillBuffer(buff);
        shader.setUniformMatrix4("mvpMatrix", false, buff);
        torusBatch.draw(shader.getAttributeLocations());
        matrixStack.pop();         
        
        Display.update();
    }    
}
