package processing.common;

/**
 * Created by jakub on 11/7/15.
 */
public class Pixel {
    private int color;

    private int R;
    private int G;
    private int B;

    public Pixel(int color){
        this.color = color;

        R = RGB.getR(color);
        G = RGB.getG(color);
        B = RGB.getB(color);
    }

    public Pixel(int R, int G, int B){
        this.R = R;
        this.G = G;
        this.B = B;

        color = RGB.toRGB(R, G, B);
    }

    public int getR(){
        return this.R;
    }

    public int getG(){
        return this.G;
    }

    public int getB(){
        return this.B;
    }

    public int getColor(){
        return this.color;
    }

    @Override
    public boolean equals(Object obj) {
        Pixel p = (Pixel)obj;

        if ((p.R == this.R) &&
            (p.G == this.G) &&
            (p.B == this.B))
            return true;

        return false;
    }
}
