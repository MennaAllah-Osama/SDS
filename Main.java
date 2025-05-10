import Authentication.AuthService;

public class Main {
    public static void main(String[] args) {
        AuthService authService = new AuthService();
        ConsoleUI ui = new ConsoleUI(authService);
        ui.start();
    }
}
