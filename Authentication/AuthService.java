package Authentication;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class AuthService {
    private List<User> users;
    private FileStorageService storageService;
    private User currentUser;
    private OTPService otpService;

    public AuthService() {
        this.storageService = new FileStorageService();
        this.users = storageService.loadUsers();
        this.otpService = new OTPService();
    }

    public String registerUser(String username, String email, String phoneNumber,
                               String password, String confirmPassword) throws IOException {
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match.";
        }

        String validationError = validateUserInput(username, email, phoneNumber, password);
        if (validationError != null) {
            return validationError;
        }

        if (users.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username))) {
            return "Username already exists.";
        }

        if (users.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email))) {
            return "Email already registered.";
        }

        if (users.stream().anyMatch(u -> u.getPhoneNumber().equals(phoneNumber))) {
            return "Phone number already registered.";
        }

        String passwordHash = Integer.toString(password.hashCode());

        User newUser = new User(username, email, phoneNumber, passwordHash);
        users.add(newUser);
        storageService.saveUsers(users);

        String otp = otpService.generateOTP();
        otpService.sendOTP(phoneNumber, otp);

        return "OTP sent to " + phoneNumber;
    }

    public boolean verifyOTP(String phoneNumber, String enteredOTP) throws IOException {
        if (otpService.verifyOTP(phoneNumber, enteredOTP)) {
            users.stream()
                    .filter(u -> u.getPhoneNumber().equals(phoneNumber))
                    .findFirst()
                    .ifPresent(u -> {
                        u.setVerified(true);
                        currentUser = u;
                    });
            storageService.saveUsers(users);
            return true;
        }
        return false;
    }

    public String login(String usernameOrEmail, String password) {
        User user = users.stream()
                .filter(u -> (u.getUsername().equalsIgnoreCase(usernameOrEmail) ||
                        u.getEmail().equalsIgnoreCase(usernameOrEmail)) &&
                        u.isVerified())
                .findFirst()
                .orElse(null);

        if (user == null) {
            return "User not found or not verified.";
        }

        if (user.getPasswordHash().equals(Integer.toString(password.hashCode()))) {
            currentUser = user;
            return "Login successful!";
        } else {
            return "Invalid password.";
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private String validateUserInput(String username, String email,
                                     String phoneNumber, String password) {
        if (username == null || username.length() < 3 || username.length() > 50) {
            return "Username must be between 3-50 characters.";
        }
        if (username.contains(" ")) {
            return "Username cannot contain spaces.";
        }
        if (isReservedUsername(username)) {
            return "Username is reserved.";
        }

        if (!Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(email).matches()) {
            return "Invalid email format.";
        }

        if (!Pattern.compile("^\\+?[0-9]{16}$").matcher(phoneNumber).matches()) {
            return "Phone number must be 16 digits with optional + prefix.";
        }

        if (!Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$")
                .matcher(password).matches()) {
            return "Password must be 8-16 chars with uppercase, lowercase, number, and special char.";
        }

        return null;
    }

    private boolean isReservedUsername(String username) {
        List<String> reservedNames = List.of("admin", "root", "system", "user");
        return reservedNames.contains(username.toLowerCase());
    }
}
