package BudgetManagement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * Represents a user's budget, including the total limit and category-specific limits.
 * This class provides methods to set and get budget limits for different categories,
 * and also to generate spending analysis and recommendations based on actual spending.
 */
public class Budget implements Serializable {
    private String userId;
    private double totalLimit;
    private Map<String, Double> categoryLimits;
    /**
     * Constructs a Budget object for a specific user.
     * Initializes the category limits to an empty map.
     *
     * @param userId the ID of the user this budget belongs to
     */
    public Budget(String userId) {
        this.userId = userId;
        this.categoryLimits = new HashMap<>();
    }
    /**
     * Sets the budget limit for a specific category.
     *
     * @param category the category to set the limit for (e.g., "groceries")
     * @param limit the limit amount for the category
     */
    public void setLimit(String category, double limit) {
        categoryLimits.put(category, limit);
    }/**
     * Gets the budget limit for a specific category.
     * If no limit is set for the category, it returns 0.0.
     *
     * @param category the category to get the limit for
     * @return the limit for the specified category, or 0.0 if no limit is set
     */

    public double getLimit(String category) {
        return categoryLimits.getOrDefault(category, 0.0);
    }
    /**
     * Returns the map of category limits for the user's budget.
     *
     * @return the map containing category names as keys and their respective limits as values
     */
    public Map<String, Double> getCategoryLimits() {
        return categoryLimits;
    }
    /**
     * Gets the user ID associated with this budget.
     *
     * @return the user ID
     */
    public String getUserId() {
        return userId;
    }
    /**
     * Generates a spending analysis report for the user based on actual spending per category.
     * Compares actual spending with the predefined limits for each category and returns a summary.
     *
     * @param spendingPerCategory a map of categories and the actual spending amounts in those categories
     * @return a string summarizing the spending analysis for each category
     */
    public String getRecommendations(Map<String, Double> spendingPerCategory) {
        StringBuilder analysis = new StringBuilder("=== Spending Analysis ===\n");
        for (String category : categoryLimits.keySet()) {
            double limit = categoryLimits.get(category);
            double spent = spendingPerCategory.getOrDefault(category, 0.0);
            analysis.append("You have spent ")
                    .append(spent)
                    .append(" on ")
                    .append(category)
                    .append(". Limit: ")
                    .append(limit)
                    .append("\n");
        }
        return analysis.toString();
    }
}