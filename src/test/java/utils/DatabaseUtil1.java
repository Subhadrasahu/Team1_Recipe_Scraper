package utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil1 {
   private static Connection connection;

    // Connect to PostgreSQL
    public static void connect() throws SQLException {
        // Update with your actual PostgreSQL DB credentials
		String url = "jdbc:postgresql://localhost:5432/Team1_ReceipeScrapping";
		String username = "postgres";
		String password = "madhuSql@03";
		String createTableSQL = "CREATE TABLE IF NOT EXISTS LFV_Elimination (" +
		        "id SERIAL PRIMARY KEY,Recipe_ID VARCHAR(20),Recipe_Name TEXT,Recipe_Category  TEXT,Food_Category TEXT,"
		        + "Ingredients TEXT,Preparation_Time VARCHAR(20),Cooking _Time VARCHAR(20),Tag TEXT,No_of_servings TEXT,"
		        + "Cuisine_category TEXT,Recipe_Description TEXT,"
		        + "Preparation_method TEXT,Nutrient_values TEXT,Recipe_URL TEXT);";
        //  connection = DriverManager.getConnection(url, username, password);
		
		try {
		connection = DriverManager.getConnection(url, username, password);
		System.out.println("Connected to PostgreSQL DB!");
		     /*   Statement stmt = connection.createStatement() ;

		       stmt.execute(createTableSQL);
		       System.out.println("✅ Table 'recipes' created or already exists.");*/

		   } catch (SQLException e) {
		      // System.err.println("❌ Failed to create table.");
		       e.printStackTrace();
		   }
		
    }

    
    // Insert recipe data
    public static void insertRecipe(String... data) {
    	String query = "INSERT INTO LFV_Elimination (Recipe_ID, Recipe_Name, Recipe_Category, Food_Category, " +
                "Ingredients, Preparation_Time, Cooking_Time, Tag, No_of_servings, Cuisine_category, " +
                "Recipe_Description, Preparation_method, Nutrient_values,Recipe_URL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            for (int i = 0; i < data.length; i++) {
                pstmt.setString(i + 1, data[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void insertRecipeAdd(String... data) {
    	String query = "INSERT INTO LFV_ADD (Recipe_ID, Recipe_Name, Recipe_Category, Food_Category, " +
                "Ingredients, Preparation_Time, Cooking_Time, Tag, No_of_servings, Cuisine_category, " +
                "Recipe_Description, Preparation_method, Nutrient_values,Recipe_URL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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