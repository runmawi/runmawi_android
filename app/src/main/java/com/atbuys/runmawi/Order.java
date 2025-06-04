package com.atbuys.runmawi;

import org.json.JSONObject;
import java.util.Map;
import java.util.HashMap;

public class Order {

    private String amount, currency, receipt, payment_capture;
    private Map<String, Object> notes; // Changed from JSONObject to Map for better Gson serialization

    public Order(String amount, String currency, String receipt, String payment_capture, JSONObject notes) {
        this.amount = amount;
        this.currency = currency;
        this.receipt = receipt;
        this.payment_capture = payment_capture;
        
        // Convert JSONObject to Map for better Gson serialization
        this.notes = new HashMap<>();
        if (notes != null) {
            try {
                java.util.Iterator<String> keys = notes.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    this.notes.put(key, notes.get(key));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Getter for notes, which will be used by Retrofit/Gson for serialization
    public Map<String, Object> getNotes() {
        return notes;
    }

    // Getters for other fields for Gson serialization
    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getReceipt() {
        return receipt;
    }

    public String getPayment_capture() {
        return payment_capture;
    }
}
