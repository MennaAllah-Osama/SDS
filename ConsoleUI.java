
import Authentication.AuthService;
import Authentication.User;
import BudgetManagement.Reminder;
import BudgetManagement.ReminderStorageService;
import BudgetManagement.Expense;
import BudgetManagement.ExpenseStorageService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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


        while (true) {
            System.out.println("\n--- Dashboard Menu ---");
            System.out.println("1. View Profile");
            System.out.println("2. Add Reminder");
            System.out.println("3. Show My Reminders");
            System.out.println("4. Add Expense");
            System.out.println("5. Show My Expenses");
            System.out.println("6. Logout");
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
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
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

        System.out.print("Is this a recurring expense? (yes/no): ");
        String input = scanner.nextLine().trim().toLowerCase();
        boolean isRecurring = input.startsWith("y");

        Expense expense = new Expense(category, amount, date, isRecurring, user.getUserId());

        ExpenseStorageService storage = new ExpenseStorageService();
        List<Expense> expenses = storage.loadExpenses();
        expenses.add(expense);
        storage.saveExpenses(expenses);

        System.out.println(" Expense added successfully!");
    }



    private void showMyExpenses(User user) {
        ExpenseStorageService storage = new ExpenseStorageService();
        List<Expense> allExpenses = storage.loadExpenses();

        List<Expense> userExpenses = allExpenses.stream()
                .filter(e -> e.getUserId().equals(user.getUserId()))
                .toList();

        if (userExpenses.isEmpty()) {
            System.out.println("no recorded expenses.");
        }
        else {
            System.out.println("\n=== Your Expenses ===");
            for (Expense expense : userExpenses) {
                String type = expense.isRecurring() ? "<Recurring>" : "<One-Time>";
                System.out.println(">> " + expense + " " + type);
            }
        }
    }


}

