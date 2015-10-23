package processing.common;

public class RGB
{
   /// Max and Min pixel intensities
   public static int MAX_PIXEL = 255;
   public static int MIN_PIXEL = 0;

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
