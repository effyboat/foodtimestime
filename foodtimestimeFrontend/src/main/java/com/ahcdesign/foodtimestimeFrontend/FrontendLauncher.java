package com.ahcdesign.foodtimestimeFrontend;

import static spark.Spark.*;

public class FrontendLauncher {
    public static void main(String[] args) {
        staticFileLocation("/react");
        get("/hello", (req, res) -> "Welcome to the Foodtimestime Front End!");
    }
}
