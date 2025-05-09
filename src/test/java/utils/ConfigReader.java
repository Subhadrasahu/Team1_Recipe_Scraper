package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
	public static String readDataFromConfig(String key) throws IOException {
		Properties prop = new Properties();
		FileInputStream file = new FileInputStream("./src/test/resources/config/config.properties");
		prop.load(file);

		return prop.getProperty(key);

	}

}
