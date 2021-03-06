package project.android.imageprocessing.filter.blend;

import project.android.imageprocessing.filter.MultiInputFilter;

/**
 * Applies a saturation blend of two images
 * @author Chris Batt
 */
public class SaturationBlendFilter extends MultiInputFilter {
	public SaturationBlendFilter() {
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
				 
				+"lowp vec3 setlum(lowp vec3 c, highp float l2) {\n"
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
				
				+"highp float sat(lowp vec3 c) {\n"
				+"     lowp float n = min(min(c.r, c.g), c.b);\n"
				+"     lowp float x = max(max(c.r, c.g), c.b);\n"
				+"     return x - n;\n"
				+"}\n"
				 
				+"lowp float mid(lowp float cmin, lowp float cmid, lowp float cmax, highp float s) {\n"
				+"     return ((cmid - cmin) * s) / (cmax - cmin);\n"
				+"}\n"
				 
				+"lowp vec3 setsat(lowp vec3 c, highp float s) {\n"
				+"     if (c.r > c.g) {\n"
				+"         if (c.r > c.b) {\n"
				+"             if (c.g > c.b) {\n"  /* g is mid, b is min */
				+"                 c.g = mid(c.b, c.g, c.r, s);\n"
				+"                 c.b = 0.0;\n"
				+"             } else {\n"  /* b is mid, g is min */
				+"                 c.b = mid(c.g, c.b, c.r, s);\n"
				+"                 c.g = 0.0;\n"
				+"             }\n"
				+"             c.r = s;\n"
				+"        } else {\n" /* b is max, r is mid, g is min */
				+"             c.r = mid(c.g, c.r, c.b, s);\n"
				+"             c.b = s;\n"
				+"             c.g = 0.0;\n"
				+"         }\n"
				+"     } else if (c.r > c.b) {\n" /* g is max, r is mid, b is min */
				+"         c.r = mid(c.b, c.r, c.g, s);\n"
				+"         c.g = s;\n"
				+"         c.b = 0.0;\n"
				+"     } else if (c.g > c.b) {\n" /* g is max, b is mid, r is min */
				+"         c.b = mid(c.r, c.b, c.g, s);\n"
				+"         c.g = s;\n"
				+"         c.r = 0.0;\n"
				+"     } else if (c.b > c.g) {\n" /* b is max, g is mid, r is min */
				+"         c.g = mid(c.r, c.g, c.b, s);\n"
				+"         c.b = s;\n"
				+"         c.r = 0.0;\n"
				+"     } else {\n"
				+"         c = vec3(0.0);\n"
				+"     }\n"
				+"     return c;\n"
				+"}\n"
				
		  		+"void main(){\n"
		  		+"   highp vec4 color1 = texture2D("+UNIFORM_TEXTURE0+","+VARYING_TEXCOORD+");\n"
		  		+"   highp vec4 color2 = texture2D("+UNIFORM_TEXTUREBASE+1+","+VARYING_TEXCOORD+");\n"
		  		+"   gl_FragColor = vec4(color1.rgb * (1.0 - color2.a) + setlum(setsat(color1.rgb, sat(color1.rgb)), lum(color2.rgb)) * color2.a, color1.a);\n"
		  		+"}\n";	
	}
}
