package BudgetManagement;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseStorageService {
    private static final String FILE_NAME = "expenses.dat";

    public void saveExpenses(List<Expense> expenses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(expenses);
        } catch (IOException e) {
            System.err.println("Error saving expenses: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<Expense> loadExpenses() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Expense>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}

