package com.ahcdesign.foodtimestime;

import com.ahcdesign.foodtimestime.utils.ConfigUtilities;
import com.ahcdesign.foodtimestime.utils.DbUtilities;
import org.json.simple.JSONObject;

import static spark.Spark.after;
import static spark.Spark.port;
import static spark.Spark.post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BackendLauncher {
    public static Logger rootLogger;

    public static void main(String[] args) {
        initializeLogger();

        port(5678);

        post("/api/v1/random", (request, response) -> {
            return response;
        });
        after((request, response) -> {
            response.type("text/html");
            response.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            response.header("Access-Control-Allow-Headers",
                "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
            response.header("Access-Control-Allow-Origin", "*");
            response.body(fetchRandomMealName());
        });
    }

    private static String fetchRandomMealName() {
        Connection conn;
        String mealName = null;

        try {
            conn = DbUtilities.connect();
            if (conn != null) {
                String prepQuery = "SELECT mName FROM meals ORDER BY RAND() LIMIT 1";
                PreparedStatement preparedStatement = conn.prepareStatement(prepQuery);
                ResultSet result = preparedStatement.executeQuery();
                result.next();
                mealName = result.getString(1);
            }
        } catch (SQLException ex) {
            mealName = "Exception:" + ex.toString();
        }

        return mealName;
    }

    private static void initializeLogger() {
        try {
            JSONObject configRoot = ConfigUtilities.getConfigRoot();
            JSONObject logConfig = (JSONObject) configRoot.get("logs");
            String logPropsPath = (String) logConfig.get("propsFile");
            System.setProperty("log4j.configurationFile", logPropsPath);
        } catch (Exception ignored) {
            // no-op
        }

        rootLogger = LogManager.getRootLogger();
        rootLogger.info("Logging successfully initialized!");
    }
}
