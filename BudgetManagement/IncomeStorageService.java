package BudgetManagement;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Service class to handle the storage and retrieval of income data.
 * This class provides methods to save and load income records to/from a file.
 */
public class IncomeStorageService {
    private static final String FILE_NAME = "incomes.dat";
    /**
     * Saves an income object to the storage file.
     * It loads the existing incomes, adds the new income, and writes the updated list back to the file.
     *
     * @param income the income object to be saved
     */
    public void saveIncome(Income income) {
        List<Income> incomes = loadIncomes();
        incomes.add(income);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(incomes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Loads the list of income objects from the storage file.
     * If the file does not exist or there is an error, an empty list is returned.
     *
     * @return a list of income objects
     */
    public List<Income> loadIncomes() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Income>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}