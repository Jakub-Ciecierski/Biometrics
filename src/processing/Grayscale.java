package processing;

import java.awt.image.BufferedImage;
import processing.common.RGB;

public class Grayscale
{
   //------------------------------------------------------------------------
   // 02.10.2015
   //------------------------------------------------------------------------
   public static void compute(BufferedImage image)
   {

      for (int x = 0; x < image.getWidth(); x++)
      {
           for (int y = 0; y < image.getHeight(); y++)
           {

               final int clr = image.getRGB(x, y);

               // Get RGB
               int R = RGB.getR(clr);
               int G = RGB.getG(clr);
               int B = RGB.getB(clr);

               // Convert
               int newR = convertGrayscale_std(R, G, B);
               int newG = convertGrayscale_std(R, G, B);
               int newB = convertGrayscale_std(R, G, B);

               // Save
               int rgb = RGB.toRGB(newR, newG, newB);

               image.setRGB(x, y, rgb);
           }
      }
   }

   /// Converts RGB to grayscale by standard method
   private static int convertGrayscale_std(int R, int G, int B)
   {
      int grayscale = (R + G + B) / 3;

      return grayscale;
   }
}
