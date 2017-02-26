// Global configuration variables.
package me.lsdo.processing;

import java.util.*;
import java.io.*;



public class Config {

    private static class ConfigInstance {
        public static Config config = new Config();
    }

    public static Config getConfig() {
        return ConfigInstance.config;
    }

    private Config()
    {
        Properties prop = new Properties();
        InputStream input = null;

        // defaults
        OpcHostname = "127.0.0.1";
        OpcPort = 7890;

        try {

            String workingDir = System.getProperty("user.dir");
            System.out.println("Looking for config.properties in: " + workingDir);

            File file = new File("config.properties");
            if (file.isFile()) {

                input = new FileInputStream("config.properties");

                // load a properties file
                prop.load(input);

                // get the property value and print it out
                if (prop.containsKey("opchostname"))
                    OpcHostname = prop.getProperty("opchostname");
                if (prop.containsKey("opcport"))
                    OpcPort = Integer.parseInt(prop.getProperty("opcport"));
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Debug mode.
    static final boolean DEBUG = false;

    public String OpcHostname;
    public int OpcPort;

    // Size of single panel's pixel grid.
    static final int PANEL_SIZE = 15;

}
