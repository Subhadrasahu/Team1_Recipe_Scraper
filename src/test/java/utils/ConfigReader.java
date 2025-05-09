package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties = new Properties();

    public static void loadConfig() {
        try (FileInputStream fis = new FileInputStream("src/test/resources/config/config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Failed to load configuration file.");
            e.printStackTrace();
            throw new RuntimeException("Could not load config.properties file.");
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("Missing configuration for key: " + key);
        }
        return value.trim();
    }
}
