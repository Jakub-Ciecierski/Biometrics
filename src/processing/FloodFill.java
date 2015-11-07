package processing;

import processing.common.Pixel;
import processing.common.RGB;
import util.SmartConsole;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * Created by jakub on 11/7/15.
 */
public class FloodFill {
/*
    public class Node{
        public int x;
        public int y;

        public Node(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
*/
    /*

     Breadth-first Flood Fill algorithm, using queue
     */
    public static void compute(BufferedImage image,
                               int startX, int startY,
                               Pixel labelPixel)
    {
        int width, height;
        Pixel backgroundPixel;

        width = image.getWidth();
        height = image.getHeight();

        backgroundPixel = new Pixel(0, 0, 0);

        LinkedList<int[]> q = new LinkedList<>();

        q.addFirst(new int[]{startX, startY});

        while (!q.isEmpty()) {
            Pixel pixel;
            int[] node = q.removeLast();
            int x = node[0];
            int y = node[1];

            if ((x >= 0) && (x < width) &&
                (y >= 0) && (y < height) &&
                (new Pixel(image.getRGB(x, y)))
                    .equals(backgroundPixel)) {
                image.setRGB(x, y, labelPixel.getColor());

                q.addFirst(new int[]{x + 1, y});
                q.addFirst(new int[]{x, y + 1});

                q.addFirst(new int[]{x, y - 1});
                q.addFirst(new int[]{x - 1, y});
            }
        }
    }
}
