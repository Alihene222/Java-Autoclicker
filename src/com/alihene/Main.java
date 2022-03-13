package com.alihene;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main implements NativeKeyListener, ActionListener {

    static final Autoclick autoclick = new Autoclick();
    static final AutoclickManager autoclickManager = new AutoclickManager();
    static final Thread AutoClickThread = new Thread(autoclick);
    static final Thread AutoClickManagerThread = new Thread(autoclickManager);

    static volatile boolean ToggleClick = false;
    volatile int currentKeyCode = NativeKeyEvent.VC_F7;
    final JFrame Frame;
    final JTextField DelayTextField = new JTextField(15);
    final JRadioButton LeftClickRadioButton = new JRadioButton("Left click");
    final JRadioButton RightClickRadioButton = new JRadioButton("Right click");
    final JLabel DelayTextFieldLabel = new JLabel("Delay in milliseconds");
    final JButton SetDelayButton = new JButton("Set Delay");
    final JPanel Panel = new JPanel();
    final Color gray = new Color(46, 46, 46);
    final ButtonGroup buttonGroup = new ButtonGroup();

    final GridBagConstraints gbc = new GridBagConstraints();

    Main() {
        //Builds the gui and action listeners
        buttonGroup.add(LeftClickRadioButton);
        buttonGroup.add(RightClickRadioButton);
        DelayTextFieldLabel.setForeground(Color.WHITE);
        Frame = new JFrame("Java autoclicker by Alihene222");
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.setSize(500, 500);
        Frame.setLayout(null);
        Frame.setLocationRelativeTo(null);
        Frame.setResizable(false);
        Frame.setVisible(true);
        Frame.getContentPane().setBackground(gray);
        LeftClickRadioButton.setBackground(gray);
        RightClickRadioButton.setBackground(gray);
        LeftClickRadioButton.setForeground(Color.WHITE);
        RightClickRadioButton.setForeground(Color.WHITE);
        Panel.setBounds(40, -50, 400, 250);
        Panel.setBackground(gray);
        Frame.add(Panel);
        Panel.setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 5);
        Panel.add(DelayTextField, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 10);
        Panel.add(DelayTextFieldLabel, gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        Panel.add(SetDelayButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 20);
        Panel.add(LeftClickRadioButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        Panel.add(RightClickRadioButton, gbc);
        SetDelayButton.addActionListener(e -> AutoclickManager.MillisDelay = Double.parseDouble(DelayTextField.getText()));
        LeftClickRadioButton.addActionListener(e -> AutoclickManager.TypeOfClick = 1);
        RightClickRadioButton.addActionListener(e -> AutoclickManager.TypeOfClick = 2);

        //Gets rid of the unnecessary JNativeHook logs
        LogManager.getLogManager().reset();
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
    }

    public static void main(String[] args) {
        //Starts the other threads
        AutoClickThread.start();
        AutoClickManagerThread.start();

        Main main = new Main();
        try{
            GlobalScreen.registerNativeHook();
        } catch (Exception e){
            e.printStackTrace();
        }
        GlobalScreen.addNativeKeyListener(main);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        int code = nativeKeyEvent.getKeyCode();
        if (code == currentKeyCode) {
            if(!(AutoclickManager.MillisDelay == 0)) ToggleClick = !ToggleClick;
        }
    }

    //Unneeded implemented methods
    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {}
    @Override
    public void actionPerformed(ActionEvent e) {}
    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {}
}