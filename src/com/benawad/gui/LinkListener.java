package com.benawad.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class LinkListener implements ActionListener {

    private String link;

    public LinkListener(String sLink){
        super();
        link = sLink;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // go to this url in the user's default browser
            URL url = new URL(link);

            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(url.toURI());
            }
        } catch (URISyntaxException | IOException e1) {
            e1.printStackTrace();
        }
    }
}
