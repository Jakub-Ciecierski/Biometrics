package processing;

import java.awt.image.BufferedImage;
import processing.common.RGB;

/*

*/
public class KMM
{

    private static Pixel Pixel1 = new Pixel(255, 255, 255);
    private static Pixel Pixel2 = new Pixel(0, 255, 0);
    private static Pixel Pixel3 = new Pixel(0, 0, 255);
    private static Pixel Pixel4 = new Pixel(255, 0, 0);

   //------------------------------------------------------------------------
   // 02.10.2015
   //------------------------------------------------------------------------
   public static void compute(BufferedImage image)
   {
       // Filter step 0
       // Implicit step 1, since all black pixel are '1s'
       filterStep(image);

       selectStickingToBackground(image);
   }

   // Step 0, Filters - Normalization and Binarization.
   private static void filterStep(BufferedImage image){
        // Apply Normalization
        //HistogramExpension.compute_all(image);

        // Apply Binarization
        int t = 100;
        //Grayscale.compute(image);
        Treshold.compute(image, t);
   }

   // Step 2
   // a) All '1s' sticking to background become '2'
   // b) All '1' sticking to backgroubnd by corner are changed to '3s'
   private static void selectStickingToBackground(BufferedImage image){
       // For each pixel call
       for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                // get neighbours
                int clr = image.getRGB(x, y);

                Pixel newPixel = changePixelBasedOnBackground(image, x, y);


            }
        }
   }

   // Used in step 2
   //
   // Look at table of step 2
   // _____________
   // | 3 | 2 | 3 |
   // | 2 | x | 2 |
   // | 3 | 2 | 3 |
   // |___________|
   //
   // _____________________________
   // |(-1,+1) | (0,+1) | (+1,+1) |
   // |(-1, 0) | (0, 0) | (+1, 0) |
   // |(-1,-1) | (0,-1) | (+1,-1) |
   // |___________________________|
   //
   // Thus all pixels with relative coordinates corresponding to 2 in LaT are
   // changed into Pixel2.
   // All pixels ... to 3 are changed into Pixel3.
   //
   private static Pixel changePixelBasedOnBackground(image, int x, int y){
       int i, int j;
       i = (x-1) < 0 ? 0 : x-1;
       j = (y-1) < 0 ? 0 : y-1;
       p0 = imageRead.getRGB(i, j);

       i = x;
       j = (y-1) < 0 ? 0 : y-1;
       p1 = imageRead.getRGB(i, j);

       i = (x+1) >= width ? width-1 : x+1;
       j = (y-1) < 0 ? 0 : y-1;
       p2 = imageRead.getRGB(i, j);

       i = (x+1) >= width ? width - 1 : x+1;
       j = (y+1) >= height ? height - 1 : y+1;
       p4 = imageRead.getRGB(i, j);

       i = x;
       j = (y+1) >= height ? height - 1 : y+1;
       p5 = imageRead.getRGB(i, j);


       i = (x-1) < 0 ? 0 : x-1;
       j = (y+1) >= height ? height - 1 : y+1;
       p6 = imageRead.getRGB(i, j);

       i = (x+1) >= width ? width-1 : x+1;
       j = y;
       p3 = imageRead.getRGB(i, j);

       i = (x-1) < 0 ? 0 : x-1;
       j = y;
       p7 = imageRead.getRGB(i, j);

       return new Pixel();
   }
}
