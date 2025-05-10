package BudgetManagement;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetStorageService {

    private static final String FILE_NAME = "budgets.dat";

    public void saveBudget(Budget budget) {
        List<Budget> budgets = loadBudgets();

        budgets.removeIf(b -> b.getUserId().equals(budget.getUserId())); // replace old
        budgets.add(budget);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(budgets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Budget getBudgetByUserId(String userId) {
        return loadBudgets().stream()
                .filter(b -> b.getUserId().equals(userId))
                .findFirst()
                .orElse(new Budget(userId));
    }

    @SuppressWarnings("unchecked")
    private List<Budget> loadBudgets() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Budget>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}
