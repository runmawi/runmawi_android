package com.atbuys.runmawi;

public class CreateRazorpayOrderResponse {
    private String status;
    private String message;
    private String order_id;
    private String amount;
    private String currency;
    private String key_id;
    
    // Getters and setters
    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    public String getMessage() { 
        return message; 
    }
    
    public void setMessage(String message) { 
        this.message = message; 
    }
    
    public String getOrder_id() { 
        return order_id; 
    }
    
    public void setOrder_id(String order_id) { 
        this.order_id = order_id; 
    }
    
    public String getAmount() { 
        return amount; 
    }
    
    public void setAmount(String amount) { 
        this.amount = amount; 
    }
    
    public String getCurrency() { 
        return currency; 
    }
    
    public void setCurrency(String currency) { 
        this.currency = currency; 
    }
    
    public String getKey_id() { 
        return key_id; 
    }
    
    public void setKey_id(String key_id) { 
        this.key_id = key_id; 
    }
} 