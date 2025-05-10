package BudgetManagement;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReminderStorageService {
    private static final String FILE_NAME = "reminders.dat";

    public void saveReminders(List<Reminder> reminders) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(reminders);
        }
        catch (IOException e) {
            System.err.println("Error saving reminders: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<Reminder> loadReminders() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Reminder>) ois.readObject();
        }
        catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
