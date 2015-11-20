import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main {
    //metody pomocnicze
    private static int getR(int in) {
        return (int)((in << 8) >> 24) & 0xff;
    }
    private static int getG(int in) {
        return (int)((in << 16) >> 24) & 0xff;
    }
    private static int getB(int in) {
        return (int)((in << 24) >> 24) & 0xff;
    }
    private static int toRGB(int r,int g,int b) {
        return (int)((((r << 8)|g) << 8)|b);
    }
    public static void main(String[] args) {
        try {
            File file = new File("face.jpg");
            BufferedImage in=ImageIO.read(file);

            //tworzymy obraz wyjœciowy o identycznych parametrach jak wejœciowy
            BufferedImage out=new BufferedImage(in.getWidth(),in.getHeight(),BufferedImage.TYPE_INT_RGB);

            //pêtla przechodz¹ca po ka¿dym pikselu
            for (int i=0;i<in.getWidth();i++) {
                for (int j=0;j<in.getHeight();j++) {
                    int x=in.getRGB(i, j); //x oznacza wartoœæ piksela obrazu wejœciowego
                    out.setRGB(i,j,255-x); //ustawiamy wartoœæ piksela obrazu wyjœciowego
                }
            }

            //zapis obrazu wyjœciowego do pliku
            ImageIO.write(out, "jpeg", new File("out.jpg"));
        }
        catch(IOException e) { }
    }

}
