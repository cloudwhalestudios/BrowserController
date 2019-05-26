package com.cloudwhale.browsercontroller;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

class SeleniumBrowserController extends JFrame {

    private Boolean manualControl = false;

    private WebDriver driver;
    private JLabel label;

    private String url;

    private int width = 800;
    private int height = 400;

    private String currentTitle = "";
    private String exitTitleCondition = "Exiting";

    private static final String CHROME_DRIVER_LOC = "C:\\chromedriver.exe";
    private static final String GECKO_DRIVER_LOC = "C:\\geckodriver.exe";
    private static final String EDGE_DRIVER_LOC = "C:\\MicrosoftWebDriver.exe";

    SeleniumBrowserController(String targetUrl) {
        super("Cloudwhale Controller");

        url = targetUrl;

        // Configure window
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - width) / 2, (screenSize.height - height) / 2, width, height);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Attach closing event handle
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                quitApplication();
            }
        });

        // Open button
        JButton jButton1 = new javax.swing.JButton();

        // Create panel with label
        label = new JLabel("");
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(jButton1);

        add(panel, BorderLayout.CENTER);
        add(label, BorderLayout.SOUTH);

        // Configure launch button
        jButton1.setEnabled(manualControl);
        jButton1.setText("Open '"+ url + "'");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                label.setText("Loading browser. Please wait..");

                java.util.Timer t = new java.util.Timer();
                t.schedule(new java.util.TimerTask() {

                    @Override
                    public void run() {
                        openBrowserAndWait();
                    }
                }, 10);
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                if (!manualControl) {
                    label.setText("Loading browser. Please wait..");
                    openBrowserAndWait();
                }
            }
        });
    }

    private void openBrowserAndWait() {
        System.setProperty("webdriver.gecko.driver", GECKO_DRIVER_LOC);
        driver = new FirefoxDriver();

        // Maximize window
        driver.manage().window().maximize();

        String baseUrl = url;
        driver.get(baseUrl);

        java.util.Timer monitorTimer = new java.util.Timer();
        monitorTimer.schedule(new java.util.TimerTask() {

            @Override
            public void run() {
                while (true) {
                    checkDriver();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }, 10);
    }

    private void checkDriver() {
        if (driver == null) {
            return;
        }

        boolean shouldExit = false;

        try {
            // Try to set the panels title to current browser title
            currentTitle = driver.getTitle();
            label.setText(currentTitle);

            if (exitConditionIsMet()) {
                shouldExit = true;
            }

        } catch (NoSuchWindowException e) {
            System.out.println("Browser has been closed. Exiting Program");
            shouldExit = true;
        } catch (Exception e) {
            System.out.println("Browser has been closed. Exiting Program");
            shouldExit = true;
        }

        if (shouldExit) {
            this.quitApplication();
        }
    }

    private Boolean exitConditionIsMet() {
        System.out.println("Current title '" + currentTitle + "' contains exit condition '" + exitTitleCondition + "'?\n" +
                currentTitle.contains(exitTitleCondition));
        return currentTitle.contains(exitTitleCondition);
    }

    private void quitApplication() {
        // attempt to close gracefully
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {

            }
        }

        System.exit(0);
    }
}
