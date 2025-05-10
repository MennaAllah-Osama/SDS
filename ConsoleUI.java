import Authentication.AuthService;
import Authentication.User;
import BudgetManagement.Reminder;
import BudgetManagement.ReminderStorageService;
import BudgetManagement.Expense;
import BudgetManagement.ExpenseStorageService;
import BudgetManagement.Income;
import BudgetManagement.IncomeStorageService;
import BudgetManagement.Budget;
import BudgetManagement.BudgetStorageService;


import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

public class ConsoleUI {
    private AuthService authService;
    private Scanner scanner;

    public ConsoleUI(AuthService authService) {
        this.authService = authService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n=== Welcome to Budgeting App ===");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    showSignUp();
                    break;
                case 2:
                    showLogin();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void showSignUp() {
        System.out.println("\n=== Sign Up ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Phone Number (+country code): ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Confirm Password: ");
        String confirmPassword = scanner.nextLine();

        try {
            String result = authService.registerUser(username, email, phoneNumber, password, confirmPassword);
            System.out.println(result);

            if (result.startsWith("OTP sent to")) {
                verifyOTP(phoneNumber);
                if (authService.getCurrentUser() != null) {
                    showDashboard();
                }
            }
        }
        catch (IOException e) {
            System.out.println("Error during registration: " + e.getMessage());
        }
    }

    private void verifyOTP(String phoneNumber) {
        System.out.print("Enter OTP sent to your phone: ");
        String otp = scanner.nextLine();

        try {
            if (authService.verifyOTP(phoneNumber, otp)) {
                System.out.println("Verification successful! You can now login.");
            } else {
                System.out.println("Invalid OTP or OTP expired.");
            }
        } catch (IOException e) {
            System.out.println("Error verifying OTP: " + e.getMessage());
        }
    }

    private void showLogin() {
        System.out.println("\n=== Login ===");
        System.out.print("Username/Email: ");
        String usernameOrEmail = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        String result = authService.login(usernameOrEmail, password);
        System.out.println(result);

        if (result.startsWith("Login successful")) {
            User user = authService.getCurrentUser();
            System.out.println("\nWelcome, " + user.getUsername() + "!");
            showDashboard();
        }
    }

    private void showDashboard() {
        System.out.println("\n=== Dashboard ===");
        User currentUser = authService.getCurrentUser();

        if (currentUser == null) {
            System.out.println("No user is currently logged in.");
            return;
        }


//        System.out.println("Welcome, " + currentUser.getUsername() + "!");

        while (true) {
            System.out.println("\n--- Dashboard Menu ---");
            System.out.println("1. View Profile");
            System.out.println("2. Add Reminder");
            System.out.println("3. Show My Reminders");
            System.out.println("4. Add Expense");
            System.out.println("5. Show My Expenses");
            System.out.println("6. Add Income");
            System.out.println("7. Show My Incomes");
            System.out.println("8. Set Budget for Category");
            System.out.println("9. Analyze Spending");

            System.out.println("10. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\nYour Profile:");
                    System.out.println("Username: " + currentUser.getUsername());
                    System.out.println("Email: " + currentUser.getEmail());
                    System.out.println("Phone: " + currentUser.getPhoneNumber());
                    break;
                case 2:
                    addReminder(currentUser);
                    break;
                case 3:
                    showMyReminders(currentUser);
                    break;
                case 4:
                    addExpense(currentUser);
                    break;
                case 5:
                    showMyExpenses(currentUser);
                    break;
                case 6:
                    addIncome(currentUser); break;
                case 7: showMyIncomes(currentUser); break;
                case 8: setBudget(currentUser); break;
                case 9: analyzeSpending(currentUser); break;

                case 10:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
    private void addIncome(User user) {
        System.out.print("Enter income source (e.g., salary): ");
        String source = scanner.nextLine();
        if (source.length() < 3 || source.length() > 50) {
            System.out.println("Invalid source length.");
            return;
        }

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // consume newline
        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return;
        }

        System.out.print("Enter date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        if (date.isAfter(LocalDate.now())) {
            System.out.println("Date cannot be in the future.");
            return;
        }

        System.out.print("Is this recurring? (true/false): ");
        boolean isRecurring = Boolean.parseBoolean(scanner.nextLine());

        Income income = new Income(source, amount, date, isRecurring, user.getUserId());
        IncomeStorageService storage = new IncomeStorageService();
        storage.saveIncome(income);
        System.out.println("Income saved.");
    }

    private void showMyIncomes(User user) {
        IncomeStorageService storage = new IncomeStorageService();
        List<Income> incomes = storage.loadIncomes();
        System.out.println("=== My Incomes ===");
        incomes.stream()
                .filter(i -> i.getUserId().equals(user.getUserId()))
                .forEach(System.out::println);
    }
    private void setBudget(User user) {
        System.out.print("Enter category (e.g., groceries, rent): ");
        String category = scanner.nextLine().trim();

         if (!category.matches("^[a-zA-Z0-9 ]{3,50}$")) {
            System.out.println("Invalid category. Must be 3-50 characters and contain only letters, numbers, and spaces.");
            return;
        }

         List<String> validCategories = List.of("groceries", "rent", "entertainment", "transportation", "bills");
        if (validCategories.stream().noneMatch(v -> v.equalsIgnoreCase(category))) {
            System.out.println("Invalid category. Choose from: " + String.join(", ", validCategories));
            return;
        }

        System.out.print("Enter budget amount: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine());
            if (amount <= 0) {
                System.out.println("Amount must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format. Please enter a number like 500.00");
            return;
        }

        BudgetStorageService storage = new BudgetStorageService();
        Budget budget = storage.getBudgetByUserId(user.getUserId());
        budget.setLimit(category.toLowerCase(), amount);
        storage.saveBudget(budget);

        System.out.println("✅ Budget saved for category: " + category);
    }


    private void analyzeSpending(User user) {
        ExpenseStorageService expenseStorage = new ExpenseStorageService();
        List<Expense> expenses = expenseStorage.loadExpenses();

        Map<String, Double> spendingPerCategory = new HashMap<>();

        for (Expense expense : expenses) {
            if (expense.getUserId().equals(user.getUserId())) {
                String category = expense.getCategory().toLowerCase();
                spendingPerCategory.put(category,
                        spendingPerCategory.getOrDefault(category, 0.0) + expense.getAmount());
            }
        }

         if (spendingPerCategory.isEmpty()) {
            System.out.println("No expenses found to analyze.");
            return;
        }

        System.out.println("\n=== Spending Summary ===");
        for (Map.Entry<String, Double> entry : spendingPerCategory.entrySet()) {
            System.out.printf("You have spent %.2f on %s this month.\n", entry.getValue(), entry.getKey());
        }

         BudgetStorageService budgetStorage = new BudgetStorageService();
        Budget budget = budgetStorage.getBudgetByUserId(user.getUserId());

        System.out.println("\n=== Budget Recommendations ===");
        String recommendations = budget.getRecommendations(spendingPerCategory);
        System.out.println(recommendations);
    }


    private void addReminder(User user) {
        System.out.print("Enter reminder title: ");
        String title = scanner.nextLine();

        System.out.print("Enter reminder date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateStr);

        System.out.print("Enter reminder time (H:m): ");
        String time = scanner.nextLine();

        Reminder reminder = new Reminder(title, date, time, user.getUserId());

        ReminderStorageService storage = new ReminderStorageService();
        List<Reminder> reminders = storage.loadReminders();
        reminders.add(reminder);
        storage.saveReminders(reminders);

        System.out.println("Reminder saved successfully!");
    }

    private void showMyReminders(User user) {
        ReminderStorageService storage = new ReminderStorageService();
        List<Reminder> reminders = storage.loadReminders();

        List<Reminder> userReminders = reminders.stream()
                .filter(remine -> remine.getUserId().equals(user.getUserId()))
                .collect(Collectors.toList());

        if (userReminders.isEmpty()) {
            System.out.println("No reminders found.");
        }
        else {
            System.out.println("\n=== Your Reminders ===");
            for (Reminder remine : userReminders) {
                System.out.println("Reminder>> " + remine.getTitle() + " on " + remine.getDate() + " at " + remine.getTime()+"!!\n");
            }
        }
    }

    private void addExpense(User user) {
        System.out.print("Enter expense amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter expense category: ");
        String category = scanner.nextLine();

        System.out.print("Enter expense date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        Expense expense = new Expense(amount, category, date, user.getUserId());

        ExpenseStorageService storage = new ExpenseStorageService();
        List<Expense> allExpenses = storage.loadExpenses();
        allExpenses.add(expense);
        storage.saveExpenses(allExpenses);

        System.out.println("Expense added successfully!");
    }

    private void showMyExpenses(User user) {
        ExpenseStorageService storage = new ExpenseStorageService();
        List<Expense> expenses = storage.loadExpenses();

        System.out.println("\n=== Your Expenses ===");
        for (Expense e : expenses) {
            if (e.getUserId().equals(user.getUserId())) {
                System.out.println(e);
            }
        }
    }


}
