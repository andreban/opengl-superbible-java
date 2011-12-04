package openglsuperbible.glutils;

/**
 *
 * @author andreban
 */
public class GLShaderFactory {
    ///////////////////////////////////////////////////////////////////////////////
    // Identity Shader (GLT_SHADER_IDENTITY)
    // This shader does no transformations at all, and uses the current
    // glColor value for fragments.
    // It will shade between verticies.    
    private static final String IDENTITY_VERTEX_PROGRAM = 
            "attribute vec4 inVertex;\n"
            + "void main(void) { \n"
            + "    gl_Position = inVertex; \n"
            + "}";            
    private static final String IDENTITY_FRAGMENT_PROGRAM = 
            "uniform vec4 vColor;"
            + "void main(void) { "
            + "    gl_FragColor = vColor;"
            + "}";
	
    ///////////////////////////////////////////////////////////////////////////////
    // Flat Shader (GLT_SHADER_FLAT)
    // This shader applies the given model view matrix to the verticies, 
    // and uses a uniform color value.    
    private static final String FLAT_VERTEX_PROGRAM = 
            "uniform mat4 mvpMatrix;"
            + "attribute vec4 inVertex;"
            + "void main(void) { "
            + "    gl_Position = mvpMatrix * inVertex; "
            + "}";
    
    private static final String FLAT_FRAGMENT_PROGRAM = 
            "uniform vec4 vColor;"
            + "void main(void) {"
            + "    gl_FragColor = vColor; "
            + "}";            

    /////////////////////////////////////////////////////////////////////////////////
    //// GLT_SHADER_SHADED
    //// Point light, diffuse lighting only    
    private static final String SHADED_VERTEX_PROGRAM = 
            "uniform mat4 mvpMatrix;"
            + "attribute vec4 vColor;"
            + "attribute vec4 vVertex;"
            + "varying vec4 vFragColor;"
            + "void main(void) {"
            + "vFragColor = vColor; "
            + " gl_Position = mvpMatrix * vVertex; "
            + "}";

    private static final String SHADED_FRAGMENT_PROGRAM =
            "varying vec4 vFragColor; "
            + "void main(void) { "
            + " gl_FragColor = vFragColor; "
            + "}";
    
    									
    //// GLT_SHADER_DEFAULT_LIGHT
    //// Simple diffuse, directional, and vertex based light
    private static final String DEFAULT_LIGHT_VERTEX_PROGRAM =
            "uniform mat4 mvMatrix;"
            + "uniform mat4 pMatrix;"
            + "varying vec4 vFragColor;"
            + "attribute vec4 vVertex;"
            + "attribute vec3 vNormal;"
            + "uniform vec4 vColor;"
            + "void main(void) { "
            + "    mat3 mNormalMatrix;"
            + "    mNormalMatrix[0] = mvMatrix[0].xyz;"
            + "    mNormalMatrix[1] = mvMatrix[1].xyz;"
            + "    mNormalMatrix[2] = mvMatrix[2].xyz;"
            + "    vec3 vNorm = normalize(mNormalMatrix * vNormal);"
            + "    vec3 vLightDir = vec3(0.0, 0.0, 1.0); "
            + "    float fDot = max(0.0, dot(vNorm, vLightDir)); "
            + "    vFragColor.rgb = vColor.rgb * fDot;"
            + "    vFragColor.a = vColor.a;"
            + "    mat4 mvpMatrix;"
            + "    mvpMatrix = pMatrix * mvMatrix;"
            + "    gl_Position = mvpMatrix * vVertex; "
            + "}";

    private static final String DEFAULT_LIGHT_FRAGMENT_PROGRAM =
            "varying vec4 vFragColor; "
            + "void main(void) { "
            + " gl_FragColor = vFragColor; "
            + "}";
    

