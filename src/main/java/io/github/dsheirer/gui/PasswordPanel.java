/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.dsheirer.gui;

import io.github.dsheirer.HealthChecks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import java.util.Timer; 
import java.util.TimerTask;

/**
 *
 * @author davidbelle
 */
public class PasswordPanel extends JPanel {
    
    private JPasswordField passwordField;
    private JFrame mMainGui;
    private JFrame mPasswordGui;
    private JButton button;
    private JLabel label;
    
    private JButton showPasswordButton;
    private boolean mAutolock = true;
    private Timer activateTimer;
    private final static Logger mLog = LoggerFactory.getLogger(PasswordPanel.class);
    
    
    
    public PasswordPanel(JFrame mainGui, JFrame passwordGui) {
        Dimension expectedDimension = new Dimension(490, 65);

        this.setPreferredSize(expectedDimension);
        this.setMaximumSize(expectedDimension);
        this.setMinimumSize(expectedDimension);
        
        // this.setBackground(Color.RED); 
        
        this.mMainGui = mainGui;
        this.mPasswordGui = passwordGui;
        
        this.mMainGui.getRootPane().setDefaultButton(button);
        

    
    }

    public boolean autolock() {
        mLog.debug("Auto lock is " + (mAutolock ? " true " : " false "));
        return mAutolock;
    }
    
    public void init()
    {
     this.setLayout(null);
     // setLayout(new MigLayout("insets 0 0 0 0 ", "[grow,fill]", "[]0[grow,fill]0[]"));
     // Box box = new Box(BoxLayout.Y_AXIS); 
     
     showPasswordButton = new JButton("OK");
     showPasswordButton.setBounds(205,10,80,25);
     showPasswordButton.addActionListener(new ShowLoginFields(this));
     this.add(showPasswordButton);
     
     label = new JLabel("Token");
     label.setBounds(25,35,80,25);
     label.setVisible(false);
     this.add(label);
     
     passwordField = new JPasswordField(20);
     passwordField.setBounds(100, 35, 300, 25);
     passwordField.setVisible(false);
     
     passwordField.addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent e) {
          /*
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            button.doClick();
        }
        */
        isPasswordCorrect(false);
      }
    });
     this.add(passwordField);
     
      button = new JButton("Login ");
     button.setBounds(400, 35, 80, 25);
     button.addActionListener(new LoginHandler(this));
     button.setVisible(false);
     this.add(button);
     this.acivateTimer();
     
    }



    public void acivateTimer() {
        activateTimer = new Timer();
        ActivateLockScreen activateTask = new ActivateLockScreen(mMainGui,mPasswordGui,this);
        if (SDRTrunk.lockable) {
            activateTimer.schedule(activateTask, 900000, 900000);  // 900000 = 15 minutes
        }
    }

    public boolean isPasswordCorrect(boolean autoClear) {
        var entered = new String(passwordField.getPassword()).toLowerCase();
        char[] password1Char = {115,100,114,115,100,114,49};
        String password1 = new String(password1Char);
        
        char[] password2Char = {55,51,55,49};
        String password2 = new String(password2Char);

        char[] password3Char = {55,51,55,50,110,111};
        String password3 = new String(password3Char);
        
        
        
        if (password1.equals(entered) || password2.equals(entered) || password3.equals(entered)) {
            this.mPasswordGui.setVisible(false);
            this.mMainGui.setVisible(true);

            passwordField.setText("");
            toggleLoginFields(false);

            if (password3.equals(entered) && activateTimer != null) {
                activateTimer.cancel();
                mAutolock = false;
            }
            return true;
        }
        
        if (autoClear){
            passwordField.setText("");
        }
        return false;
    }
    
    public void toggleLoginFields(boolean show) {
        button.setVisible(show);
        label.setVisible(show);
        passwordField.setVisible(show);
        showPasswordButton.setVisible(!show);
    }
    
    public class LoginHandler implements ActionListener {

        private PasswordPanel mPasswordPanel;
        public LoginHandler(PasswordPanel passwordPanel) {
            mPasswordPanel = passwordPanel;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            mPasswordPanel.isPasswordCorrect(true);
        }
    
        
    }
    
    public class ActivateLockScreen extends TimerTask 
    { 
        private JFrame mMainGui;
        private JFrame mPasswordGui;
        private PasswordPanel mPassowrdPanel;
        public ActivateLockScreen(JFrame mainGui,JFrame passwordGui, PasswordPanel passwordPanel) {
            this.mMainGui = mainGui;
            this.mPasswordGui = passwordGui;
            this.mPassowrdPanel = passwordPanel;
        }
        public void run() 
        { 
            if (mMainGui.isVisible()) {
                mPassowrdPanel.toggleLoginFields(false);
                mMainGui.setVisible(false);
                mPasswordGui.setVisible(true);
            }
        } 
    }
    
    public class ShowLoginFields implements ActionListener {

        private PasswordPanel mPasswordPanel;
        public ShowLoginFields(PasswordPanel passwordPanel) {
            mPasswordPanel = passwordPanel;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            mPasswordPanel.toggleLoginFields(true);
        }
    
        
    }
    
    
}
