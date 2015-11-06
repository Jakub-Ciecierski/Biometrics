package processing.common;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RGB
{
   /// Max and Min pixel intensities
   public static int MAX_PIXEL = 255;
   public static int MIN_PIXEL = 0;

   public static BufferedImage copyImage(BufferedImage source){
      BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
      Graphics g = b.getGraphics();
      g.drawImage(source, 0, 0, null);
      g.dispose();
      return b;
   }

   public static int boundChannelValue(int in){
      if(in < MIN_PIXEL)
         in = MIN_PIXEL;
      if(in > MAX_PIXEL)
         in = MAX_PIXEL;

      return in;
   }

    public static int getR(int in) {
      return (int)((in << 8) >> 24) & 0xff;
   }

    public static int getG(int in) {
      return (int)((in << 16) >> 24) & 0xff;
   }

    public static int getB(int in) {
      return (int)((in << 24) >> 24) & 0xff;
   }

    public static int toRGB(int r,int g,int b) {
      return (int)((((r << 8)|g) << 8)|b);
   }

}
