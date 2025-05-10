package BudgetManagement;
import java.io.Serializable;
import java.time.LocalDate;

public class Reminder implements Serializable {
    private String title;
    private LocalDate date;
    private String time;
    private String userId;

    public Reminder(String title, LocalDate date, String time, String userId) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.userId = userId;
    }


    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getUserId() {
        return userId;
    }

    public void notifyUser() {
        System.out.println("Reminder: " + title + " on " + date + " at " + time);
    }

    @Override
    public String toString() {
        return ">>" + title + " on " + date + " at " + time;
    }
}


