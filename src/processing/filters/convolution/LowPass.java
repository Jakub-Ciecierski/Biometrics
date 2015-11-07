package processing.filters.convolution;

import java.awt.image.BufferedImage;
import processing.common.RGB;

/**
 * Created by jakub on 11/6/15.
 */
public class LowPass {

    private static final int [] kernelMatrix =
            {
                    1, 1, 1,
                    1, 1, 1,
                    1, 1, 1
            };

    private static final double divisor = 9.0f;

    // The dimensions of the kernelMatrix
    private static final int n = 3;
    private static final int m = 3;

    // Index of the anchor element in the kernelMatrix
    private static final int anchor = 4;

    // The interval to iterate through the kernelMatrix with
    private static final int sRange = -1;
    private static final int eRange = 1;

    // Returns the value of kernel matrix
    // The indexing is relative to the anchor point.
    //
    private static int getKernelPoint(int i, int j){
        return kernelMatrix[anchor + n*i + j];
    }

    private static int computeKernelColor(BufferedImage imageRead, int x, int y) {
        double newR = 0, newG = 0, newB = 0;

        int width = imageRead.getWidth();
        int height = imageRead.getHeight();

        // Enter kernel matrix
        for(int k = sRange; k <= eRange; k++) {
            double relR = 0, relG = 0, relB = 0;

            for (int l = sRange; l <= eRange; l++) {
                int i, j;
                int relativeColor;
                double kernelPoint;

                i = x+k < 0 ? (width + (x+k)) : x+k;
                j = y+l < 0 ? (height + (y+l)) : y+l;
                i = i >= width ? (i - width) : i;
                j = j >= height ? (j - height) : j;

                // Get relative color
                relativeColor = imageRead.getRGB(i, j);
                // Get the kernel point
                kernelPoint = getKernelPoint(k, l);

                relR += RGB.getR(relativeColor) * kernelPoint;
                relG += RGB.getG(relativeColor) * kernelPoint;
                relB += RGB.getB(relativeColor) * kernelPoint;

            }

            newR += relR;
            newG += relG;
            newB += relB;
        }

        newR /= divisor;
        newG /= divisor;
        newB /= divisor;

        // Save
        int rgb = RGB.toRGB(RGB.boundChannelValue((int)newR),
                RGB.boundChannelValue((int)newG),
                RGB.boundChannelValue((int)newB));

        return rgb;
    }

    public static void compute(BufferedImage image)
    {
        BufferedImage imageRead = RGB.copyImage(image);

        for (int x = 0; x < image.getWidth(); x++)
        {
            for (int y = 0; y < image.getHeight(); y++)
            {
                int rgb = computeKernelColor(imageRead, x , y);

                image.setRGB(x, y, rgb);
            }
        }
    }


}

