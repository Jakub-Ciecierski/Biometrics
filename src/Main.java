import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Main
{

public static String LOG_PREFIX = " >> ";

//------------------------------------------------------------------------

    public static void main(String[] args)
    {
        if(args.length != 3) {
            usage();
            return;
        }

        int whichTest = Integer.parseInt(args[0]);

        String inFilename = args[1];
        String outFilename = args[2];

        System.out.println(LOG_PREFIX + "Starting...");

        // 1) Load image
        BufferedImage image = loadFile(inFilename);

        // 2) Transform
        if (whichTest == 1)
            toGrayscale(image);
        if (whichTest == 2)
            toInverse(image);

        // 3) Save image
        saveFile(image, outFilename);
    }

//------------------------------------------------------------------------

    public static void usage()
    {
        System.out.println(LOG_PREFIX + "java Main InputFilename OutputFileName");
    }

//------------------------------------------------------------------------
//------------------------------------------------------------------------

    /// Loads a file and returns a BufferedImage
    public static BufferedImage loadFile(String filename)
    {
        BufferedImage img = null;
        File file = new File(filename);

        System.out.println(LOG_PREFIX + "Openning file: " + filename);

        try{
            img = ImageIO.read(file);
        } catch(Exception e) { System.out.println("Exception"); }

        System.out.println(LOG_PREFIX + "Loaded Successfull");
        return img;
    }

    public static void saveFile(BufferedImage bufferedImage, String name)
    {
        System.out.println(LOG_PREFIX + "Saving file...");

        File outputfile = new File(name);

        try{
            ImageIO.write(bufferedImage, "jpg", outputfile);
        }catch(Exception e) { System.out.println("Exception"); }

        System.out.println(LOG_PREFIX + "Saved Successfull: " + name);
    }

//------------------------------------------------------------------------
//------------------------------------------------------------------------

    public static void toInverse(BufferedImage image)
    {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                final int clr = image.getRGB(x, y);

                // Get RGB
                int R = getR(clr);
                int G = getG(clr);
                int B = getB(clr);

                // Convert
                int newR = 255 - R;
                int newG = 255 - G;
                int newB = 255 - B;

                // Save
                int rgb = toRGB(newR, newG, newB);

                image.setRGB(x, y, rgb);
            }
        }
    }

//------------------------------------------------------------------------
//------------------------------------------------------------------------

    public static void toGrayscale(BufferedImage image) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                final int clr = image.getRGB(x, y);

                // Get RGB
                int R = getR(clr);
                int G = getG(clr);
                int B = getB(clr);

                // Convert
                int newR = convertGrayscale_std(R, G, B);
                int newG = convertGrayscale_std(R, G, B);
                int newB = convertGrayscale_std(R, G, B);

                // Save
                int rgb = toRGB(newR, newG, newB);

                image.setRGB(x, y, rgb);
            }
        }
    }

//------------------------------------------------------------------------

    private static int getR(int in) {
        return (int)((in << 8) >> 24) & 0xff;
    }

    private static int getG(int in) {
        return (int)((in << 16) >> 24) & 0xff;
    }

    private static int getB(int in) {
        return (int)((in << 24) >> 24) & 0xff;
    }

    private static int toRGB(int r,int g,int b) {
        return (int)((((r << 8)|g) << 8)|b);
    }

//------------------------------------------------------------------------

    /// Converts RGB to grayscale by standard method
    private static int convertGrayscale_std(int R, int G, int B)
    {
        int grayscale = (R + G + B) / 3;

        return grayscale;
    }

//------------------------------------------------------------------------
//------------------------------------------------------------------------

}
