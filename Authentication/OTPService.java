package Authentication;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPService {
    private Map<String, String> otpStore = new HashMap<>();
    private Map<String, Long> otpTimestamps = new HashMap<>();

    public String generateOTP() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }

    public void sendOTP(String phoneNumber, String otp) {
        System.out.println("[DEBUG] OTP for " + phoneNumber + ": " + otp);
        otpStore.put(phoneNumber, otp);
        otpTimestamps.put(phoneNumber, System.currentTimeMillis());
    }

    public boolean verifyOTP(String phoneNumber, String enteredOTP) {
        String storedOTP = otpStore.get(phoneNumber);
        Long timestamp = otpTimestamps.get(phoneNumber);

        if (storedOTP == null || timestamp == null) return false;

        boolean expired = (System.currentTimeMillis() - timestamp) > (5 * 60 * 1000);
        if (expired) {
            otpStore.remove(phoneNumber);
            otpTimestamps.remove(phoneNumber);
            return false;
        }

        return storedOTP.equals(enteredOTP);
    }
}
