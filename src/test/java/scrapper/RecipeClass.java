package scrapper;

public class RecipeClass {
    String recipeID;
    String recipeName;
    String recipeCategory;
    String foodCategory;
    String ingredients;
    String preparation_Time;
    String cookingTimeString;
    String tag;
    String noOfServings;
    String cuisineCategory;
    String recipeDescription;

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeCategory() {
        return recipeCategory;
    }

    public void setRecipeCategory(String recipeCategory) {
        this.recipeCategory = recipeCategory;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getPreparation_Time() {
        return preparation_Time;
    }

    public void setPreparation_Time(String preparation_Time) {
        this.preparation_Time = preparation_Time;
    }

    public String getCookingTimeString() {
        return cookingTimeString;
    }

    public void setCookingTimeString(String cookingTimeString) {
        this.cookingTimeString = cookingTimeString;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getNoOfServings() {
        return noOfServings;
    }

    public void setNoOfServings(String noOfServings) {
        this.noOfServings = noOfServings;
    }

    public String getCuisineCategory() {
        return cuisineCategory;
    }

    public void setCuisineCategory(String cuisineCategory) {
        this.cuisineCategory = cuisineCategory;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    public String getPreparationMethod() {
        return preparationMethod;
    }

    public void setPreparationMethod(String preparationMethod) {
        this.preparationMethod = preparationMethod;
    }

    public String getNutrientValues() {
        return nutrientValues;
    }

    public void setNutrientValues(String nutrientValues) {
        this.nutrientValues = nutrientValues;
    }

    public String getRecipeURL() {
        return recipeURL;
    }

    public void setRecipeURL(String recipeURL) {
        this.recipeURL = recipeURL;
    }

    String preparationMethod;
    String nutrientValues;
    String recipeURL;

    public String getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    @Override
    public String toString() {
        return "RecipeClass{" +
                "recipeID='" + recipeID + '\'' +
                ", recipeName='" + recipeName + '\'' +
                ", recipeCategory='" + recipeCategory + '\'' +
                ", foodCategory='" + foodCategory + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", preparation_Time='" + preparation_Time + '\'' +
                ", cookingTimeString='" + cookingTimeString + '\'' +
                ", tag='" + tag + '\'' +
                ", noOfServings='" + noOfServings + '\'' +
                ", cuisineCategory='" + cuisineCategory + '\'' +
                ", recipeDescription='" + recipeDescription + '\'' +
                ", preparationMethod='" + preparationMethod + '\'' +
                ", nutrientValues='" + nutrientValues + '\'' +
                ", recipeURL='" + recipeURL + '\'' +
                '}';
    }
}
