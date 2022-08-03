package io.github.dsheirer;
import com.jidesoft.swing.JideTabbedPane;
import io.github.dsheirer.gui.PasswordPanel;
import io.github.dsheirer.gui.SDRTrunk;
import io.github.dsheirer.util.TimeStamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.util.*;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.nio.file.Path;
import java.awt.EventQueue;

public class HealthChecks extends TimerTask  {
    private JTable mNowPlayingTable;
    private JPanel mTunersPanel;
    private Path mScreenshotDirectory;
    private Logger mLog = LoggerFactory.getLogger(HealthChecks.class);

    public HealthChecks(JTable nowPlayingTable, JPanel tunersPanel, Path screenshotDirectory){
        mNowPlayingTable = nowPlayingTable;
        mTunersPanel = tunersPanel;
        mScreenshotDirectory = screenshotDirectory;
    }



    public void run() {
        mLog.debug("running scheduled health checks");
        String filename = TimeStamp.getTimeStamp("_") + "_nowplaying.png";
        Path captureFile = mScreenshotDirectory.resolve(filename);

        try {
            BufferedImage image = ScreenImage.createImage(mNowPlayingTable);
            ScreenImage.writeImage(image, captureFile.toString());
        } catch (IOException ex){
            mLog.error(ex.toString());
        } catch (Exception ex){
            mLog.error(ex.toString());
        }

        filename = TimeStamp.getTimeStamp("_") + "_tuners.png";
        captureFile = mScreenshotDirectory.resolve(filename);

        try {
            BufferedImage image = ScreenImage.createImage(mTunersPanel);
            ScreenImage.writeImage(image, captureFile.toString());
        } catch (IOException ex){
            mLog.error(ex.toString());
        } catch (Exception ex){
            mLog.error(ex.toString());
        }




        /*

        BufferedImage image = new BufferedImage(1000, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        mMainGui.printAll(g);
        g.dispose();

        String filename = TimeStamp.getTimeStamp("_") + "_screen_capture.png";

        final Path captureFile = mScreenshotDirectory.resolve(filename);
*/
//        EventQueue.invokeLater(() -> {
//            try
//            {
//                ImageIO.write(image, "png", captureFile.toFile());
//            }
//            catch(IOException e)
//            {
//                // mLog.error("Couldn't write screen capture to file [" + captureFile.toString() + "]", e);
//            }
//        });

//        try {
//            ImageIO.write(image, "jpg", new File("Paint.jpg"));
//            ImageIO.write(image, "png", new File("Paint.png"));
//        } catch (IOException exp) {
//            exp.printStackTrace();
//        }


        /*
        if (mPasswordGui.autolock()) {
            mPasswordGui.setVisible(false);
            mMainGui.setVisible(true);
        }
        try {
            Thread.sleep(500);
        } catch (Exception ex){

        }

        mScreenCaptureMenuItem.doClick();
        try {
            Thread.sleep(500);
        } catch (Exception ex){

        }

        if (mPasswordGui.autolock()){
            mMainGui.setVisible(false);
            mPasswordGui.setVisible(true);
        }
        */
    }



}
