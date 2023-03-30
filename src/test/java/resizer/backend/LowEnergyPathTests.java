package resizer.backend;
import org.junit.jupiter.api.Test;
import resizer.middle.ImageHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LowEnergyPathTests {

    //test 1: low energy path has columns that are always +1 or -1 mod the size of the image


    //test 2: a simple image with a straight line path


    @Test
    void straightLineDownMiddlePath() {

        //make a simple 5x5 image with the following pixel values:
        /**
         * luminance image pixel values:
         *      15 50 50 50 75
         *      20 50 50 50 80
         *      25 50 50 50 85
         *      30 50 50 50 90
         *      35 50 50 50 95
         *
         *      the low energy path 'should' go down the middle
         *      (since those middle values contain a 3x3 with a gradient = 0)
         */
        //remember images go column major
        int[][] luminanceImg = { {15, 20, 25, 30, 35}, {50, 50, 50, 50, 50}, {50, 50, 50, 50, 50},
                {50, 50, 50, 50, 50}, {75, 80, 85, 90, 95}};
        EdgeAvoidanceImageShrinker shrinker = new EdgeAvoidanceImageShrinker();
        shrinker.setGradientOperator(EdgeAvoidanceImageShrinker.SOBEL);

        shrinker.setImage(luminanceImg);


        int[][] energyImg = shrinker.getEnergyImage();

        //remember to reverse col and row so it prints in a more readable format
        System.out.println("energy image (converted from the original luminance image)");
        for(int x = 0; x < energyImg[0].length; x++) {
            for(int y = 0; y < energyImg.length; y++) {
                System.out.print(energyImg[y][x] + " ");
            }
            System.out.println();
        }

        int[] lowPath = shrinker.lowEnergyPath();

        System.out.println("low energy path");
        for(int i = 0; i < energyImg.length; i++) {
            System.out.println(lowPath[i]);
        }

        for(int i = 0; i < lowPath.length; i++) {
            assertEquals(2, lowPath[i], "expecting the middle column to be the low energy path");
        }
    }

    @Test
    void lowEnergyDiagonalPath() {

        //make a simple 5x5 image with the following pixel values:
        /**
         * luminance image pixel values:
         *      50x  50  55  100  50
         *      50  50x  50  100  50
         *      50  50   50x 50   60
         *      100 100  50  50x  50
         *      50   50  100  50  50x
         *      the low energy path 'should' go down the diagonal (marked with x's)
         */
        //remember images go column major
        int[][] luminanceImg = { {50, 50, 50, 100, 50}, {50, 50, 50, 100, 50}, {55, 50, 50, 50, 50},
                {100, 100, 50, 50, 50}, {50, 50, 60, 50, 50}};
        EdgeAvoidanceImageShrinker shrinker = new EdgeAvoidanceImageShrinker();
        shrinker.setGradientOperator(EdgeAvoidanceImageShrinker.SOBEL);

        shrinker.setImage(luminanceImg);


        int[][] energyImg = shrinker.getEnergyImage();

        //remember to reverse col and row so it prints in a more readable format
        System.out.println("energy image (converted from the original luminance image)");
        for(int x = 0; x < energyImg[0].length; x++) {
            for(int y = 0; y < energyImg.length; y++) {
                System.out.print(energyImg[y][x] + " ");
            }
            System.out.println();
        }

        int[] lowPath = shrinker.lowEnergyPath();

        System.out.println("low energy path");
        for(int i = 0; i < energyImg.length; i++) {
            System.out.println(lowPath[i]);
        }

        //the path should be the diagonal from top left to bottom right
        for(int i = 0; i < lowPath.length; i++) {
            assertEquals(i, lowPath[i], "expecting the diagonal to be the low energy path");
        }
    }


    @Test
    void lowEnergyNoWrapPath() {

        //make a simple 5x5 image with the following pixel values:
        /**
         * luminance image pixel values:
         *      100  50  50x  50  55
         *      100  50  50x  50  50
         *      50   60x 50   50  50
         *      50x  50  100  100  50
         *      50  50x  50   50  100
         *      the low energy path 'should' not wrap from one side to the other
         *      because this would distort the image
         *      the x's above chart the low energy path
         */
        //remember images go column major
        int[][] luminanceImg = { {100, 100, 50, 50, 50}, {50, 50, 60, 50, 50}, {50, 50, 50, 100, 50},
                {50, 50, 50, 100, 50}, {50, 50, 50, 50, 100}};
        EdgeAvoidanceImageShrinker shrinker = new EdgeAvoidanceImageShrinker();
        shrinker.setGradientOperator(EdgeAvoidanceImageShrinker.SOBEL);

        shrinker.setImage(luminanceImg);

        int[][] energyImg = shrinker.getEnergyImage();

        //remember to reverse col and row so it prints in a more readable format
        System.out.println("energy image (converted from the original luminance image)");
        for(int x = 0; x < energyImg[0].length; x++) {
            for(int y = 0; y < energyImg.length; y++) {
                System.out.print(energyImg[y][x] + " ");
            }
            System.out.println();
        }

        int[] lowPath = shrinker.lowEnergyPath();

        System.out.println("low energy path");
        for(int i = 0; i < energyImg.length; i++) {
            System.out.println(lowPath[i]);
        }

        assertEquals(2, lowPath[0], "expecting a meandering path that doesn't quite go where you might expect");
        assertEquals(2, lowPath[1], "expecting a meandering path that doesn't quite go where you might expect");
        assertEquals(1, lowPath[2], "expecting a meandering path that doesn't quite go where you might expect");
        assertEquals(0, lowPath[3], "expecting a meandering path that doesn't quite go where you might expect");
        assertEquals(1, lowPath[4], "expecting a meandering path that doesn't quite go where you might expect");

    }
}
