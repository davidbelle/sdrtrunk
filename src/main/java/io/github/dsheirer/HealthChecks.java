package io.github.dsheirer;
import io.github.dsheirer.gui.PasswordPanel;

import javax.swing.*;
import java.util.*;

public class HealthChecks extends TimerTask  {
    private JMenuItem mScreenCaptureMenuItem;
    private JFrame mPasswordGui;
    private JFrame mMainGui;
    public HealthChecks(JMenuItem screenCaptureMenuItem, JFrame mainGui, JFrame passwordPanel){
        mScreenCaptureMenuItem = screenCaptureMenuItem;
        mPasswordGui = passwordPanel;
        mMainGui = mainGui;
    }

    public void run() {
        mPasswordGui.setVisible(false);
        mMainGui.setVisible(true);
        try {
            Thread.sleep(1000);
        } catch (Exception ex){

        }

        mScreenCaptureMenuItem.doClick();
        try {
            Thread.sleep(1000);
        } catch (Exception ex){

        }
        mMainGui.setVisible(false);
        mPasswordGui.setVisible(true);

        System.out.println("Running");
    }
}
