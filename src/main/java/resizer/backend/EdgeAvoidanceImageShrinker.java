package resizer.backend;

import java.util.function.BiFunction;


/**
 * Class to shrink an image by removing a strip of pixels from the top of
 * the image to the bottom through a low energy path of pixels
 */
public class EdgeAvoidanceImageShrinker {

    //the original luminance image
    private int[][] img;
    //after applying the gradient at all (x,y) positions we get the energy image
    private int[][] energyImage;
    private int width;
    private int height;

    public static String SOBEL = "sobel";
    public static String PREWITT = "prewitt";

    //A BiFunction to convert an (x,y) position within img into the corresponding gradient
    private BiFunction<Integer,Integer,Integer> gradientOperator;

    /**
     * Public Constructor
     */
    public EdgeAvoidanceImageShrinker() {
        gradientOperator = this::sobel;
    }

    /**
     * initialize img
     * @param luminanceImage a gray scale luminance image to save as img for later processing
     */
    public void setImage(int[][] luminanceImage) {
        img = luminanceImage;
        width = luminanceImage.length;
        height = luminanceImage[0].length;
        energyImage = new int[luminanceImage.length][luminanceImage[0].length];
        generateEnergyImage();
    }

    /**
     * Return the energy image, i.e, an image in which the gradient has been applied at every x,y position to img
     * note the gradient will wrap across the edges of the img where required
     * pre: generateEnergyImage() has already been called
     * @return the energy image representation of img
     */
    public int[][] getEnergyImage() {
        return energyImage;
    }

    /**
     * Set the gradient to either Prewitt or Sobel
     * @param op a string specifying either "prewitt" or "sobel"
     */
    public void setGradientOperator(String op) {
        if (op.equals(SOBEL)) {
            gradientOperator = this::sobel;
        }
        else{
            gradientOperator = this::prewitt;
        }
    }

    /**
     * Return the low energy path from the top of the energy image to the bottom
     * given as an int[] containing the index of the column of the pixel within in the path
     * .e.g., the 0th index in the returned array will contain the column representing the
     * pixel in the low energy path at row 0, the 1st index is the column in the path at row 1, ...
     * pre: the energy image is initialized
     * @return the low energy path as an array of columns in order from the top of the image to the bottom
     */
    public int[] lowEnergyPath() {

        //TODO Complete this method to compute the low energy path
        return new int[energyImage[0].length];
    }

    /**
     * Perform a 3x3 convolution between G and A
     * @param G the kernel
     * @param A the image to convolve the kernel with
     * @return the convolution between G and A
     */
    private int convolve3x3(int[][] G, int[][] A) {
        int result = 0;
        for (int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                result += G[2-i][2-j] * A[i][j];
            }
        }
        return result;
    }

    /**
     * Compute and return the gradient of img using the sobel operator, centered at position (x,y)
     * This method is used as a BiFunction
     * pre: img is initialized to a gray scale image
     * post: no changes
     * @param x the x position of the center of where the sobel operator is to be applied within the image
     * @param y the y position of the center of where the sobel operator is to be applied within the image
     * @return the gradient as computed by the sobel operator centered at position (x,y) within img
     */
    public int sobel(int x, int y) {

        /**
         * From wikipedia: https://en.wikipedia.org/wiki/Sobel_operator
         * G_x = +1 0 -1
         *        +2 0 -2    * A
         *       +1  0 -1
         *
         * G_y = +1 +2 +1
         *        0   0  0   * A
         *        -1 -2  -1
         */
        int xMinus1 = x > 0? x-1: width-1;
        int yMinus1 = y > 0? y-1:height-1;
        int xPlus1 = (x+1) % width;
        int yPlus1 = (y+1) % height;

        int[][] A = { {img[xMinus1][yMinus1],img[x][yMinus1],    img[xPlus1][yMinus1]},
                      {img[xMinus1][y],      img[x][y],          img[xPlus1][y] },
                      {img[xMinus1][yPlus1], img[x][yPlus1],     img[x][yPlus1] } };
        int[][] kernelX = { {1,0,-1},
                            {2,0,-2},
                            {1,0,-1} };

        int[][] kernelY = { {1,2,1},
                            {0,0,0},
                            {-1,-2,-1} };

        //gradients in x and y directions
        int Gx = convolve3x3(kernelX, A);
        int Gy = convolve3x3(kernelY, A);

        //round to integer
        return (int)Math.round(Math.sqrt(Gx*Gx + Gy*Gy));
    }


    /**
     * Compute and return the gradient of img using the prewitt operator, centered at position (x,y)
     * This method is used as a BiFunction
     * pre: img is initialized to a gray scale image
     * post: no changes
     * @param x the x position of the center of where the prewitt operator is to be applied within the image
     * @param y the y position of the center of where the prewitt operator is to be applied within the image
     * @return the gradient as computed by the prewitt operator centered at position (x,y) within img
     */
    public int prewitt(int x, int y) {
        /**
         * From wikipedia: https://en.wikipedia.org/wiki/Prewitt_operator
         * G_x = [+1 0 -1
         *        +1 0 -1    * A
         *       +1  0 -1]
         *
         * G_y = [+1 +1 +1
         *        0   0  0   * A
         *        -1 -1  -1]
         */

        //TODO Complete this method (it will look very similar to the sobel method)

        return 0;
    }


    /**
     * Generate the energy image by computing the gradient at all possible (x,y) positions
     * pre: img is a set and initialized as a gray image (luminance image)
     */
    private void generateEnergyImage() {
        //for the columns
        for (int x = 0; x < width; x++) {
            //rows
            for(int y = 0; y < height; y++) {
                energyImage[x][y] = gradientOperator.apply(x,y);
            }
        }
    }
}
