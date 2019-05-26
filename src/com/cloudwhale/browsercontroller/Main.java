package com.cloudwhale.browsercontroller;

import java.io.IOException;

public class Main {

    // The url of the launcher.
    private static  final String LAUNCHER_URL = "https://cloudwhale.nl/launcher";

    public static void main(String[] args) throws InterruptedException {
        java.awt.EventQueue.invokeLater(() -> new SeleniumBrowserController(LAUNCHER_URL).setVisible(true));
    }
}
