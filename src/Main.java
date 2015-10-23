import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import util.IO;
import util.SmartConsole;
import processing.*;

public class Main
{
   public static void main(String[] args)
   {
      if(args.length < 3) {
         usage();
         return;
      }

      // 0) Get commands
      double processToRun = Double.parseDouble(args[0]);
      String inFilename = args[1];
      String outFilename = args[2];


      // 1) Load image
      BufferedImage image = IO.loadFile(inFilename);

      // 2) Run Process
      process(processToRun, image, args);

      // 3) Save image
      IO.saveFile(image, outFilename);
    }

//------------------------------------------------------------------------

   public static void process(double test, BufferedImage image, String[] args)
   {
      // 2) Transform
      if (test == 1)
      {
         SmartConsole.Print("Running Grayscale");

         Grayscale.compute(image);
      }

      if (test == 2)
      {
         SmartConsole.Print("Running Inverse");

         Inverse.compute(image);
      }

      if (test == 3)
      {
         int brightness = Integer.parseInt(args[3]);

         SmartConsole.Print("Running Brightness: " + brightness);

         Brightness.compute(image, brightness);
      }

      if (test == 4)
      {
         int treshold = Integer.parseInt(args[3]);

         SmartConsole.Print("Running Treshold: " + treshold);

         Treshold.compute(image, treshold);
      }

      if (test == 5.1)
      {
         SmartConsole.Print("Running Histogram Expension ALL");

         HistogramExpension.compute_all(image);
      }
      if (test == 5.2)
      {
         SmartConsole.Print("Running Histogram Expension AVG");

         HistogramExpension.compute_avg(image);
      }
   }

//------------------------------------------------------------------------

   public static void usage()
   {
      SmartConsole.Print("java Main TestCase InputFilename OutputFileName");
   }

}
