package com.ahcdesign.foodtimestime;

import com.ahcdesign.foodtimestime.log.FttLogger;
import com.ahcdesign.foodtimestime.utils.DbUtilities;

import static spark.Spark.after;
import static spark.Spark.port;
import static spark.Spark.post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;

public class BackendLauncher {
    public static Logger startupLogger;

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

        startupLogger.info("Finding random meal...");
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
        startupLogger.info("Random meal found!");
        return mealName;
    }

    private static void initializeLogger() {
        startupLogger = FttLogger.getLogger();
    }
}
