import java.io.IOException;
import okhttp3.*;
import org.json.JSONObject;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        try {
            String baseCurrency = JOptionPane.showInputDialog("Enter the base currency (e.g. USD):");
            String targetCurrency = JOptionPane.showInputDialog("Enter the target currency (e.g. ZAR):");
            double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter the amount to convert:"));

            double convertedAmount = getConversionRate(baseCurrency, targetCurrency, amount);

            displayResult(amount, baseCurrency, convertedAmount, targetCurrency);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double getConversionRate(String baseCurrency, String targetCurrency, double amount) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().build();

        Request request = new Request.Builder()
                .url("https://api.apilayer.com/exchangerates_data/convert?to=" + targetCurrency + "&from=" + baseCurrency + "&amount=" + amount)
                .addHeader("apikey", "XGXf4WUkJm2VoRVkd7RKI963Fpc3uyH1")
                .method("GET", null)
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            return jsonResponse.getDouble("result"); // Extract the conversion rate from the JSON response
        } else {
            throw new IOException("Unexpected response code: " + response);
        }
    }

    public static void displayResult(double amount, String baseCurrency, double convertedAmount, String targetCurrency) {
        String formattedConvertedAmount = String.format("%.2f", convertedAmount);
        String message = amount + " " + baseCurrency + " is equivalent to " + formattedConvertedAmount + " " + targetCurrency;
        JOptionPane.showMessageDialog(null, message);
    }
}
