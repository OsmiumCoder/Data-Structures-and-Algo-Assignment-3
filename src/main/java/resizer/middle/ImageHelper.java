package resizer.middle;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageHelper {

    public static void addRedLine(int [] line, BufferedImage image) {

        //some debugs to help if there are errors in the line array
        for (int row = 0; row < image.getHeight(); row++) {
            try {
                assert(line[row] < image.getWidth());
                if (line[row] >= image.getWidth()) {
                    System.out.println("error the column to add the red line is out of bounds");
                    System.out.println(image.getWidth());
                    System.out.println("row:" + row);
                    System.out.println("col:" + line[row]);
                }
                if(row >= image.getHeight()) {
                    System.out.println("error the row to add the red line is out of bounds");
                    System.out.println(image.getHeight());
                    System.out.println("row:" + row);
                    System.out.println("col:" + line[row]);
                }
                image.setRGB(line[row], row, Color.RED.getRGB());
            }catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                break;
            }
        }
    }

    public static BufferedImage imageFrom2DGrayArray(int[][] arr) {
        int width = arr.length;
        int height = arr[0].length;

        BufferedImage bImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = (int)arr[x][y]<<16 | (int)arr[x][y] << 8 | (int)arr[x][y];
                bImg.setRGB(x,y,pixel);
            }
        }
        return bImg;
    }

    public static BufferedImage removeAndShrink(BufferedImage img, int[] lineForRemoval) {
        int width = img.getWidth()-1;
        int height = img.getHeight();

        BufferedImage smallerImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int skipColumn = lineForRemoval[y];
                int updateX = x;
                if (x > skipColumn)
                    updateX = x-1;
                if (x != skipColumn) {
                    smallerImg.setRGB(updateX, y, img.getRGB(x,y));
                }
            }
        }
        return smallerImg;
    }

    /**
     * inspiration from https://introcs.cs.princeton.edu/java/31datatype/Luminance.java.html
     * @param img
     * @return
     */
    public static int[][] convertToGray(BufferedImage img) {

        int[][] grayImg = new int[img.getWidth()][img.getHeight()];

        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                Color c =  new Color(img.getRGB(x,y));
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();

                double lum = 0.299*r + 0.587*g + 0.114*b;

                int gray = (int) Math.round(lum);
                grayImg[x][y] = gray;//new Color(gray,gray,gray).getRGB();
            }
        }
        return grayImg;
    }

}
