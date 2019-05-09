package com.cloudwhale.browsercontroller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;


// Reference from https://www.java-tips.org/java-se-tips-100019/88888889-java-util/40-opening-default-browser.html
class BrowserController {

    // Used to identify the windows platform.
    private static final String WIN_ID = "Windows";
    // The default system browser under windows.
    private static final String WIN_PATH = "rundll32";
    // The flag to display a url.
    private static final String WIN_FLAG = "url.dll,FileProtocolHandler";
    // The default browser under unix.
    private static final String UNIX_PATH = "netscape";
    // The flag to display a url.
    private static final String UNIX_FLAG = "-remote openURL";

    private boolean debugEnabled;

    String osName;
    boolean isWindows;

    String browserApplicationPath;
    Process browserProcess;

    BrowserController(boolean enableDebugging) {
        debugEnabled = enableDebugging;

        identifyOperatingSystem();
        identifyDefaultBrowserApplication();
    }

    private void identifyOperatingSystem() {
        osName = System.getProperty("os.name");
        isWindows = osName != null && osName.startsWith(WIN_ID);

        if (debugEnabled) System.out.println("Identified system name as " + osName);
        if (debugEnabled) System.out.println("Windows? " + isWindows);
    }

    private void identifyDefaultBrowserApplication() {

    }

    // see https://stackoverflow.com/a/54950/11460333
    String getProcessList() {
        StringBuilder processList = new StringBuilder();
        if (debugEnabled) System.out.println("Getting process list...");
        try {
            String line;
            Process p;
            if (isWindows) {
                p = Runtime.getRuntime().exec
                        (System.getenv("windir") +"\\system32\\"+"tasklist.exe");
            }
            else {
                p = Runtime.getRuntime().exec("ps -e");
            }
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                processList.append(line).append("\n");
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }

        if (debugEnabled) System.out.println(processList);

        return processList.toString();
    }

    // Lists all folders inside of the users home directory
    void testRuntime() throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder();
        if (isWindows) {
            builder.command("cmd.exe", "/c", "dir");
        } else {
            builder.command("sh", "-c", "ls");
        }
        builder.directory(new File(System.getProperty("user.home")));
        Process process = builder.start();
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        assert exitCode == 0;
    }

    void openBrowser(String url) {
        String cmd = null;
        try {
            if (isWindows) {
                // cmd = 'rundll32 url.dll,FileProtocolHandler http://...'
                cmd = WIN_PATH + " " + WIN_FLAG + " " + url;
                browserProcess = Runtime.getRuntime().exec(cmd);
                if (debugEnabled) System.out.println("Open cmd: " + cmd);

            } else {
                // Under Unix, Netscape has to be running for the "-remote"
                // command to work.  So, we try sending the command and
                // check for an exit value.  If the exit command is 0,
                // it worked, otherwise we need to start the browser.
                // cmd = 'netscape -remote openURL(http://www.java-tips.org)'
                cmd = UNIX_PATH + " " + UNIX_FLAG + "(" + url + ")";
                browserProcess = Runtime.getRuntime().exec(cmd);
                try {
                    // wait for exit code -- if it's 0, command worked,
                    // otherwise we need to start the browser up.
                    int exitCode = browserProcess.waitFor();
                    if (exitCode != 0) {
                        // Command failed, start up the browser
                        // cmd = 'netscape http://www.java-tips.org'
                        cmd = UNIX_PATH + " "  + url;
                        browserProcess = Runtime.getRuntime().exec(cmd);
                    }
                } catch(InterruptedException x) {
                    System.err.println("Error bringing up browser, cmd='" +
                            cmd + "'");
                    System.err.println("Caught: " + x);
                }
            }

            if (debugEnabled && browserProcess != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(browserProcess.getInputStream()));

                StringBuilder inputStreamString = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    inputStreamString.append(line).append("\n");
                }

                System.out.println(inputStreamString);
            }
        } catch(IOException x) {
            // couldn't exec browser
            System.err.println("Could not invoke browser, command=" + cmd);
            System.err.println("Caught: " + x);
        }
    }

    void closeBrowser() {
        try {
            browserProcess.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (debugEnabled) System.out.println("Exit value: " + browserProcess.exitValue());
    }
}
