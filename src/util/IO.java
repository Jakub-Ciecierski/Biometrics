package util;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class IO
{
   /// Loads a file and returns a BufferedImage
   public static BufferedImage loadFile(String filename)
   {
      BufferedImage img = null;
      File file = new File(filename);

      try{
           img = ImageIO.read(file);
      } catch(Exception e) { e.printStackTrace(); }

      return img;
   }

//------------------------------------------------------------------------

   // Save File to the disk.
   public static void saveFile(BufferedImage bufferedImage, String name)
   {
      File outputfile = new File(name);

      try{
           ImageIO.write(bufferedImage, "jpg", outputfile);
      }catch(Exception e) {  e.printStackTrace(); }

   }

}
