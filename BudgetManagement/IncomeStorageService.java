package BudgetManagement;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IncomeStorageService {

    private static final String FILE_NAME = "incomes.dat";

    public void saveIncome(Income income) {
        List<Income> incomes = loadIncomes();
        incomes.add(income);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(incomes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Income> loadIncomes() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Income>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}
