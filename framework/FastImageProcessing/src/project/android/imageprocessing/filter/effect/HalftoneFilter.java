package project.android.imageprocessing.filter.effect;

public class HalftoneFilter extends PixellateFilter {
	public HalftoneFilter(float fractionalWidth, float aspectRatio) {
		super(fractionalWidth, aspectRatio);
	}

	@Override
	protected String getFragmentShader() {
		return 
				"precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform float "+UNIFORM_FRACTIONAL_WIDTH+";\n"	
				+"uniform float "+UNIFORM_ASPECT_RATIO+";\n"
				+"const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\n"
				
		  		+"void main(){\n"
		  		+"   highp vec2 sampleDivisor = vec2("+UNIFORM_FRACTIONAL_WIDTH+", "+UNIFORM_FRACTIONAL_WIDTH+" /  "+UNIFORM_ASPECT_RATIO+");\n"
			    +"   highp vec2 samplePos = "+VARYING_TEXCOORD+" - mod("+VARYING_TEXCOORD+", sampleDivisor) + 0.5 * sampleDivisor;\n"
		  		+"   highp vec2 textureCoordinateToUse = vec2("+VARYING_TEXCOORD+".x, ("+VARYING_TEXCOORD+".y * "+UNIFORM_ASPECT_RATIO+" + 0.5 - 0.5 * "+UNIFORM_ASPECT_RATIO+"));\n"
		  		+"   highp vec2 adjustedSamplePos = vec2(samplePos.x, (samplePos.y * "+UNIFORM_ASPECT_RATIO+" + 0.5 - 0.5 * "+UNIFORM_ASPECT_RATIO+"));\n"
			    +"   highp float distanceFromSamplePoint = distance(adjustedSamplePos, textureCoordinateToUse);\n"
			    +"   lowp vec3 sampledColor = texture2D("+UNIFORM_TEXTURE0+", samplePos ).rgb;\n"
			    +"   highp float dotScaling = 1.0 - dot(sampledColor, W);\n"
			    +"   lowp float checkForPresenceWithinDot = 1.0 - step(distanceFromSamplePoint, ("+UNIFORM_FRACTIONAL_WIDTH+" * 0.5) * dotScaling);\n"
			    +"   gl_FragColor = vec4(vec3(checkForPresenceWithinDot), 1.0);\n"
		  		+"}\n";	
	}
}
