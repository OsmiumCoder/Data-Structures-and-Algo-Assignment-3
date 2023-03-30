package resizer.middle;

import resizer.backend.EdgeAvoidanceImageShrinker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;


/**
 * facilitate communication between the front and backends
 */
public class ShrinkController {

    //the backend
    EdgeAvoidanceImageShrinker imgShrinker;

    //storage for our images
    BufferedImage rgbImage;
    BufferedImage grayImage;

    /**
     * public constructor
     */
    public ShrinkController() {
        imgShrinker = new EdgeAvoidanceImageShrinker();
    }

    /**
     * Process an image by converting it to a luminance image and then
     * sending to the backend to apply the gradient to convert to an energy image
     * @param img the image to process
     * @return the energy image
     */
    public BufferedImage processImage(BufferedImage img) {

        rgbImage = img;
        //this step is not required each iteration - just need to remove the red line
        //in the array. But leave as is for simplicity
        int[][] luminanceImg = ImageHelper.convertToGray(rgbImage);
        imgShrinker.setImage(luminanceImg);

        int[][] engImg = imgShrinker.getEnergyImage();

        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 6; j++) {

            }
        }
        grayImage = ImageHelper.imageFrom2DGrayArray(engImg);

        return grayImage;
    }

    /**
     *
     * @param rgbImageUpdater the function to call to update the GUI to show the RGB image
     * @param grayImageUpdater the function to call to update the GUI to show the energy image
     * @param doneToggle function to update the buttons
     * @param milliseconds length in time to delay before updating with the shrunk image
     */
    public void shrink(Consumer<BufferedImage> rgbImageUpdater, Consumer<BufferedImage> grayImageUpdater, Runnable doneToggle, int milliseconds) {

        //retrieve the path of the red line
        int [] redLine = imgShrinker.lowEnergyPath();

        //add it to the image
        ImageHelper.addRedLine(redLine, rgbImage);
        ImageHelper.addRedLine(redLine, grayImage);

        rgbImageUpdater.accept(rgbImage);
        grayImageUpdater.accept(grayImage);

        //remove the red line

        //use a java timer
        //https://docs.oracle.com/javase/tutorial/uiswing/misc/timer.html
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                rgbImage = ImageHelper.removeAndShrink(rgbImage,redLine);
                grayImage = ImageHelper.removeAndShrink(grayImage,redLine);
                rgbImageUpdater.accept(rgbImage);
                grayImageUpdater.accept(grayImage);
                processImage(rgbImage);
                doneToggle.run();
            }
        };
        Timer timer = new Timer(milliseconds, taskPerformer);
        timer.setRepeats(false);
        timer.start();

        //after removing the red line we should re-enable the shrink button
    }


    /**
     * Inform the backend to use a new gradient operator
     * @param operator the new operator to use, e.g., sobel or prewitt
     */
    public void setGradientOperator(String operator) {
        imgShrinker.setGradientOperator(operator);
    }

}
