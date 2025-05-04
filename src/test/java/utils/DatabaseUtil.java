package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseUtil {
    private static Connection connection;

    // Connect to PostgreSQL
    public static void connect() {
        try {
            // Update with your actual PostgreSQL DB credentials
            String url = "jdbc:postgresql://localhost:5432/MyFirstDB1";
            String username = "postgres";
            String password = "admin";
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to PostgreSQL DB!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert recipe data
    public static void insertRecipe(String... data) {
        String query = "INSERT INTO recipes (recipe_name, recipe_category, food_category, ingredients, " +
                "preparation_time, cooking_time, tag, no_of_servings, cuisine_category, recipe_description, " +
                "preparation_method, nutrient_values, recipe_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            for (int i = 0; i < data.length; i++) {
                pstmt.setString(i + 1, data[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Disconnect from DB
    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from PostgreSQL DB.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
