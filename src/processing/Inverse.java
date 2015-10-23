package processing;

import java.awt.image.BufferedImage;
import processing.common.RGB;

public class Inverse
{

   //------------------------------------------------------------------------
   // 02.10.2015
   //------------------------------------------------------------------------
   public static void compute(BufferedImage image)
   {
      for (int x = 0; x < image.getWidth(); x++) {
           for (int y = 0; y < image.getHeight(); y++) {

               final int clr = image.getRGB(x, y);

               // Get RGB
               int R = RGB.getR(clr);
               int G = RGB.getG(clr);
               int B = RGB.getB(clr);

               // Convert
               int newR = RGB.MAX_PIXEL - R;
               int newG = RGB.MAX_PIXEL - G;
               int newB = RGB.MAX_PIXEL - B;

               // Save
               int rgb = RGB.toRGB(newR, newG, newB);

               image.setRGB(x, y, rgb);
           }
      }
   }
}
