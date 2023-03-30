package resizer.backend;

import org.junit.jupiter.api.Test;
import resizer.middle.ImageHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The image shrinker tests operate on int[][] arrays but the
 * png files may make it easier to visualize the original images and/or
 * to open them with the GUI program and inspect that way. Thus they
 * will be read in as .png and converted to int[][]
 * for testing
 * */
class GradientOperatorTests {

    @Test
    void sobelMiddleOfAllBlackImage() {

        Path resourceDir = Paths.get("src", "test", "resources");
        String imgFile = resourceDir.toAbsolutePath() + "/img_66_0.png";
        int[][] luminanceImg = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(imgFile));
            luminanceImg = ImageHelper.convertToGray(bufferedImage);
            for(int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    System.out.print(luminanceImg[i][j] + " ");
                }
                System.out.println();
            }

        }catch (IOException e) {
            System.err.println("Error opening: " + imgFile);
            assert(false);
        }
        EdgeAvoidanceImageShrinker shrinker = new EdgeAvoidanceImageShrinker();
        shrinker.setGradientOperator(EdgeAvoidanceImageShrinker.SOBEL);

        shrinker.setImage(luminanceImg);

        int middle = shrinker.sobel(3,3);

        assertEquals(0, middle, "sobel of all zeros should be zero");
    }

    @Test
    void sobelEdgeOfAllBlackImage() {

        Path resourceDir = Paths.get("src", "test", "resources");
        String imgFile = resourceDir.toAbsolutePath() + "/img_66_0.png";
        int[][] luminanceImg = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(imgFile));
            luminanceImg = ImageHelper.convertToGray(bufferedImage);
            for(int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    System.out.print(luminanceImg[i][j] + " ");
                }
                System.out.println();
            }

        }catch (IOException e) {
            System.err.println("Error opening: " + imgFile);
            assert(false);
        }
        EdgeAvoidanceImageShrinker shrinker = new EdgeAvoidanceImageShrinker();
        shrinker.setGradientOperator(EdgeAvoidanceImageShrinker.SOBEL);

        shrinker.setImage(luminanceImg);

        int bottomCorner = shrinker.sobel(5,5);

        assertEquals(0, bottomCorner, "sobel of all zeros should be zero");
    }

    @Test
    void prewittMiddleOfAllBlackImage() {

        Path resourceDir = Paths.get("src", "test", "resources");
        String imgFile = resourceDir.toAbsolutePath() + "/img_66_0.png";
        int[][] luminanceImg = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(imgFile));
            luminanceImg = ImageHelper.convertToGray(bufferedImage);
            for(int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    System.out.print(luminanceImg[i][j] + " ");
                }
                System.out.println();
            }

        }catch (IOException e) {
            System.err.println("Error opening: " + imgFile);
            assert(false);
        }
        EdgeAvoidanceImageShrinker shrinker = new EdgeAvoidanceImageShrinker();
        shrinker.setGradientOperator(EdgeAvoidanceImageShrinker.PREWITT);

        shrinker.setImage(luminanceImg);

        int middle = shrinker.prewitt(3,3);

        assertEquals(0, middle, "prewitt of all zeros should be zero");
    }

    @Test
    void prewittEdgeOfAllBlackImage() {

        Path resourceDir = Paths.get("src", "test", "resources");
        String imgFile = resourceDir.toAbsolutePath() + "/img_66_0.png";
        int[][] luminanceImg = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(imgFile));
            luminanceImg = ImageHelper.convertToGray(bufferedImage);
            for(int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    System.out.print(luminanceImg[i][j] + " ");
                }
                System.out.println();
            }

        }catch (IOException e) {
            System.err.println("Error opening: " + imgFile);
            assert(false);
        }
        EdgeAvoidanceImageShrinker shrinker = new EdgeAvoidanceImageShrinker();
        shrinker.setGradientOperator(EdgeAvoidanceImageShrinker.PREWITT);

        shrinker.setImage(luminanceImg);

        int bottomCorner = shrinker.prewitt(5,5);

        assertEquals(0, bottomCorner, "prewitt of all zeros should be zero");
    }


    @Test
    void prewittAllPointsOfMidSquareImage() {

        Path resourceDir = Paths.get("src", "test", "resources");
        String imgFile = resourceDir.toAbsolutePath() + "/img_66_1.png";
        int[][] luminanceImg = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(imgFile));
            luminanceImg = ImageHelper.convertToGray(bufferedImage);
            for(int row = 0; row < 6; row++) {
                for (int col = 0; col < 6; col++) {
                    System.out.print(luminanceImg[col][row] + " ");
                }
                System.out.println();
            }

        }catch (IOException e) {
            System.err.println("Error opening: " + imgFile);
            assert(false);
        }
        EdgeAvoidanceImageShrinker shrinker = new EdgeAvoidanceImageShrinker();
        shrinker.setGradientOperator(EdgeAvoidanceImageShrinker.PREWITT);

        shrinker.setImage(luminanceImg);

        int [][] gradientImage = shrinker.getEnergyImage();

        for(int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                System.out.print(gradientImage[col][row] + " ");
            }
            System.out.println();
        }

        //note cols and rows flipped for images
        int[][] expectedGradientImg =
                {{0,0,0,0,0,0},
                        {0,0,168,376,238,0},
                        {0,376,475,475,376,0},
                        {0, 504,531,475, 376,0},
                        {0,238,376,376,238,0},
                        {0,0,0,0,0,0}};

                /*{{0,  0,   0,   0,   0, 0},
                 {0,  0, 376, 504, 238, 0},
                 {0, 168, 475, 531, 376, 0},
                 {0, 376, 475, 475, 376, 0},
                 {0, 238, 376, 376, 238, 0},
                 {0,   0,   0,   0,   0, 0} };*/

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                assertEquals(expectedGradientImg[col][row], gradientImage[col][row], "gradient image comparison at: col "+ col + " row " + row + " failed.");
            }
        }
    }

    @Test
    void sobelAllPointsOfMidSquareImage() {

        Path resourceDir = Paths.get("src", "test", "resources");
        String imgFile = resourceDir.toAbsolutePath() + "/img_66_1.png";
        int[][] luminanceImg = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(imgFile));

            luminanceImg = ImageHelper.convertToGray(bufferedImage);
            for(int row = 0; row < 6; row++) {
                for (int col = 0; col < 6; col++) {
                    System.out.print(luminanceImg[col][row] + " ");
                }
                System.out.println();
            }

        }catch (IOException e) {
            System.err.println("Error opening: " + imgFile);
            assert(false);
        }
        EdgeAvoidanceImageShrinker shrinker = new EdgeAvoidanceImageShrinker();
        shrinker.setGradientOperator(EdgeAvoidanceImageShrinker.SOBEL);

        shrinker.setImage(luminanceImg);

        int [][] gradientImage = shrinker.getEnergyImage();

        for(int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                System.out.print(gradientImage[col][row] + " ");
            }
            System.out.println();
        }

        //note cols and rows flipped for images
        int[][] expectedGradientImg = { {0,0,0,0,0,0},
                                        {0,0,336,531,238,0},
                                        {0,531,713,713,531,0},
                                        {0,672,751,713,531,0},
                                        {0,238,531,531,238,0},
                                        {0,0,0,0,0,0} };

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                assertEquals(expectedGradientImg[col][row], gradientImage[col][row], "gradient image comparison at: col "+ col + " row " + row + " failed.");
            }
        }
    }


}