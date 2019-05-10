package com.cloudwhale.browsercontroller;

import java.io.IOException;

public class Main {

    // The url of the launcher.
    private static  final String LAUNCHER_URL = "https://cloudwhale.nl/launcher";

    public static void main(String[] args) throws InterruptedException {
        SeleniumBrowserController controller = new SeleniumBrowserController();
        controller.openUrl(LAUNCHER_URL);
        boolean titleMatchesHa = controller.titleMatches("ha!");
        boolean titleMatches2ButtonLauncher = controller.titleMatches("Unity WebGL Player | 2ButtonLauncher");

        System.out.println("Title matches 'ha!' = " + titleMatchesHa);
        System.out.println("Title matches 'Unity WebGL Player | 2ButtonLauncher' = " + titleMatches2ButtonLauncher);

        Thread.sleep(2000);

        controller.closeUrl();
    }

    /*
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
    */


}
