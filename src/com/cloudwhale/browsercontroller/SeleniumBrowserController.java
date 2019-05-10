package com.cloudwhale.browsercontroller;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumBrowserController {

    WebDriver driver;
    boolean browserOpen;

    public static final String CHROME_DRIVER_LOC = "C:\\chromedriver.exe";
    public static final String GECKO_DRIVER_LOC = "C:\\geckodriver.exe";
    public static final String EDGE_DRIVER_LOC = "C:\\MicrosoftWebDriver.exe"; //"C:\\msedgedriver.exe";

    public SeleniumBrowserController() {
        /*System.setProperty("webdriver.firefox.marionette", GECKO_DRIVER_LOC);
        driver = new FirefoxDriver();
         */
        /*System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_LOC);
        driver = new ChromeDriver();
         */

        System.setProperty("webdriver.edge.driver", EDGE_DRIVER_LOC);
        driver = new EdgeDriver();
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

    public void quit() {
        driver.quit();
        browserOpen = false;
    }
}
