package BudgetManagement;

import java.io.Serializable;
import java.time.LocalDate;
/**
 * Represents an income entry, including details such as the income source, amount, date,
 * recurrence status, and the associated user ID.
 */
public class Income implements Serializable {
    private String source;
    private double amount;
    private LocalDate date;
    private boolean isRecurring;
    private String userId;
    /**
     * Constructs an Income object with the specified details.
     *
     * @param source      the source of the income (e.g., salary, freelance, bonus)
     * @param amount      the amount of income
     * @param date        the date when the income was received
     * @param isRecurring indicates whether the income is recurring
     * @param userId      the ID of the user to whom the income belongs
     */
    public Income(String source, double amount, LocalDate date, boolean isRecurring, String userId) {
        this.source = source;
        this.amount = amount;
        this.date = date;
        this.isRecurring = isRecurring;
        this.userId = userId;
    }
    /**
     * Gets the source of the income.
     *
     * @return the source of the income
     */
    public String getSource() { return source; }
    /**
     * Sets the source of the income.
     *
     * @param source the source to set
     */
    public void setSource(String source) { this.source = source; }
    /**
     * Gets the amount of income.
     *
     * @return the amount of income
     */
    public double getAmount() { return amount; }
    /**
     * Sets the amount of income.
     *
     * @param amount the amount to set
     */
    public void setAmount(double amount) { this.amount = amount; }
    /**
     * Gets the date when the income was received.
     *
     * @return the date of income
     */
    public LocalDate getDate() { return date; }
    /**
     * Sets the date when the income was received.
     *
     * @param date the date to set
     */
    public void setDate(LocalDate date) { this.date = date; }
    /**
     * Checks whether the income is recurring.
     *
     * @return true if the income is recurring, false otherwise
     */
    public boolean isRecurring() { return isRecurring; }
    /**
     * Sets the recurrence status of the income.
     *
     * @param recurring the recurrence status to set
     */
    public void setRecurring(boolean recurring) { isRecurring = recurring; }
    /**
     * Gets the user ID associated with this income.
     *
     * @return the user ID
     */
    public String getUserId() { return userId; }
    /**
     * Returns a string representation of the income, including source, amount, date,
     * and recurrence status.
     *
     * @return a string representing the income
     */
    @Override
    public String toString() {
        return source + ": " + amount + " EGP on " + date + (isRecurring ? " (Recurring)" : "");
    }
}