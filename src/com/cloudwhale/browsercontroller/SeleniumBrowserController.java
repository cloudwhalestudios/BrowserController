package com.cloudwhale.browsercontroller;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.stream.Stream;
//import org.openqa.selenium.edge.EdgeDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumBrowserController {

    WebDriver driver;
    boolean browserOpen;

    public static final String CHROME_DRIVER_LOC = "C:\\chromedriver.exe";
    public static final String GECKO_DRIVER_LOC = "C:\\geckodriver.exe";
    public static final String EDGE_DRIVER_LOC = "C:\\msedgedriver.exe";

    public SeleniumBrowserController() {
        //System.setProperty("webdriver.firefox.marionette", "C:\\geckodriver.exe");
        //driver = new FirefoxDriver();
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_LOC);
        driver = new ChromeDriver();
    }

    public void openUrl(String url) {
        driver.get(url);
        browserOpen = true;
    }

    public boolean titleMatches(String expectedTitle) {
        return browserOpen && driver.getTitle().contentEquals(expectedTitle);
    }

    public void closeUrl() {
        driver.close();
        browserOpen = false;
    }
}
