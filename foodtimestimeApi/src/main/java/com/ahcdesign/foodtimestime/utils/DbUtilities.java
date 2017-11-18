package com.ahcdesign.foodtimestime.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtilities {
    public static boolean configRead = false;

    private static String host;
    private static Long port;
    private static String dbName;
    private static String user;
    private static String password;
    private static String connectUrl;

    private static final String CONNECTION_STRING_FORMAT = "jdbc:mysql://%s:%d/%s?user=%s&password=%s&useSSL=false";

    /**
     * Connects to the database specified in configure.json
     */
    public static Connection connect() {
        try {
            if (!configRead) {
                JSONObject configJson = ConfigUtilities.getConfigRoot();
                JSONObject dbConfigJson = (JSONObject) configJson.get(ConfigUtilities.DB_CONFIG_KEY);

                host = (String) dbConfigJson.get(ConfigUtilities.HOSTNAME_KEY);
                port = (Long) dbConfigJson.get(ConfigUtilities.PORT_KEY);
                dbName = (String) dbConfigJson.get(ConfigUtilities.DB_NAME_KEY);
                user = (String) dbConfigJson.get(ConfigUtilities.USERNAME_KEY);
                password = (String) dbConfigJson.get(ConfigUtilities.PASSOWRD_KEY);
                connectUrl = buildJdbcConnectionString(host, port, dbName, user, password);
                // This lets me know I've cached the config settings I need, so I don't need
                // to read the file every time something calls connect()
                configRead = true;
            }

            Connection connection = DriverManager.getConnection(connectUrl);
            connection.setSchema("foodtimestime");

            return connection;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (ConfigUtilities.ConfigFileNotFoundException cfnfe) {
            System.out.println("No config file found. Please ensure a file named config.json is in your 'configure' "
                    + "directory, or create a new one by copying config_clean.json.");
            if (cfnfe.getCause() != null) {
                cfnfe.getCause().printStackTrace();
            }
        } catch (IOException ioex) {
            System.out.println("IOException while reading config file.");
            if (ioex.getCause() != null) {
                ioex.getCause().printStackTrace();
            }
        } catch (ParseException pex) {
            System.out.println("Unable to parse config.json.");
            if (pex.getCause() != null) {
                pex.getCause().printStackTrace();
            }
        }
        return null;
    }

    public static String buildJdbcConnectionString(String host, Long port, String schema, String username, String password) {
        return String.format(CONNECTION_STRING_FORMAT, host, port, schema, username, password);
    }
}
