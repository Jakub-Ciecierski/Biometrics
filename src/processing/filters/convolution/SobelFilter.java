package processing.filters.convolution;

import java.awt.image.BufferedImage;
import processing.common.RGB;

/**
 * Created by jakub on 11/6/15.
 */
public class SobelFilter {

    private static int p0, p1, p2, p3, p4, p5, p6, p7;

    private static void computeNeighbours(BufferedImage imageRead, int x, int y){
        int i, j;
        int width, height;

        width = imageRead.getWidth();
        height = imageRead.getHeight();

        p0 = p1 = p2 = p3 = p4 = p5 = p6 = p7 = 0;

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

    }

    private static int computeKernelColor(BufferedImage imageRead, int x, int y) {
        double xR, xG, xB;
        double yR, yG, yB;
        double pxR, pxG, pxB;
        int newR = 0, newG = 0, newB = 0;

        computeNeighbours(imageRead, x, y);

        xR = (RGB.getR(p2) + 2 * RGB.getR(p3) + RGB.getR(p4)) - (RGB.getR(p0) + 2 * RGB.getR(p7) + RGB.getR(p6));
        xG = (RGB.getG(p2) + 2 * RGB.getG(p3) + RGB.getG(p4)) - (RGB.getG(p0) + 2 * RGB.getG(p7) + RGB.getG(p6));
        xB = (RGB.getB(p2) + 2 * RGB.getB(p3) + RGB.getB(p4)) - (RGB.getB(p0) + 2 * RGB.getB(p7) + RGB.getB(p6));

        yR = (RGB.getR(p6) + 2 * RGB.getR(p5) + RGB.getR(p4)) - (RGB.getR(p0) + 2 * RGB.getR(p1) + RGB.getR(p2));
        yG = (RGB.getG(p6) + 2 * RGB.getG(p5) + RGB.getG(p4)) - (RGB.getG(p0) + 2 * RGB.getG(p1) + RGB.getG(p2));
        yB = (RGB.getB(p6) + 2 * RGB.getB(p5) + RGB.getB(p4)) - (RGB.getB(p0) + 2 * RGB.getB(p1) + RGB.getB(p2));

        pxR = Math.sqrt(xR*xR + yR*yR);
        pxG = Math.sqrt(xG*xG + yG*yG);
        pxB = Math.sqrt(xB*xB + yB*yB);

        newR = (int)pxR;
        newG = (int)pxG;
        newB = (int)pxB;

        newR = RGB.boundChannelValue(newR);
        newG = RGB.boundChannelValue(newG);
        newB = RGB.boundChannelValue(newB);

        // Save
        int rgb = RGB.toRGB(newR, newG, newB);

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