    ////GLT_SHADER_POINT_LIGHT_DIFF
    //// Point light, diffuse lighting only
    private static final String POINT_LIGHT_DIFF_VERTEX_PROGRAM = 
            "uniform mat4 mvMatrix;"
            + "uniform mat4 pMatrix;"
            + "uniform vec3 vLightPos;"
            + "uniform vec4 inColor;"
            + "attribute vec4 inVertex;"
            + "attribute vec3 inNormal;"
            + "varying vec4 vFragColor;"
            + "void main(void) { "
            + "   mat3 mNormalMatrix;"
            + "   mNormalMatrix[0] = normalize(mvMatrix[0].xyz);"
            + "   mNormalMatrix[1] = normalize(mvMatrix[1].xyz);"
            + "   mNormalMatrix[2] = normalize(mvMatrix[2].xyz);"
            + "   vec3 vNorm = normalize(mNormalMatrix * inNormal);"
            + "   vec4 ecPosition;"
            + "   vec3 ecPosition3;"
            + "   ecPosition = mvMatrix * inVertex;"
            + "   ecPosition3 = ecPosition.xyz /ecPosition.w;"
            + "   vec3 vLightDir = normalize(vLightPos - ecPosition3);"
            + "   float fDot = max(0.0, dot(vNorm, vLightDir)); "
            + "   vFragColor.rgb = inColor.rgb * fDot;"
            + "   vFragColor.a = inColor.a;"
            + "   mat4 mvpMatrix;"
            + "   mvpMatrix = pMatrix * mvMatrix;"
            + "   gl_Position = mvpMatrix * inVertex; "
            + "}";

    private static final String POINT_LIGHT_DIFF_FRAGMENT_PROGRAM =
            "varying vec4 vFragColor; "
            + "void main(void) { "
            + " gl_FragColor = vFragColor; "
            + "}";

    ////GLT_SHADER_TEXTURE_REPLACE
    //// Just put the texture on the polygons
    private static final String TEXTURE_REPLACE_VERTEX_PROGRAM =
            "uniform mat4 mvpMatrix;"
            + "attribute vec4 inVertex;"
            + "attribute vec2 inTexCoord0;"
            + "varying vec2 vTex;"
            + "void main(void) "
            + "{ vTex = vTexCoord0;"
            + " gl_Position = mvpMatrix * vVertex; "
            + "}";
    
    private static final String TEXTURE_REPLACE_FRAGMENT_PROGRAM =
            "varying vec2 vTex;"
            + "uniform sampler2D textureUnit0;"
            + "void main(void) "
            + "{ gl_FragColor = texture2D(textureUnit0, vTex); "
            + "}";

    //// Just put the texture on the polygons
    private static final String TEXTURE_RECT_REPLACE_VERTEX_PROGRAM = 
            "uniform mat4 mvpMatrix;"
            + "attribute vec4 vVertex;"
            + "attribute vec2 vTexCoord0;"
            + "varying vec2 vTex;"
            + "void main(void) "
            + "{ vTex = vTexCoord0;"
            + " gl_Position = mvpMatrix * vVertex; "
            + "}";

    private static final String TEXTURE_RECT_REPLACE_FRAGMENT_PROGRAM =
            "varying vec2 vTex;"
            + "uniform sampler2DRect textureUnit0;"
            + "void main(void) "
            + "{ gl_FragColor = texture2DRect(textureUnit0, vTex); "
            + "}";

    
    ////GLT_SHADER_TEXTURE_MODULATE
    //// Just put the texture on the polygons, but multiply by the color (as a unifomr)
    private static final String TEXTURE_MODULATE_VERTEX_PROGRAM = 
            "uniform mat4 mvpMatrix;"
            + "attribute vec4 vVertex;"
            + "attribute vec2 vTexCoord0;"
            + "varying vec2 vTex;"
            + "void main(void) "
            + "{ vTex = vTexCoord0;"
            + " gl_Position = mvpMatrix * vVertex; "
            + "}";

    private static final String TEXTURE_MODULATE_FRAGMENT_PROGRAM =
            "varying vec2 vTex;"
            + "uniform sampler2D textureUnit0;"
            + "uniform vec4 vColor;"
            + "void main(void) "
            + "{ gl_FragColor = vColor * texture2D(textureUnit0, vTex); "
            + "}";

