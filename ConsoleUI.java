import Authentication.AuthService;
import Authentication.User;

import java.io.IOException;
import java.util.Scanner;

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
        } catch (IOException e) {
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

        System.out.println("Welcome, " + currentUser.getUsername() + "!");

        while (true) {
            System.out.println("\n--- Dashboard Menu ---");
            System.out.println("1. View Profile");
            System.out.println("2. Logout");
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
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
