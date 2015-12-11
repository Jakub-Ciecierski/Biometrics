package processing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import processing.common.Pixel;
import processing.common.RGB;
import util.SmartConsole;

/*

*/
public class KMM
{
    private static final int BM_UNDEFINED = -1;
    private static final int BM_BACKGROUND = 0;
    private static final int BM_INTERIOR = 1;
    private static final int BM_STICKING_TO_BACKGROUND = 2;
    private static final int BM_STICKING_ELBOW_TO_BACKGROUND = 3;
    private static final int BM_TO_DELETE = 4;


    //------------------------------------------------------------------------

    private static Pixel Pixel0 = new Pixel(255, 255, 255);
    private static Pixel Pixel1 = new Pixel(0, 0, 0);
    private static Pixel Pixel2 = new Pixel(0, 255, 0);
    private static Pixel Pixel3 = new Pixel(0, 0, 255);
    private static Pixel Pixel4 = new Pixel(255, 0, 0);

    //------------------------------------------------------------------------

    private static final int [] StickyNeighbours =
            {
                    BM_STICKING_ELBOW_TO_BACKGROUND,    BM_STICKING_TO_BACKGROUND,  BM_STICKING_ELBOW_TO_BACKGROUND,
                    BM_STICKING_TO_BACKGROUND,          BM_INTERIOR,                BM_STICKING_TO_BACKGROUND,
                    BM_STICKING_ELBOW_TO_BACKGROUND,    BM_STICKING_TO_BACKGROUND,  BM_STICKING_ELBOW_TO_BACKGROUND
            };

    //------------------------------------------------------------------------

    private static final int [] BinaryWeights =
            {
                    128, 1,  2,
                    64,  0,  4,
                    32,  16, 8
            };

    //------------------------------------------------------------------------

    private static final int n = 3;
    private static final int m = 3;

    private static final int anchor = 4;

    // The interval to iterate through the kernelMatrix with
    private static final int sRange = -1;
    private static final int eRange = 1;


    //------------------------------------------------------------------------

    private static final int [] DeletionArray = {
            3, 5, 7, 12, 13, 14, 15, 20,
            21, 22, 23, 28, 29, 30, 31, 48,
            52, 53, 54, 55, 56, 60, 61, 62,
            63, 65, 67, 69, 71, 77, 79, 80,
            81, 83, 84, 85, 86, 87, 88, 89,
            91, 92, 93, 94, 95, 97, 99, 101,
            103, 109, 111, 112, 113, 115, 116, 117,
            118, 119, 120, 121, 123, 124, 125, 126,
            127, 131, 133, 135, 141, 143, 149, 151,
            157, 159, 181, 183, 189, 191, 192, 193,
            195, 197, 199, 205, 207, 208, 209, 211,
            212, 213, 214, 215, 216, 217, 219, 220,
            221, 222, 223, 224, 225, 227, 229, 231,
            237, 239, 240, 241, 243, 244, 245, 246,
            247, 248, 249, 251, 252, 253, 254, 255
    };


    //------------------------------------------------------------------------

    public static void compute(BufferedImage image)
    {
        int[][] bitMap = new int[image.getWidth()][image.getHeight()];

        // Step 0 - Create a binary image
        SmartConsole.Print("Step 0 - Filtering");
        filterStep(image);


        // Step 1 - Bitmap the image, save 0s and 1s in bitMap
        SmartConsole.Print("Step 1 - Init BitMap");
        initBitMap(image, bitMap);

        // Step 2
        SmartConsole.Print("Step 2 - Choose Pixel Sticking to Background");
        choosePixelStickingToBackground(bitMap);

        // Step 3
        SmartConsole.Print("Step 2 - Binary Weights");
        choosePixelSToDeletion_BinaryWeights(bitMap);

        // Step 4.1
        SmartConsole.Print("Step 4.1 - Remove Pixel Without loss of connectivity: BM_STICKING_TO_BACKGROUND");
        removePixelsWithoutLossOfConnectivity(bitMap, BM_STICKING_TO_BACKGROUND);
        // Step 4.2
        SmartConsole.Print("Step 4.2 - Remove Pixel Without loss of connectivity: BM_STICKING_ELBOW_TO_BACKGROUND");
        removePixelsWithoutLossOfConnectivity(bitMap, BM_STICKING_ELBOW_TO_BACKGROUND);

        updateImageWithBitMap(image, bitMap);

    }


