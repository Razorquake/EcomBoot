import java.net.HttpURLConnection;
import java.net.URL;

public class HealthCheck {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: java HealthCheck.java <url>");
            System.exit(1);
        }

        var url = new URL(args[0]);
        var conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(3000); // 3-second timeout
        conn.setReadTimeout(3000);

        int responseCode = conn.getResponseCode();

        // Exit with 0 for success (2xx codes), 1 for failure.
        if (responseCode >= 200 && responseCode <= 299) {
            System.exit(0);
        } else {
            System.exit(1);
        }
    }
}