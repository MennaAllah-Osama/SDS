package BudgetManagement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Budget implements Serializable {

    private String userId;
    private double totalLimit;
    private Map<String, Double> categoryLimits;

    public Budget(String userId) {
        this.userId = userId;
        this.categoryLimits = new HashMap<>();
    }

    public void setLimit(String category, double limit) {
        categoryLimits.put(category, limit);
    }

    public double getLimit(String category) {
        return categoryLimits.getOrDefault(category, 0.0);
    }

    public Map<String, Double> getCategoryLimits() {
        return categoryLimits;
    }

    public String getUserId() {
        return userId;
    }

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
