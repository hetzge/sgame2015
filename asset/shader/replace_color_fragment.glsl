#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

uniform vec4 replace_color;
uniform vec4 replace_with;

void main() {
        vec4 color = texture2D(u_texture, v_texCoords);
        
        if(color == replace_color){
        	color = replace_with;
        }

        gl_FragColor = color;
}