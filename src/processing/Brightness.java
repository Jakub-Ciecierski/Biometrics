package processing;

import java.awt.image.BufferedImage;
import processing.common.RGB;

public class Brightness
{

   public static void compute(BufferedImage image, int brightness)
   {
      for (int x = 0; x < image.getWidth(); x++) {
           for (int y = 0; y < image.getHeight(); y++) {

               int clr = image.getRGB(x, y);

               // Get RGB
               int R = RGB.getR(clr);
               int G = RGB.getG(clr);
               int B = RGB.getB(clr);

               // Convert
               int newR = R + brightness;
               if (newR > RGB.MAX_PIXEL)
                   newR = RGB.MAX_PIXEL;

               int newG = G + brightness;
               if (newG > RGB.MAX_PIXEL)
                   newG = RGB.MAX_PIXEL;

               int newB = B + brightness;
               if (newB > RGB.MAX_PIXEL)
                   newB = RGB.MAX_PIXEL;

               // Save
               int rgb = RGB.toRGB(newR, newG, newB);

               image.setRGB(x, y, rgb);
           }
      }
   }

}
