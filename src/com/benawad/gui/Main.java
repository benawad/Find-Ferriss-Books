package com.benawad.gui;

import com.benawad.DownloadRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Main {
    final static String BOOKS_PANEL = "Books";
    final static String DOWNLOAD_PANEL = "Download";
    public static final String API_KEY_URL = "https://developers.google.com/api-client-library/php/guide/aaa_apikeys";
    final static int extraWindowWidth = 100;
    JTextField apiKeyField;
    JButton downloadButton;
    DownloadListener downloadListener;

    private void fillDownloadPanel(JPanel panel) {

        JButton apiLabel = new JButton();
        apiLabel.setText("<HTML><FONT color=\"#000099\"><U>Google Books API key:</U></FONT></HTML>");
        //style button to look like a link
        apiLabel.setBorderPainted(false);
        apiLabel.setOpaque(false);
        apiLabel.setBackground(Color.WHITE);
        apiLabel.setToolTipText(API_KEY_URL);
        apiLabel.addActionListener(new LinkListener());


        apiKeyField = new JTextField();
        apiKeyField.setPreferredSize(new Dimension(225, 25));

        downloadButton = new JButton("Start Download");
        downloadListener = new DownloadListener();
        downloadButton.addActionListener(downloadListener);

        panel.add(apiLabel);
        panel.add(apiKeyField);
        panel.add(downloadButton);
    }

    class DownloadListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            downloadButton.setText("Loading...");
            System.out.println("downloading...");

            Runnable threadJob = new DownloadRunner(downloadButton, apiKeyField.getText());
            Thread downloadThread = new Thread(threadJob);
            downloadThread.start();

        }
    }

    class LinkListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                //go to this url in the user's default browser
                URL url = new URL(API_KEY_URL);

                Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if(desktop != null && desktop.isSupported(Desktop.Action.BROWSE)){
                    desktop.browse(url.toURI());
                }
            } catch (URISyntaxException | IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void addComponentToPane(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();

        //Create the "cards".
        JPanel card1 = new JPanel() {
            //Make the panel wider than it really needs, so
            //the window's wide enough for the tabs to stay
            //in one row.
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                return size;
            }
        };
        card1.add(new JButton("Button 1"));
        card1.add(new JButton("Button 2"));
        card1.add(new JButton("Button 3"));

        JPanel card2 = new JPanel();
        fillDownloadPanel(card2);

        tabbedPane.addTab(BOOKS_PANEL, card1);
        tabbedPane.addTab(DOWNLOAD_PANEL, card2);

        pane.add(tabbedPane, BorderLayout.CENTER);
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Find Ferriss Books");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        Main demo = new Main();
        demo.addComponentToPane(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
       createAndShowGUI();
    }
}