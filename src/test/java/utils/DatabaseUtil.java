package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DatabaseUtil {
    private static Connection connection;

    // Connect to PostgreSQL using properties file
    public static void connect() {
        try {
            // Load database configuration from properties file
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("src/test/resources/config/config.properties");
            props.load(fis);

            String url = props.getProperty("postgresUrl");
            String username = props.getProperty("postgresUser");
            String password = props.getProperty("postgresPassword");

            // Establish the connection
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to PostgreSQL DB!");
        } catch (SQLException e) {
            System.err.println("Failed to connect to DB: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + e.getMessage());
        }
    }

    // Insert recipe data
    public static void insertRecipe(int recipeId, String recipeName, String recipeCategory, String foodCategory, 
                                    String ingredients, String preparationTime, String cookingTime, String tag, 
                                    int noOfServings, String cuisineCategory, String recipeDescription, 
                                    String preparationMethod, String nutrientValues, String recipeUrl) {

        String query = "INSERT INTO recipes (Recipe_ID, Recipe_Name, Recipe_Category, Food_Category, Ingredients, " +
                       "Preparation_Time, Cooking_Time, Tag, No_of_servings, Cuisine_category, Recipe_Description, " +
                       "Preparation_method, Nutrient_values, Recipe_URL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, recipeId);
            pstmt.setString(2, recipeName);
            pstmt.setString(3, recipeCategory);
            pstmt.setString(4, foodCategory);
            pstmt.setString(5, ingredients);
            pstmt.setString(6, preparationTime);
            pstmt.setString(7, cookingTime);
            pstmt.setString(8, tag);
            pstmt.setInt(9, noOfServings);
            pstmt.setString(10, cuisineCategory);
            pstmt.setString(11, recipeDescription);
            pstmt.setString(12, preparationMethod);
            pstmt.setString(13, nutrientValues);
            pstmt.setString(14, recipeUrl);

            pstmt.executeUpdate();
            System.out.println("Recipe inserted into DB successfully!");
        } catch (SQLException e) {
            System.err.println("Error inserting data: " + e.getMessage());
        }
    }

    // Close the connection
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
