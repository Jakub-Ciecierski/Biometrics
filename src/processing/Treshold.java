package processing;

import java.awt.image.BufferedImage;
import processing.common.RGB;

public class Treshold
{
   /// Assumes image in grayscale
   public static void compute(BufferedImage image, int treshold)
   {
      for (int x = 0; x < image.getWidth(); x++) {
           for (int y = 0; y < image.getHeight(); y++) {

               final int clr = image.getRGB(x, y);

               // Get RGB
               int R = RGB.getR(clr);
               int G = RGB.getG(clr);
               int B = RGB.getB(clr);

               int newR = applyTreshold(R, treshold);
               int newG = applyTreshold(G, treshold);
               int newB = applyTreshold(B, treshold);

               // Save
               int rgb = RGB.toRGB(newR, newG, newB);

               image.setRGB(x, y, rgb);
           }
      }
   }

   ///
   /// Applies treshold to a pixel
   /// For grayscale image it is any channel.
   /// For color images it should be the average of the channels.
   ///
   private static int applyTreshold(int pixelIntensity, int treshold)
   {
      int newIntes = RGB.MIN_PIXEL;

      if (pixelIntensity >= treshold)
           newIntes = RGB.MAX_PIXEL;
      else
           newIntes = RGB.MIN_PIXEL;

      return newIntes;
   }

}
