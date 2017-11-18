package com.ahcdesign.foodtimestime.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConfigUtilities {
    public static String CONFIG_KEY = "config";
    public static String DB_CONFIG_KEY = "db";
    public static String HOSTNAME_KEY = "hostName";
    public static String PORT_KEY = "port";
    public static String DB_NAME_KEY = "databaseName";
    public static String USERNAME_KEY = "userName";
    public static String PASSOWRD_KEY = "password";

    public static final String getConfigString() throws ConfigFileNotFoundException, IOException {
        try {
            String line;
            StringBuilder sb = new StringBuilder();

            ClassLoader classLoader = ConfigUtilities.class.getClassLoader();
            File file = new File(classLoader.getResource("configure/config.json").getFile());

            InputStreamReader configFileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(configFileReader);

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            return sb.toString();
        } catch (FileNotFoundException fnfe) {
            // If we can't find the config file, make a new one, and return the clean copy since they'll be identical
            makeNewConfig();
            return getConfigString();
        } catch (IOException ioex) {
            throw new IOException(ioex);
        }
    }

    public static final JSONObject getConfigRoot() throws ConfigFileNotFoundException, IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject rootJson = (JSONObject) jsonParser.parse(ConfigUtilities.getConfigString());
        return (JSONObject) rootJson.get(CONFIG_KEY);
    }

    public static final String getCleanConfigString() throws ConfigFileNotFoundException, IOException {
        try {
            String line;
            StringBuilder sb = new StringBuilder();

            ClassLoader classLoader = ConfigUtilities.class.getClassLoader();
            File file = new File(classLoader.getResource("configure/.config_clean").getFile());

            InputStreamReader configFileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(configFileReader);

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            return sb.toString();
        } catch (FileNotFoundException fnfe) {
            throw new ConfigFileNotFoundException("Config file not found.", fnfe.getCause());
        } catch (IOException ioex) {
            throw new IOException(ioex);
        }
    }

    /**
     * Creates a config.json file in the appropriate directory by copying the .config_clean file from the same dir.
     * Called when no config file is found. Does not check for an existing config file.
     * Throws an exception if no .config_clean is found
     */
    public static void makeNewConfig() throws ConfigFileNotFoundException, IOException {
        ClassLoader classLoader = ConfigUtilities.class.getClassLoader();
        File cleanConfig = new File(classLoader.getResource("configure/.config_clean").getFile());
        if (cleanConfig.exists()) {
            try {
                String cleanString = getCleanConfigString();
                byte data[] = cleanString.getBytes();
                FileOutputStream out = new FileOutputStream("config.json");
                out.write(data);
                out.close();
            } catch (FileNotFoundException ignored) {
                throw new ConfigFileNotFoundException("Unknkown error while creating new config file.");
            } catch (IOException ioex) {
                throw new IOException(ioex);
            }
        } else {
            throw new ConfigFileNotFoundException("Unable to make a new config file. No clean copy of the config file "
                    + "could be found.");
        }
    }

    protected static class ConfigFileNotFoundException extends Exception {

        ConfigFileNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }

        ConfigFileNotFoundException(String message) {
            new ConfigFileNotFoundException(message, null);
        }
    }
}
