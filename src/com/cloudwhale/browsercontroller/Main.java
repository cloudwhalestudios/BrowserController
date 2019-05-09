package com.cloudwhale.browsercontroller;

import java.io.IOException;

public class Main {

    // The url of the launcher.
    private static  final String LAUNCHER_URL = "https://cloudwhale.nl/launcher";

    public static void main(String[] args) throws InterruptedException {
        BrowserController controller = new BrowserController(true);
        controller.getProcessList();

        try {
            controller.testRuntime();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        controller.openBrowser(LAUNCHER_URL);

        Thread.sleep(20000);

        controller.closeBrowser();
    }
}