    //GLT_SHADER_TEXTURE_POINT_LIGHT_DIFF
    // Point light (Diffuse only), with texture (modulated)
    private static final String TEXTURE_POINT_LIGHT_DIFF_VERTEX_PROGRAM =
            "uniform mat4 mvMatrix;"
            + "uniform mat4 pMatrix;"
            + "uniform vec3 vLightPos;"
            + "uniform vec4 vColor;"
            + "attribute vec4 vVertex;"
            + "attribute vec3 vNormal;"
            + "varying vec4 vFragColor;"
            + "attribute vec2 vTexCoord0;"
            + "varying vec2 vTex;"
            + "void main(void) { "
            + " mat3 mNormalMatrix;"
            + " mNormalMatrix[0] = normalize(mvMatrix[0].xyz);"
            + " mNormalMatrix[1] = normalize(mvMatrix[1].xyz);"
            + " mNormalMatrix[2] = normalize(mvMatrix[2].xyz);"
            + " vec3 vNorm = normalize(mNormalMatrix * vNormal);"
            + " vec4 ecPosition;"
            + " vec3 ecPosition3;"
            + " ecPosition = mvMatrix * vVertex;"
            + " ecPosition3 = ecPosition.xyz /ecPosition.w;"
            + " vec3 vLightDir = normalize(vLightPos - ecPosition3);"
            + " float fDot = max(0.0, dot(vNorm, vLightDir)); "
            + " vFragColor.rgb = vColor.rgb * fDot;"
            + " vFragColor.a = vColor.a;"
            + " vTex = vTexCoord0;"
            + " mat4 mvpMatrix;"
            + " mvpMatrix = pMatrix * mvMatrix;"
            + " gl_Position = mvpMatrix * vVertex; "
            + "}";
    private static final String TEXTURE_POINT_LIGHT_DIFF_FRAGMENT_PROGRAM =
            "varying vec4 vFragColor;"
            + "varying vec2 vTex;"
            + "uniform sampler2D textureUnit0;"
            + "void main(void) { "
            + " gl_FragColor = vFragColor * texture2D(textureUnit0, vTex);"
            + "}";
    
    public static GLShader getIdentityShader() {
        return new GLShader(IDENTITY_VERTEX_PROGRAM, IDENTITY_FRAGMENT_PROGRAM);
    }
    
    public static GLShader getFlatShader() {
        return new GLShader(FLAT_VERTEX_PROGRAM, FLAT_FRAGMENT_PROGRAM);
    }
    
    public static GLShader getShadedShader() {
        return new GLShader(SHADED_VERTEX_PROGRAM, SHADED_FRAGMENT_PROGRAM);
    }
    
    public static GLShader getDefaultLightShader() {
        return new GLShader(DEFAULT_LIGHT_VERTEX_PROGRAM, DEFAULT_LIGHT_FRAGMENT_PROGRAM);
    }
    
    public static GLShader getPointLightDiffuseShader() {
        return new GLShader(POINT_LIGHT_DIFF_VERTEX_PROGRAM, POINT_LIGHT_DIFF_FRAGMENT_PROGRAM);
    }
    
    public static GLShader getTextureReplaceShader() {
        return new GLShader(TEXTURE_REPLACE_VERTEX_PROGRAM, TEXTURE_REPLACE_FRAGMENT_PROGRAM);
    }
    
    public static GLShader getTextureRectReplaceShader() {
        return new GLShader(TEXTURE_RECT_REPLACE_VERTEX_PROGRAM, TEXTURE_RECT_REPLACE_FRAGMENT_PROGRAM);
    }
    
    public static GLShader getTextureModulateShader() {
        return new GLShader(TEXTURE_MODULATE_VERTEX_PROGRAM, TEXTURE_MODULATE_FRAGMENT_PROGRAM);
    }
    
    public static GLShader getTexturePointLightDiffuseShader() {
        return new GLShader(TEXTURE_POINT_LIGHT_DIFF_VERTEX_PROGRAM, TEXTURE_POINT_LIGHT_DIFF_FRAGMENT_PROGRAM);
    }
}
