package processing;

import java.awt.image.BufferedImage;
import processing.common.RGB;

public class HistogramExpension
{
   ///
   /// histogram Expension - Each channel seperated
   ///
   public static void compute_all(BufferedImage image)
   {
      // 1 ) Find min, max

      int minR, maxR;
      int minG, maxG;
      int minB, maxB;

      int clr = image.getRGB(0, 0);

      minR = RGB.getR(clr);
      maxR = RGB.getR(clr);

      minG = RGB.getG(clr);
      maxG = RGB.getG(clr);

      minB = RGB.getB(clr);
      maxB = RGB.getB(clr);

      for (int x = 0; x < image.getWidth(); x++) {
           for (int y = 0; y < image.getHeight(); y++) {

               clr = image.getRGB(x, y);

               int curR = RGB.getR(clr);
               int curG = RGB.getG(clr);
               int curB = RGB.getB(clr);

               if (curR > maxR)
                   maxR = curR;
               if (curR < minR)
                   minR = curR;

               if (curG > maxG)
                   maxG = curG;
               if (curG < minG)
                   minG = curG;

               if (curB > maxB)
                   maxB = curB;
               if (curB < minB)
                   minB = curB;
           }
      }


      // 2) Apply normalization
      for (int x = 0; x < image.getWidth(); x++) {
           for (int y = 0; y < image.getHeight(); y++) {

               clr = image.getRGB(x, y);

               // Get RGB
               int R = RGB.getR(clr);
               int G = RGB.getG(clr);
               int B = RGB.getB(clr);

               double quationR = (255.0 / (double)(maxR - minR));
               double quationG = (255.0 / (double)(maxG - minG));
               double quationB = (255.0 / (double)(maxB - minB));

               // Convert
               int newR = (int) ((R - minR) * quationR);
               int newG = (int) ((G - minG) * quationG);
               int newB = (int) ((B - minB) * quationB);

               // Save
               int rgb = RGB.toRGB(newR, newG, newB);

               image.setRGB(x, y, rgb);
           }
      }
   }

//------------------------------------------------------------------------

   ///
   /// Histogram Expension - calculating the average of three channels
   /// to find min.
   ///
   /// It is applied to each channel individually
   public static void compute_avg(BufferedImage image)
   {
      // 1 ) Find min, max
      int min, max;

      int clr = image.getRGB(0, 0);

      int avg = (RGB.getR(clr) + RGB.getG(clr)  + RGB.getB(clr) ) / 3;

      min = avg;
      max = avg;

      for (int x = 0; x < image.getWidth(); x++) {
           for (int y = 0; y < image.getHeight(); y++) {

               clr = image.getRGB(x, y);

               avg = (RGB.getR(clr) + RGB.getG(clr)  + RGB.getB(clr) ) / 3;

               if (avg > max)
                   max = avg;
               if (avg < min)
                   min = avg;
           }
      }

      // 2) Apply normalization
      for (int x = 0; x < image.getWidth(); x++) {
           for (int y = 0; y < image.getHeight(); y++) {

               clr = image.getRGB(x, y);

               // Get RGB
               int R = RGB.getR(clr);
               int G = RGB.getG(clr);
               int B = RGB.getB(clr);

               // Convert
               double quation = (255.0 / (double)(max - min));

               int newR = (int) ((R - min) * quation);
               int newG = (int) ((G - min) * quation);
               int newB = (int) ((B - min) * quation);

               // Save
               int rgb = RGB.toRGB(newR, newG, newB);

               image.setRGB(x, y, rgb);
           }
      }
   }

}
