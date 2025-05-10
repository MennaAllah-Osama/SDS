package BudgetManagement;
import java.io.Serializable;
import java.time.LocalDate;


public class Expense implements Serializable {
    private String category;
    private double amount;
    private LocalDate date;
    private boolean isRecurring;
    private String userId;

    public Expense(String category, double amount, LocalDate date, boolean isRecurring, String userId) {
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.isRecurring = isRecurring;
        this.userId = userId;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean isRecurring) {
        this.isRecurring = isRecurring;
    }
    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return " " + category + ": " + amount + " EGP on " + date;
    }
}

