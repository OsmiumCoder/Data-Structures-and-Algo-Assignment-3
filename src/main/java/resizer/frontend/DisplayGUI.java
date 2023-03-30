package resizer.frontend;

import resizer.backend.EdgeAvoidanceImageShrinker;
import resizer.middle.ShrinkController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 *
 * Some help setting the image and menu from:
 * https://stackoverflow.com/questions/41497140/displaying-an-image-using-swing-in-java
 */
public class DisplayGUI extends JFrame implements ActionListener {

    private JMenu mFileMenu;
    private JMenuItem mOpenFile;

    private ImageIcon mImg;
    private JLabel mImgLabel;

    private ImageIcon mEnergyImg;
    private JLabel mEnergyImgLabel;

    private JMenuBar parentBar;
    private JButton mShrinkButton;
    private JButton mAnimateButton;
    private JButton mStopButton;

    private JRadioButton prewittOpRadioButton;
    private JRadioButton sobelOpRadioButton;

    private boolean stopAnimation;

    //private EdgeAvoidanceImageShrinker imgShrinker;
    private ShrinkController controller;

    public DisplayGUI() {
        super();
        stopAnimation = false;
        BorderLayout layout = new BorderLayout();

        setLayout(layout);
        setTitle("Image Shrinking");

        add(generateImagePanel(1280, 1024), BorderLayout.CENTER);
        add(configurationPanel(), BorderLayout.SOUTH);

        mFileMenu = makeMenu("File", 'F');
        mOpenFile = makeMenuItem(mFileMenu, "Open...", 'O');

        //setup parentBar
        parentBar = new JMenuBar();
        parentBar.add(mFileMenu);

        setJMenuBar(parentBar);

        controller = new ShrinkController();
        controller.setGradientOperator(EdgeAvoidanceImageShrinker.SOBEL);
    }



    private JPanel generateImagePanel(int width, int height) {
        mImgLabel = new JLabel();
        mImgLabel.setText("open image to start");
        JPanel imgPanel = new JPanel();
        imgPanel.setLayout(new BoxLayout(imgPanel,BoxLayout.X_AXIS));
        imgPanel.add(mImgLabel);

        mEnergyImgLabel = new JLabel();
        imgPanel.add(mEnergyImgLabel);

        return imgPanel;
    }

    private JPanel configurationPanel() {

        mShrinkButton = new JButton("Shrink Image");
        mAnimateButton = new JButton("Continuous Shrinking");
        mStopButton = new JButton("Stop");

        mShrinkButton.setEnabled(false);
        mAnimateButton.setEnabled(false);
        mStopButton.setEnabled(true);

        mShrinkButton.addActionListener(this);
        mAnimateButton.addActionListener(this);
        mStopButton.addActionListener(this);

        sobelOpRadioButton = new JRadioButton("Sobel Operator");
        sobelOpRadioButton.addActionListener(this);
        sobelOpRadioButton.setSelected(true);
        sobelOpRadioButton.setActionCommand(EdgeAvoidanceImageShrinker.SOBEL);
        prewittOpRadioButton = new JRadioButton("Prewitt Operator");
        prewittOpRadioButton.addActionListener(this);
        prewittOpRadioButton.setActionCommand(EdgeAvoidanceImageShrinker.PREWITT);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(sobelOpRadioButton);
        buttonGroup.add(prewittOpRadioButton);

        JPanel config = new JPanel();
        config.add(mShrinkButton);
        config.add(mAnimateButton);
        config.add(mStopButton);
        config.add(sobelOpRadioButton);
        config.add(prewittOpRadioButton);

        return config;
    }

    private JMenu makeMenu(String name, char mnemonic) {
        JMenu menu = new JMenu(name);
        menu.setMnemonic(mnemonic);
        return menu;
    }

    private JMenuItem makeMenuItem(JMenu menu, String name, char mnemonic){
        JMenuItem m = new JMenuItem(name, (int) mnemonic);
        m.addActionListener(this);
        menu.add(m);
        return m;
    }

    private void setImages(BufferedImage image) {
        setImage(image);

        //send the image to the backend
        BufferedImage energyImage = controller.processImage(image);
        setEnergyImg(energyImage);
        toggleButtons();
    }

    private void setImage(BufferedImage bufferedImage) {
        mImg = new ImageIcon(bufferedImage);
        mImgLabel.setIcon(mImg);
        mImgLabel.setText("");
        mImgLabel.setVisible(true);
        mImgLabel.repaint();
        repaint();
        pack();
    }

    private void setEnergyImg(BufferedImage bufferedImage) {
        mEnergyImg = new ImageIcon(bufferedImage);
        mEnergyImgLabel.setIcon(mEnergyImg);
        mEnergyImgLabel.setText("");
        mEnergyImgLabel.setVisible(true);
        mEnergyImgLabel.repaint();
        repaint();
        pack();
    }

    private void toggleButtons() {
        mAnimateButton.setEnabled(true);
        mShrinkButton.setEnabled(true);
    }

    private void toggleAndReClick() {
        mAnimateButton.setEnabled(true);
        mShrinkButton.setEnabled(true);
        if(!stopAnimation)
            mAnimateButton.doClick();
        else {
            stopAnimation = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mOpenFile) {
            String path = null;

            JFileChooser jfc = new JFileChooser();
            jfc.setCurrentDirectory(new File("./SampleImages"));

            int result = jfc.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = jfc.getSelectedFile();
                path = file.getAbsolutePath();
            }
            System.out.println(path);
            try {
                BufferedImage bufferedImage = ImageIO.read(new File(path));
                setImages(bufferedImage);
                toggleButtons();
            }catch(IOException ex){
                System.err.println("error opening file");
                mImgLabel.setText("error opening file");
            }catch(NullPointerException ex) {
                ex.printStackTrace();
                System.err.println("Cancelled image load");
            }
        }
        else if (e.getSource() == mShrinkButton) {
            //send the new image
            mAnimateButton.setEnabled(false);
            mShrinkButton.setEnabled(false);
            controller.shrink(this::setImage, this::setEnergyImg,this::toggleButtons, 500);
        }
        else if(e.getSource() == mAnimateButton) {
            //stopAnimation=false;

            mShrinkButton.setEnabled(false);
            mAnimateButton.setEnabled(false);
            controller.shrink(this::setImage, this::setEnergyImg,this::toggleAndReClick, 500);
        }
        else if (e.getSource() == mStopButton) {
            stopAnimation = true;
        }
        else if (e.getSource() == prewittOpRadioButton || e.getSource() == sobelOpRadioButton) {
            controller.setGradientOperator(e.getActionCommand());
            if(mImg != null) {
                setImages((BufferedImage) mImg.getImage());
            }
        }
    }
}
