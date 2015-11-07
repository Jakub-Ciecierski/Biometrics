package processing;

import processing.common.Pixel;
import processing.filters.convolution.LowPass;
import processing.filters.convolution.SobelFilter;
import util.SmartConsole;

import java.awt.image.BufferedImage;

/**
 * Created by jakub on 11/7/15.
 */
public class Pupil {

    private static final int threshold = 7;

    // How many times Low Pass filter is applied
    private static final int lowPassCount = 10;

    // The beginning x-coordinate of pupil's diameter
    private static int maxStartX;
    // The end of x-coordinate pupil's diameter
    private static int maxEndX;
    // The y-coordinate of pupil's diameter
    private static int maxY;

    // The Diameter of the pupil
    private static int diameter;
    // The center of the pupil
    private static int center;

    // The color of the diameter
    private static Pixel diameterPixel = new Pixel(255, 0, 0);
    // The color of the center
    private static Pixel centerPixel = new Pixel(0, 255, 0);

    // The color of pupil's exterior. Everything that is not pupil is color with that pixel
    private static Pixel labelPixel = new Pixel(125, 125, 125);
    // The color of pupil. Pupil is colored with that pixel
    private static Pixel pupilPixel = new Pixel(0, 0, 0);

    // Starts computations for finding pupil in an image.
    public static void compute(BufferedImage image){
        filterPhase(image);

        findClosedRegion(image);

        findDiameter(image);
    }

    // -----------------------------------------------------------------------

    private static void filterPhase(BufferedImage image){
        //Brightness.compute(image, 90);

        for(int i = 0; i < lowPassCount; i++)
            LowPass.compute(image);

        Grayscale.compute(image);

        for(int i = 0; i < lowPassCount; i++)
            LowPass.compute(image);

        Treshold.compute(image, threshold);

        SobelFilter.compute(image);
    }

    // -----------------------------------------------------------------------

    private static void findClosedRegion(BufferedImage image){
        // Compute flood fill.
        // All the pixel not covered by flood fill are a closes region
        int startX = 0;
        int startY = 0;

        FloodFill.compute(image, startX, startY, labelPixel);

        // Fill the closed region (i.e. the pupil) with black pixels
        fillPupil(image, labelPixel, pupilPixel);
    }

    // Fill all the pixel of closed regions with pupilPixel
    private static void fillPupil(BufferedImage image,
                                  Pixel skipPixel, Pixel pupilPixel) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                Pixel pixel = new Pixel(image.getRGB(x, y));

                if(!pixel.equals(skipPixel)){
                    image.setRGB(x, y, pupilPixel.getColor());
                }
            }
        }
    }

    // -----------------------------------------------------------------------

    private static void findDiameter(BufferedImage image){
        // Find the longest scan line
        findLongestScanLine(image, pupilPixel);

        drawLine(image, diameterPixel);

        // Draw the center.
        diameter = maxEndX - maxStartX;
        center = diameter/2 + maxStartX;
        SmartConsole.Print("Center: " + center + " Diameter: " + diameter);

        drawLineCenter(image, center, maxY, centerPixel);
    }

    private static void findLongestScanLine(BufferedImage image,
                                 Pixel pupilPixel){
        Pixel currPixel, nextPixel;
        int currLineLength, maxLineLength;
        int startX;

        startX = 0;
        maxStartX = maxEndX = 0;

        maxLineLength = 0;
        maxY = -1;

        // For each row
        for (int y = 0; y < image.getHeight(); y++) {
            currLineLength = 0;
            for (int x = 0; x < image.getWidth() - 1; x++) {
                currPixel = new Pixel(image.getRGB(x, y));
                nextPixel = new Pixel(image.getRGB(x+1, y));

                // The line is ongoing. Potentially new line
                if(currPixel.equals(pupilPixel) &&
                    nextPixel.equals(pupilPixel)) {
                    // Check if it is a new line
                    if(currLineLength == 0) {
                        startX = x;
                    }
                    currLineLength++;
                }
                // Line disconnected. Line Finished.
                else{
                    // Check if it is the longest thus far
                    if (currLineLength > maxLineLength) {
                        maxLineLength = currLineLength;
                        maxY = y;
                        maxStartX = startX;
                        maxEndX = x;
                    }
                    // Reset the length
                    currLineLength = 0;
                }

            }
            if (currLineLength > maxLineLength) {
                maxLineLength = currLineLength;
                maxY = y;
                int x = image.getWidth() - 1;
                maxStartX = startX;
                maxEndX = x;
            }
        }
    }

    private static void drawLineCenter(BufferedImage image,
                                       int x, int y,
                                       Pixel pixel){
        image.setRGB(x, y, pixel.getColor());
        image.setRGB(x-1, y, pixel.getColor());
        image.setRGB(x+1, y, pixel.getColor());

        image.setRGB(x, y + 1, pixel.getColor());
        image.setRGB(x-1, y + 1, pixel.getColor());
        image.setRGB(x+1, y + 1, pixel.getColor());

        image.setRGB(x, y - 1, pixel.getColor());
        image.setRGB(x-1, y - 1, pixel.getColor());
        image.setRGB(x+1, y - 1, pixel.getColor());
    }

    private static void drawLine(BufferedImage image, Pixel linePixel){
        // Draw the diameter line
        for (int x = maxStartX; x < maxEndX; x++) {
            image.setRGB(x, maxY, linePixel.getColor());
        }

    }
}
