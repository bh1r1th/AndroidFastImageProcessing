package project.android.imageprocessing.filter.blend;

import project.android.imageprocessing.filter.MultiInputFilter;

public class ColourBlendFilter extends MultiInputFilter {
	public ColourBlendFilter() {
		super(2);
	}
	
	@Override
	protected String getFragmentShader() {
		return 
				 "precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"uniform sampler2D "+UNIFORM_TEXTUREBASE+1+";\n" 
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				
				+"highp float lum(lowp vec3 c) {\n"
				+"  return dot(c, vec3(0.3, 0.59, 0.11));\n"
				+"}\n"
				 
				+"vec3 setlum(lowp vec3 c, highp float l2) {\n"
				+"  float d = l2 - lum(c);\n"
				+"  c = c + vec3(d);\n"
				+"  float l = lum(c);\n"
				+"  float n = min(min(c.r, c.g), c.b);\n"
				+"  float x = max(max(c.r, c.g), c.b);\n"
				+"  if (n < 0.0) {\n"
				+"    c.r = l + ((c.r - l) * l) / (l - n);\n"
				+"    c.g = l + ((c.g - l) * l) / (l - n);\n"
				+"    c.b = l + ((c.b - l) * l) / (l - n);\n"
				+"  }\n"
				+"  if (x > 1.0) {\n"
				+"    c.r = l + ((c.r - l) * (1.0 - l)) / (x - l);\n"
				+"    c.g = l + ((c.g - l) * (1.0 - l)) / (x - l);\n"
				+"    c.b = l + ((c.b - l) * (1.0 - l)) / (x - l);\n"
				+"  }\n"
				+"  return c;\n"
				+"}\n"
				
		  		+"void main(){\n"
		  		+"   vec4 color1 = texture2D("+UNIFORM_TEXTURE0+","+VARYING_TEXCOORD+");\n"
		  		+"   vec4 color2 = texture2D("+UNIFORM_TEXTUREBASE+1+","+VARYING_TEXCOORD+");\n"
		  		+"   gl_FragColor = vec4(color1.rgb * (1.0 - color2.a) + setlum(color2.rgb, lum(color1.rgb)) * color2.a, color1.a);\n"
		  		+"}\n";	
	}
}
