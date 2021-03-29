package domain;

public class Donat {

    public Donat(){
    }
    public int screen_width=40;
    public int screen_height= 40;

    final float theta_spacing = 0.07f;
    final float phi_spacing   = 0.02f;

    final float R1 = 1.0f;
    final float R2 = 2.0f;
    final float K2 = 5.0f;

    // Calculate K1 based on screen size: the maximum x-distance occurs
    // roughly at the edge of the torus, which is at x=R1+R2, z=0.  we
    // want that to be displaced 3/8ths of the width of the screen, which
    // is 3/4th of the way from the center to the side of the screen.
    // screen_width*3/8 = K1*(R1+R2)/(K2+0)
    // screen_width*K2*3/(8*(R1+R2)) = K1
    final float K1 = screen_width*K2*3/(8*(R1+R2));

    public char[][] output=new char[screen_width][screen_height];
    public float[][] zbuffer=new float[screen_width][screen_height];
    final char[] luminance={'.',',','-','~',':',';','=','!','*','#','$','@'};

    public void render_frame(float A, float B) {
        // precompute sines and cosines of A and B
        float cosA = (float) Math.cos(A), sinA = (float)Math.sin(A);
        float cosB = (float) Math.cos(B), sinB = (float) Math.sin(B);

        for(int j = 0; j < screen_height; j++) {
            for (int i = 0; i < screen_width; i++) {
                output[i][j] =' ';
            }
        }

        // theta goes around the cross-sectional circle of a torus
        for (float theta=0; theta < 2*Math.PI; theta += theta_spacing) {
            // precompute sines and cosines of theta
            float costheta = (float) Math.cos(theta), sintheta = (float) Math.sin(theta);

            // phi goes around the center of revolution of a torus
            for(float phi=0; phi < 2*Math.PI; phi += phi_spacing) {
                // precompute sines and cosines of phi
                float cosphi = (float) Math.cos(phi), sinphi = (float) Math.sin(phi);

                // the x,y coordinate of the circle, before revolving (factored
                // out of the above equations)
                float circlex = R2 + R1*costheta;
                float circley = R1*sintheta;

                // final 3D (x,y,z) coordinate after rotations, directly from
                // our math above
                float x = circlex*(cosB*cosphi + sinA*sinB*sinphi)
                        - circley*cosA*sinB;
                float y = circlex*(sinB*cosphi - sinA*cosB*sinphi)
                        + circley*cosA*cosB;
                float z = K2 + cosA*circlex*sinphi + circley*sinA;
                float ooz = 1/z;  // "one over z"

                // x and y projection.  note that y is negated here, because y
                // goes up in 3D space but down on 2D displays.
                int xp = (int) (screen_width/2 + K1*ooz*x);
                int yp = (int) (screen_height/2 - K1*ooz*y);

                // calculate luminance.  ugly, but correct.
                float L = cosphi*costheta*sinB - cosA*costheta*sinphi -
                        sinA*sintheta + cosB*(cosA*sintheta - costheta*sinA*sinphi);
                // L ranges from -sqrt(2) to +sqrt(2).  If it's < 0, the surface
                // is pointing away from us, so we won't bother trying to plot it.
                if (L > 0) {
                    // test against the z-buffer.  larger 1/z means the pixel is
                    // closer to the viewer than what's already plotted.
                    if(ooz > zbuffer[xp][yp]) {
                        zbuffer[xp][yp] = ooz;
                        int luminance_index = (int)(L*8);
                        // luminance_index is now in the range 0..11 (8*sqrt(2) = 11.3)
                        // now we lookup the character corresponding to the
                        // luminance and plot it in our output:
                        output[xp][ yp] = luminance[luminance_index];
                    }
                }
            }
        }
    }
}
