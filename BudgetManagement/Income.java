package BudgetManagement;

import java.io.Serializable;
import java.time.LocalDate;

public class Income implements Serializable {

    private double amount;
    private String source;
    private LocalDate date;
    private String userId;

    public Income(double amount, String source, LocalDate date, String userId) {
        this.amount = amount;
        this.source = source;
        this.date = date;
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public String getSource() {
        return source;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return source + ": " + amount + " EGP on " + date;
    }
}
