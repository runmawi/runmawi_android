package com.atbuys.runmawi;

/*import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.OrdersCaptureRequest;
import com.paypal.orders.OrdersCaptureResponse;
import com.paypal.orders.Order;*/

public class PayPalVerifier {

    private static final String CLIENT_ID = "YOUR_CLIENT_ID";
    private static final String CLIENT_SECRET = "YOUR_CLIENT_SECRET";
    private static final String ENVIRONMENT = "sandbox"; // Use "live" for production

 /*   private final PayPalHttpClient client;

    public PayPalVerifier() {
        PayPalEnvironment environment = new PayPalEnvironment.Sandbox(CLIENT_ID, CLIENT_SECRET);
        client = new PayPalHttpClient(environment);
    }

    public boolean verifyPayment(String paymentId) {
        try {
            OrdersCaptureRequest request = new OrdersCaptureRequest(paymentId);
            OrdersCaptureResponse response = client.execute(request);

            if (response.statusCode() == 201 || response.statusCode() == 200) {
                Order order = response.result();
                // Check the order status and other details to ensure the payment is successful
                return "COMPLETED".equals(order.status());
            } else {
                // Handle error response
                System.err.println("Error: " + response.statusMessage());
                return false;
            }
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            return false;
        }
    }*/
}

