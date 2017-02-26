// Global configuration variables.
package me.lsdo.processing;

import java.util.*;
import java.io.*;



public class Config {

    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final int DEFAULT_PORT = 7890;
    public static final int DEFAULT_PANELS = 24;
    
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
		if (prop.containsKey("num_panels"))
                    numPanels = Integer.parseInt(prop.getProperty("num_panels"));
            } else {
		System.out.println("No properties file found");
	    }

        } catch (IOException ex) {
	    System.out.println("Error reading properties");
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

    public String OpcHostname = DEFAULT_HOST;
    public int OpcPort = DEFAULT_PORT;
    public int numPanels = DEFAULT_PANELS;

    // Size of single panel's pixel grid.
    static final int PANEL_SIZE = 15;

}
