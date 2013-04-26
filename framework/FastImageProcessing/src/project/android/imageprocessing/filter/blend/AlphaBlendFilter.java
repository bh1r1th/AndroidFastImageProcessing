package project.android.imageprocessing.filter.blend;

import android.opengl.GLES20;
import project.android.imageprocessing.filter.MultiInputFilter;

public class AlphaBlendFilter extends MultiInputFilter {
	private static final String UNIFORM_MIX_PERCENT = "u_MixPercent";
	
	private float mixPercent;
	private int mixPercentHandle;

	public AlphaBlendFilter(float mixPercent) {
		super(2);
		this.mixPercent = mixPercent;
	}
	
	@Override
	protected void initShaderHandles() {
		super.initShaderHandles();
		mixPercentHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_MIX_PERCENT);
	}
	
	@Override
	protected void passShaderValues() {
		super.passShaderValues();
		GLES20.glUniform1f(mixPercentHandle, mixPercent);
	} 
	
	@Override
	protected String getFragmentShader() {
		return 
				 "precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"uniform sampler2D "+UNIFORM_TEXTUREBASE+1+";\n" 
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform float "+UNIFORM_MIX_PERCENT+";\n"
				
		  		+"void main(){\n"
				+"   vec4 tex1 = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+");\n"
				+"   vec4 tex2 = texture2D("+UNIFORM_TEXTUREBASE+1+", "+VARYING_TEXCOORD+");\n"
		  		+"   gl_FragColor = vec4(mix(tex1.rgb, tex2.rgb, tex2.a * "+UNIFORM_MIX_PERCENT+"), tex1.a);\n"
		  		+"}\n";
	}

}
