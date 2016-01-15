package processing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import processing.common.Pixel;
import processing.common.RGB;
import util.SmartConsole;

import processing.filters.convolution.GaussianFilter;
import processing.filters.convolution.HighPass;
import processing.filters.convolution.LowPass;
import processing.filters.convolution.SobelFilter;

import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/*

*/
public class FeatureExtraction
{
    private static Pixel WhitePixel = new Pixel(255, 255, 255);
    private static Pixel BlackPixel = new Pixel(0, 0, 0);
    private static Pixel TMPPixel = new Pixel(0, 255, 0);

    //------------------------------------------------------------------------

    public static void compute(BufferedImage image)
    {
        //compute1(image);
        panasiuk(image);
    }

    public static void compute1(BufferedImage image){
        HistogramExpension.compute_all(image);

        GaussianFilter.compute(image);

        Treshold.compute(image, 195);
/*
        SobelFilter.compute(image);

        FloodFill.compute(image, 10, 10, TMPPixel);

        //removeNoise(image);

        replacePixels(image, WhitePixel, TMPPixel);
        replacePixels(image, TMPPixel, WhitePixel);*/
    }

// -----------------------------------------------------------------

    public static void panasiuk(BufferedImage image){
        BufferedImage tmpImage = deepCopy(image);

        Treshold.compute(tmpImage, 190);

        SobelFilter.compute(image);
        GaussianFilter.compute(image);

        subtractImages(image, tmpImage);

    }
    public static void subtractImages(BufferedImage subtractareeImage,
                                    BufferedImage subtractorImage){

        for (int x = 0; x < subtractareeImage.getWidth(); x++)
        {
             for (int y = 0; y < subtractareeImage.getHeight(); y++)
             {

                Pixel subtractareePixel =
                    new Pixel(subtractareeImage.getRGB(x, y));

                Pixel subtractorPixel =
                    new Pixel(subtractorImage.getRGB(x, y));

                String subtractionString = subtractareePixel.toString()
                            + " - " +
                            subtractorPixel.toString() + " = ";

                subtractareePixel.subtract(subtractorPixel);
                subtractionString += subtractionString + subtractareePixel.toString();

                SmartConsole.Print(subtractionString);

                subtractareeImage.setRGB(x, y, subtractareePixel.getColor());
             }
        }
    }

// -----------------------------------------------------------------

    public static void replacePixels(BufferedImage image,
                                        Pixel toRemovePixel,
                                        Pixel toReplacePixel){
        for (int x = 0; x < image.getWidth(); x++)
        {
             for (int y = 0; y < image.getHeight(); y++)
             {
                 Pixel currentPixel = new Pixel(image.getRGB(x, y));
                 if(currentPixel.equals(toRemovePixel))
                    image.setRGB(x, y, toReplacePixel.getColor());
             }
        }
    }

    static BufferedImage deepCopy(BufferedImage bi) {
         ColorModel cm = bi.getColorModel();
         boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
         WritableRaster raster = bi.copyData(null);
         return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

}