   // Step 0, Filters - Normalization and Binarization.
    private static void filterStep(BufferedImage image){
        // Apply Normalization
        HistogramExpension.compute_all(image);

        // Apply Binarization
        int t = 175;
        Grayscale.compute(image);
        Treshold.compute(image, t);
    }

    private static void initBitMap(BufferedImage image, int[][] bitMap) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                int clr = image.getRGB(x, y);
                Pixel pixel = new Pixel(clr);
                if(pixel.equals(Pixel0))
                    bitMap[x][y] = BM_BACKGROUND;
                else if(pixel.equals(Pixel1))
                    bitMap[x][y] = BM_INTERIOR;
                else {
                    SmartConsole.Print("No Pixel0 nor Pixel1 found");
                    bitMap[x][y] = BM_UNDEFINED;
                }
            }
        }
    }

    private static void updateImageWithBitMap(BufferedImage image, int[][] bitMap){
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int bmValue = bitMap[x][y];
                if(bmValue == BM_BACKGROUND){
                    image.setRGB(x, y, Pixel0.getColor());
                }
                else if(bmValue == BM_INTERIOR){
                    image.setRGB(x, y, Pixel1.getColor());
                }
                else if(bmValue == BM_STICKING_TO_BACKGROUND){
                    image.setRGB(x, y, Pixel2.getColor());
                }
                else if(bmValue == BM_STICKING_ELBOW_TO_BACKGROUND){
                    image.setRGB(x, y, Pixel3.getColor());
                }
                else if(bmValue == BM_TO_DELETE){
                    image.setRGB(x, y, Pixel4.getColor());
                }
            }
        }
    }

    //------------------------------------------------------------------------

    // Step 2
    // a) All '1s' sticking to background become '2'
    // b) All '1s' sticking to background by corner are changed to '3s'
    private static void choosePixelStickingToBackground(int[][] bitMap){
       // For each pixel call
       for (int x = 0; x < bitMap.length; x++) {
            for (int y = 0; y < bitMap[x].length; y++) {
                selectSickingPixel(bitMap, x, y);
            }
        }
    }

    private static void selectSickingPixel(int[][] bitMap, int x, int y){
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                int rel_i = x+i;
                int rel_j = x+j;
                if(rel_i >= 0 && rel_i < bitMap.length && rel_j >= 0 && rel_j < bitMap[x].length){

                    int currentValue = bitMap[rel_i][rel_j];
                    if(currentValue == BM_BACKGROUND) {
                        int whichSticky = getPointRelativeToAnchor(i, j, StickyNeighbours);

                        if(whichSticky == BM_INTERIOR) continue;
                        bitMap[x][y] = whichSticky;
                        if(whichSticky == BM_STICKING_TO_BACKGROUND){
                            return;
                        }
                    }
                }
            }
        }
    }

    //------------------------------------------------------------------------

    // Step 3
    private static void choosePixelSToDeletion_BinaryWeights(int[][] bitMap){
        for (int x = 0; x < bitMap.length; x++) {
            for (int y = 0; y < bitMap[x].length; y++) {
                if(bitMap[x][y] == BM_BACKGROUND) continue;

                int weight = getSumOfBinaryWeights(bitMap, x, y);
                if(isInDeletionArray(weight))
                    bitMap[x][y] = BM_TO_DELETE;
            }
        }

        for (int x = 0; x < bitMap.length; x++) {
            for (int y = 0; y < bitMap[x].length; y++) {
                    if(bitMap[x][y] == BM_TO_DELETE)
                        bitMap[x][y] = BM_BACKGROUND;
            }
        }
    }

    private static int getSumOfBinaryWeights(int[][] bitMap, int x, int y){
        int sum = 0;

        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                int rel_i = x+i;
                int rel_j = x+j;
                if(rel_i >= 0 && rel_i < bitMap.length && rel_j >= 0 && rel_j < bitMap[x].length){
                    if(bitMap[rel_i][rel_j] != BM_BACKGROUND)
                        sum += getPointRelativeToAnchor(i, j, BinaryWeights);
                }
            }
        }
        return sum;
    }

    //------------------------------------------------------------------------

    private static void removePixelsWithoutLossOfConnectivity(int[][] bitMap, int bitMapValueToRemove){
        for (int x = 0; x < bitMap.length; x++) {
            for (int y = 0; y < bitMap[x].length; y++) {
                if(bitMap[x][y] != bitMapValueToRemove) return;

                int[] neighbourhood = new int[9];
                int source = -1;

                for(int i = -1; i <= 1; i++){
                    for(int j = -1; j <= 1; j++){
                        int rel_i = x+i;
                        int rel_j = x+j;

                        int index = anchor + n*i + j;

                        if(rel_i >= 0 && rel_i < bitMap.length && rel_j >= 0 && rel_j < bitMap[x].length){
                            if(bitMap[rel_i][rel_j] == BM_BACKGROUND) {
                                neighbourhood[index] = BM_BACKGROUND;

                            }
                            else{
                                neighbourhood[index] = BM_INTERIOR;
                                source = index;
                            }
                        }

                        // Assume that this middle pixel is removed
                        index = anchor + n*0 + 0;
                        neighbourhood[index] = BM_BACKGROUND;
                    }

                }
                if(source != -1)
                    if(isConnectedToAll(source, neighbourhood)){
                        bitMap[x][y] = BM_BACKGROUND;
                    }
            }
        }
    }


    // 1) All states reachable from initial state are Reachable
    // 2) All states reachable from Reachable states are Reachable
    // 3) Repeat 2) until no further changes are made
    private static boolean isConnectedToAll(int source, int[] neighbourhood){
        final int NODE_ON = 1;
        final int NODE_OFF = 0;

        int neighLen = neighbourhood.length;

        // Flags of reachability: 1 if reachable, 0 otherwise
        int[] reachableNodesFlags = new int[neighLen];
        for(int i = 0; i < neighLen; i++){
            reachableNodesFlags[i] = NODE_OFF;
        }
        reachableNodesFlags[source] = NODE_ON;

        List<Integer> currentNodes = new ArrayList<Integer>();
        currentNodes.add(source);

        do{
            int currNode = currentNodes.get(0);
            currentNodes.remove(0);

            for(int i = -1; i <= 1; i++){
                for(int j = -1; j <= 1; j++) {
                    int nextNode = currNode + n * i + j;
                    if (nextNode >= 0 && nextNode < neighLen) {
                        if (neighbourhood[nextNode] == BM_INTERIOR) {
                            if (reachableNodesFlags[nextNode] != NODE_ON) {
                                reachableNodesFlags[nextNode] = NODE_ON;
                                currentNodes.add(nextNode);
                            }
                        }
                    }
                }
            }
        }while(!currentNodes.isEmpty());

        int reachableCount = 0;
        int interiorCount = 0;
        for(int i = 0; i < neighLen; i++){
            if(reachableNodesFlags[i] == NODE_ON)
                reachableCount++;
            if(neighbourhood[i] == BM_INTERIOR)
                interiorCount++;
        }

        return (reachableCount == interiorCount);
    }

    //------------------------------------------------------------------------

    private static boolean isInDeletionArray(int x){
        for(int i = 0; i < DeletionArray.length; i++){
            if( x == DeletionArray[i])
                return true;
        }
        return false;
    }

    // Returns the value of binary matrix
    // The indexing is relative to the anchor point.
    private static int getPointRelativeToAnchor(int i, int j, int[] KernelMatrix){
        return KernelMatrix[anchor + n*i + j];
    }
}

