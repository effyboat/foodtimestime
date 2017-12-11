package com.ahcdesign.foodtimestime.log;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.eclipse.jetty.util.log.AbstractLogger;

public class FttLogger extends AbstractLogger {
    private static Level DEFAULT_LOG_LEVEL = Level.INFO;
    private static Level configuredLevel = DEFAULT_LOG_LEVEL;

    private static Logger _logger = LogManager.getLogger(FttLogger.class.getName());

    public FttLogger() {
        new FttLogger(FttLogger.class.getName());
    }

    public FttLogger(String name) {
        Configurator.setLevel(name, DEFAULT_LOG_LEVEL);
        _logger = LogManager.getLogger(name);
    }

    public void setLoggingLevel(Level level) {
        Configurator.setLevel(_logger.getName(), level);
        this.configuredLevel = level;
    }

    public static Logger getLogger() {
        return _logger;
    }

    public static Logger getRootLogger() {
        return _logger;
    }

    private String format(String msg, Object... args) {
        msg = String.valueOf(msg); // Avoids NPE
        String braces = "{}";
        StringBuilder builder = new StringBuilder();
        int start = 0;
        for (Object arg : args)
        {
            int bracesIndex = msg.indexOf(braces, start);
            if (bracesIndex < 0)
            {
                builder.append(msg.substring(start));
                builder.append(" ");
                builder.append(arg);
                start = msg.length();
            }
            else
            {
                builder.append(msg.substring(start, bracesIndex));
                builder.append(String.valueOf(arg));
                start = bracesIndex + braces.length();
            }
        }
        builder.append(msg.substring(start));
        return builder.toString();
    }

    @Override
    protected FttLogger newLogger(String fullname) {
        return new FttLogger(fullname);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public String getName() {
        return _logger.getName();
    }

    @Override
    public void warn(String msg, Object... args) {
        if (configuredLevel.intLevel() <= Level.WARN.intLevel()) {
            _logger.warn(format(msg, args));
        }
    }

    @Override
    public void warn(Throwable thrown) {
        if (configuredLevel.intLevel() <= Level.WARN.intLevel()) {
            _logger.warn(thrown);
        }
    }

    @Override
    public void warn(String msg, Throwable thrown) {
        if (configuredLevel.intLevel() <= Level.WARN.intLevel()) {
            _logger.warn(msg, thrown);
        }
    }

    @Override
    public void info(String msg, Object... args) {
        if (configuredLevel.intLevel() <= Level.INFO.intLevel()) {
            _logger.info(format(msg, args));
        }
    }

    @Override
    public void info(Throwable thrown) {
        if (configuredLevel.intLevel() <= Level.INFO.intLevel()) {
            _logger.info(thrown);
        }
    }

    @Override
    public void info(String msg, Throwable thrown) {
        if (configuredLevel.intLevel() <= Level.INFO.intLevel()) {
            _logger.info(msg, thrown);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        if (configuredLevel.equals(Level.INFO)) {
            return true;
        }
        return false;
    }

    @Override
    public void setDebugEnabled(boolean enabled) {
        setLoggingLevel(enabled ? Level.DEBUG : Level.INFO);
    }

    @Override
    public void debug(String msg, Object... args) {
        if (configuredLevel.intLevel() <= Level.DEBUG.intLevel()) {
            _logger.debug(format(msg, args));
        }
    }

    @Override
    public void debug(String msg, long arg) {
        if (configuredLevel.intLevel() <= Level.DEBUG.intLevel()) {
            _logger.debug(msg, arg);
        }
    }

    @Override
    public void debug(Throwable thrown) {
        if (configuredLevel.intLevel() <= Level.DEBUG.intLevel()) {
            _logger.debug(thrown);
        }
    }

    @Override
    public void debug(String msg, Throwable thrown) {
        if (configuredLevel.intLevel() <= Level.DEBUG.intLevel()) {
            _logger.debug(msg, thrown);
        }
    }

    @Override
    public void ignore(Throwable ignored) {
        // no-op
    }
}
