package BudgetManagement;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * This class is responsible for managing the storage of budget data. It provides methods
 * to save a user's budget, retrieve a user's budget by their user ID, and load all budgets
 * from the file.
 */
public class BudgetStorageService {
    private static final String FILE_NAME = "budgets.dat";
    /**
     * Saves the specified budget to the storage file.
     * If a budget for the same user already exists, it is replaced with the new budget.
     *
     * @param budget the budget to be saved
     */
    public void saveBudget(Budget budget) {
        List<Budget> budgets = loadBudgets();
        // Remove the old budget for the user (if it exists)
        budgets.removeIf(b -> b.getUserId().equals(budget.getUserId())); // replace old
        budgets.add(budget);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(budgets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Retrieves the budget associated with the specified user ID.
     * If no budget is found for the user, a new empty budget is returned.
     *
     * @param userId the ID of the user whose budget is to be retrieved
     * @return the budget for the specified user ID
     */
    public Budget getBudgetByUserId(String userId) {
        return loadBudgets().stream()
                .filter(b -> b.getUserId().equals(userId))
                .findFirst()
                .orElse(new Budget(userId));
    }
    /**
     * Loads all budgets from the storage file.
     * If the file doesn't exist or can't be read, an empty list is returned.
     *
     * @return a list of all saved budgets
     */
    private List<Budget> loadBudgets() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Budget>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}