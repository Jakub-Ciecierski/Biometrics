import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Main
{

public static String LOG_PREFIX = " >> ";

/// Max and Min pixel intensities
public static int MAX_PIXEL = 255;
public static int MIN_PIXEL = 0;

//------------------------------------------------------------------------

    public static void main(String[] args)
    {
        if(args.length < 3) {
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
        if (whichTest == 3) {
            int treshold = Integer.parseInt(args[3]);
            tresholding(image, treshold);
        }
        if (whichTest == 4) {
            System.out.println(LOG_PREFIX + "Starting Histogram Expension");

            histogramExpension2(image);
        }
        if(whichTest == 5) {
            int brightness = Integer.parseInt(args[3]);
            applyBrightness(image, brightness);
        }

        // 3) Save image
        saveFile(image, outFilename);
    }

//------------------------------------------------------------------------

    public static void usage()
    {
        System.out.println(LOG_PREFIX + "java Main TestCase InputFilename OutputFileName");
        System.out.println(LOG_PREFIX + "TestCase 1 - GrayScale");
        System.out.println(LOG_PREFIX + "TestCase 2 - Inverse");
        System.out.println(LOG_PREFIX + "TestCase 3 - Tresholding");
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
// 16.10.2015
//------------------------------------------------------------------------

    ///
    /// Applies tresholding
    ///
    public static void tresholding(BufferedImage image, int treshold)
    {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                final int clr = image.getRGB(x, y);

                // Get RGB
                int R = getR(clr);
                int G = getG(clr);
                int B = getB(clr);

                int newR = applyTreshold(R, treshold);
                int newG = applyTreshold(G, treshold);
                int newB = applyTreshold(B, treshold);

                // Save
                int rgb = toRGB(newR, newG, newB);

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
        int newIntes = MIN_PIXEL;

        if (pixelIntensity < treshold)
            newIntes = MAX_PIXEL;
        else
            newIntes = MIN_PIXEL;

        return newIntes;
    }

//------------------------------------------------------------------------
//------------------------------------------------------------------------

    ///
    /// histogram Expension - Each channel seperated
    ///
    public static void histogramExpension(BufferedImage image)
    {
        // 1 ) Find min, max

        int minR, maxR;
        int minG, maxG;
        int minB, maxB;

        int clr = image.getRGB(0, 0);

        minR = getR(clr);
        maxR = getR(clr);

        minG = getG(clr);
        maxG = getG(clr);

        minB = getB(clr);
        maxB = getB(clr);

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                clr = image.getRGB(x, y);

                int curR = getR(clr);
                int curG = getG(clr);
                int curB = getB(clr);

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

        System.out.println(LOG_PREFIX + "Max Red: " + maxR);
        System.out.println(LOG_PREFIX + "Min Red: " + minR);
        System.out.println();
        System.out.println(LOG_PREFIX + "Max Green: " + maxG);
        System.out.println(LOG_PREFIX + "Min Green: " + minG);
        System.out.println();
        System.out.println(LOG_PREFIX + "Max Blue: " + maxB);
        System.out.println(LOG_PREFIX + "Min Blue: " + minB);

        // 2) Apply normalization
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                clr = image.getRGB(x, y);

                // Get RGB
                int R = getR(clr);
                int G = getG(clr);
                int B = getB(clr);

                // Convert
                int newR = (R - minR) * (MAX_PIXEL / (maxR - minR));
                int newG = (G - minG) * (MAX_PIXEL / (maxG - minG));
                int newB = (B - minB) * (MAX_PIXEL / (maxB - minB));

                // Save
                int rgb = toRGB(newR, newG, newB);

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
    public static void histogramExpension2(BufferedImage image)
    {
        // 1 ) Find min, max

        int min, max;

        int clr = image.getRGB(0, 0);

        int avg = (getR(clr) + getG(clr)  + getB(clr) ) / 3;

        min = avg;
        max = avg;

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                clr = image.getRGB(x, y);

                avg = (getR(clr) + getG(clr)  + getB(clr) ) / 3;

                if (avg > max)
                    max = avg;
                if (avg < min)
                    min = avg;
            }
        }

        System.out.println(LOG_PREFIX + "Max: " + max);
        System.out.println(LOG_PREFIX + "Min: " + min);

        // 2) Apply normalization
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                clr = image.getRGB(x, y);

                // Get RGB
                int R = getR(clr);
                int G = getG(clr);
                int B = getB(clr);

                // Convert
                double quation = (255.0 / (double)(max - min));

                int newR = (int) ((R - min) * quation);
                int newG = (int) ((G - min) * quation);
                int newB = (int) ((B - min) * quation);

                // Save
                int rgb = toRGB(newR, newG, newB);

                image.setRGB(x, y, rgb);
            }
        }
    }

    public static void applyBrightness(BufferedImage image, int brightness)
    {
        // 2) Apply normalization
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                int clr = image.getRGB(x, y);

                // Get RGB
                int R = getR(clr);
                int G = getG(clr);
                int B = getB(clr);

                // Convert
                int newR = R + brightness;
                if (newR > 255)
                    newR = 255;

                int newG = G + brightness;
                if (newG > 255)
                    newG = 255;

                int newB = B + brightness;
                if (newB > 255)
                    newB = 255;

                // Save
                int rgb = toRGB(newR, newG, newB);

                image.setRGB(x, y, rgb);
            }
        }
    }

//------------------------------------------------------------------------
// 02.10.2015
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
                int newR = MAX_PIXEL - R;
                int newG = MAX_PIXEL - G;
                int newB = MAX_PIXEL - B;

                // Save
                int rgb = toRGB(newR, newG, newB);

                image.setRGB(x, y, rgb);
            }
        }
    }

//------------------------------------------------------------------------
// 02.10.2015
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
