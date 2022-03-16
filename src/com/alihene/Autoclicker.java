package com.alihene;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.logging.*;

@SuppressWarnings({"deprecation"})
public class Autoclicker implements NativeKeyListener, ActionListener, Runnable {

    Robot clicker;
    Thread thread;
    boolean shouldClick = false;
    JFrame frame;
    JPanel delayPanel;
    JPanel typeOfClickPanel;
    JPanel hotkeyPanel;
    volatile double delayInMs;
    int hotkey;
    String hotkeyInText;
    JTextField setDelayField;
    JButton setDelayButton;
    JRadioButton leftClickButton;
    JRadioButton rightClickButton;
    ButtonGroup typeOfClickButtonGroup;
    JLabel currentHotkey;
    JButton setHotkeyButton;
    TypeOfClick typeOfClick;
    boolean isSelectingHotkey = false;

    Autoclicker() {
        hotkey = 65;
        try {
            clicker = new Robot();
        } catch (Exception e) {
            e.printStackTrace();
        }
        thread = new Thread(this);
        thread.start();

        GridBagConstraints constraints = new GridBagConstraints();

        frame = new JFrame("Java autoclicker by Alihene222");
        currentHotkey = new JLabel("Current hotkey: F7");
        setHotkeyButton = new JButton("Set Hotkey");
        delayPanel = new JPanel();
        leftClickButton = new JRadioButton("Left Click");
        rightClickButton = new JRadioButton("Right Click");
        typeOfClickPanel = new JPanel();
        hotkeyPanel = new JPanel();
        typeOfClickButtonGroup = new ButtonGroup();
        typeOfClickButtonGroup.add(leftClickButton);
        typeOfClickButtonGroup.add(rightClickButton);
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        delayPanel.setBounds(17, 0, 350, 50);
        typeOfClickPanel.setBounds(42, 40, 300, 40);
        hotkeyPanel.setBounds(0, 60, 400, 100);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.gray);
        delayPanel.setBackground(Color.gray);
        typeOfClickPanel.setBackground(Color.gray);
        leftClickButton.setBackground(Color.gray);
        leftClickButton.setForeground(Color.white);
        rightClickButton.setBackground(Color.gray);
        rightClickButton.setForeground(Color.white);
        hotkeyPanel.setBackground(Color.gray);
        frame.add(delayPanel);
        frame.add(typeOfClickPanel);
        frame.add(hotkeyPanel);
        delayPanel.setLayout(new GridBagLayout());
        typeOfClickPanel.setLayout(new GridBagLayout());
        constraints.gridx = 0;
        constraints.gridy = 0;
        setDelayField = new JTextField(15);
        delayPanel.add(setDelayField, constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 5, 0, 0);
        setDelayButton = new JButton("Set Delay In Milliseconds");
        delayPanel.add(setDelayButton, constraints);
        typeOfClickPanel.add(leftClickButton);
        typeOfClickPanel.add(rightClickButton);
        hotkeyPanel.setLayout(new GridBagLayout());
        constraints.gridx = 0;
        constraints.gridy = 0;
        hotkeyPanel.add(setHotkeyButton, constraints);
        currentHotkey.setForeground(Color.white);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 0,0,0);
        hotkeyPanel.add(currentHotkey, constraints);
        frame.setVisible(true);

        setDelayButton.addActionListener(e -> {
            try {
                if (Double.parseDouble(setDelayField.getText()) != 0 && setDelayField.getText() != null) {
                    delayInMs = Double.parseDouble(setDelayField.getText());
                }
            } catch (NumberFormatException ne){
                delayInMs = 0;
            }
        });
        leftClickButton.addActionListener(e -> typeOfClick = TypeOfClick.LEFT);
        rightClickButton.addActionListener(e -> typeOfClick = TypeOfClick.RIGHT);
        setHotkeyButton.addActionListener(e -> {
            isSelectingHotkey = true;
            currentHotkey.setText("Press a button...");
        });
    }

    enum TypeOfClick {
        LEFT,
        RIGHT
    }

    public static void main(String[] args){
        LogManager.getLogManager().reset();
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        Autoclicker autoclicker = new Autoclicker();
        try{
            GlobalScreen.registerNativeHook();
        } catch (Exception e){
            e.printStackTrace();
        }
        GlobalScreen.addNativeKeyListener(autoclicker);
    }

    private void Click(double delay, TypeOfClick typeOfClick){
        if (shouldClick) {
            switch (typeOfClick) {
                case LEFT -> {
                    clicker.mousePress(InputEvent.BUTTON1_MASK);
                    clicker.mouseRelease(InputEvent.BUTTON1_MASK);
                    clicker.delay((int) Math.ceil(delay));
                }
                case RIGHT -> {
                    clicker.mousePress(InputEvent.BUTTON3_MASK);
                    clicker.mouseRelease(InputEvent.BUTTON3_MASK);
                    clicker.delay((int) Math.ceil(delay));
                }
            }
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        int code = nativeKeyEvent.getKeyCode();
        if(!isSelectingHotkey) {
            if (code == hotkey) {
                if (delayInMs != 0 && typeOfClick != null) {
                    shouldClick = !shouldClick;
                }
            }
        } else {
            hotkey = code;
            hotkeyInText = NativeKeyEvent.getKeyText(code);
            currentHotkey.setText("Current hotkey: " + hotkeyInText);
            isSelectingHotkey = false;
        }
    }

    @Override
    public void run() {
        while (true) {
            if (delayInMs != 0) {
                Click(delayInMs, typeOfClick);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {}
    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {}
}
